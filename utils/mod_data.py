import json
from distutils.version import StrictVersion
from pprint import pprint
from urllib.parse import (ParseResult, parse_qsl, unquote, urlencode, urljoin,
                          urlparse)

import aiohttp
from requests import get

from utils.references import References

class SafeDict(dict):
    def __missing__(self, key):
        return "{" + key + "}"

class EndPoints:
    def format_endpoints(clazz, params):
        for attr in clazz.__dict__:
            value = clazz.__dict__[attr]
            if isinstance(value, str):
                value = value.format_map(SafeDict(**params))
                setattr(clazz, attr, value)

    def add_param(self, url, params):
        url = unquote(url)
        parsed_url = urlparse(url)
        get_args = parsed_url.query
        parsed_get_args = dict(parse_qsl(get_args))
        parsed_get_args.update(params)
        
        # convert what needs to be converted to str
        parsed_get_args.update(
            {k: json.dumps(v) for k, v in parsed_get_args.items()
            if isinstance(v, (bool, dict, list, tuple))}
        )
        
        encoded_get_args = urlencode(parsed_get_args, doseq=True)
        new_url = ParseResult(
            parsed_url.scheme, parsed_url.netloc, parsed_url.path,
            parsed_url.params, encoded_get_args, parsed_url.fragment
        ).geturl()
        return new_url

class GithubEndPoints(EndPoints):
    LASTEST_RELEASE = "repos/{username}/{repo}/releases/latest"
    RELEASE_BY_TAG = "/repos/{username}/{repo}/releases/tags/{tag}"
    RELEASES = "repos/{username}/{repo}/releases"
    TAGS = "repos/{username}/{repo}/tags"

    def __init__(self):
        GithubEndPoints.format_endpoints(params)
        # for attr in GithubEndPoints.__dict__:
        #     value = GithubEndPoints.__dict__[attr]
        #     if isinstance(value, str):
        #         value = value.format_map(SafeDict(username=References.GITHUB_USERNAME, repo=References.GITHUB_REPO))
        #         setattr(GithubEndPoints, attr, value)

class _GithubData:
    BASE_URL: str = "https://api.github.com/"
    END_POINTS = GithubEndPoints()

    MODRINTH_URL = "https://api.modrinth.com/v2/project/cpsdisplay"
    CURSEFORGE_URL = "https://www.curseforge.com/api/v1/mods/search?gameId=432&index=0&classId=6&filterText=cpsdisplay&gameVersion=1.8.9&pageSize=20&sortField=1&categoryIds%5B0%5D=424&categoryIds%5B1%5D=423&gameFlavors%5B0%5D=1"

    @property
    def headers(self):
        return {
            "Content-Type": "application/json"
        }

    def get_url(self, url: str):
        return urljoin(self.BASE_URL, url)

    def get_mod_versions(self) -> list:
        tags = get(self.get_url(self.END_POINTS.TAGS), headers=self.headers).json()
        return [tag["name"] for tag in tags]

    def get_game_versions(self) -> list:
        game_versions = []
        for asset in get(self.get_url(self.END_POINTS.LASTEST_RELEASE), headers=self.headers).json().get("assets", []):
            name = asset["name"]
            if "sources" in name:
                continue
            parts = name.replace("jar", "").split("-")
            game_versions.append(parts[1].replace("mc", ""))

        return sorted(game_versions, key=StrictVersion)

    async def get_data(self, url: str) -> dict:
        async with aiohttp.ClientSession(headers=self.headers) as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()

    async def get_assets(self, game_version: str = "*") -> dict:
        assets = []
        if releases := await self.get_data(self.get_url(self.END_POINTS.RELEASES)):
            for release in releases:
                for asset in release.get("assets"):
                    name = asset["name"]

                    if "sources" in name:
                        continue
                    if game_version != "*" and not game_version in name:
                        continue
                    
                    assets.append(asset)

        return assets

    async def get_downloads(self, game_version: str = "*") -> int:
        assets = await self.get_assets(game_version)
        return sum([asset.get("download_count", 0) for asset in assets])


GithubData = _GithubData()

class CurseForgeEndPoints(EndPoints):
    GET_FILES = "/v1/mods/%s/files"

    def game_version(self, url, version): 
        return self.add_param(url, {"gameVersion": version})

class _CurseForgeData:
    BASE_URL = "https://api.curseforge.com/"
    END_POINTS = CurseForgeEndPoints()

    def __init__(self):
        self.api_key = References.CURSEFORGE_API_KEY
    
    @property
    def headers(self):
        return {
            "Accept": "application/json",
            "x-api-key": self.api_key
        }

    def get_url(self, url: str):
        return urljoin(self.BASE_URL, url)

    async def get_data(self, url) -> dict:
        async with aiohttp.ClientSession(headers=self.headers) as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()
        return {}
    
    async def get_files(self, game_version: str = "*"):
        url = self.get_url(self.END_POINTS.GET_FILES)
        if game_version != "*":
            url = self.END_POINTS.game_version(url, game_version)
        
        data = await self.get_data(url)
        return data["data"]

    async def get_downloads(self, game_version: str = "*") -> int:
        files = await self.get_files(game_version)
        return sum([file.get("downloadCount", 0) for file in files])


CurseForgeData = _CurseForgeData()

class ModrinthEndPoints(EndPoints):
    GET_VERSION = "v2/project/%s/version" % References.MODRINTH_MODSLUG

    def game_version(self, url, *args):
        return self.add_param(url, {"game_versions": args})

class _ModrinthData():
    BASE_URL = "https://api.modrinth.com/"
    END_POINTS = ModrinthEndPoints()

    @property
    def headers(self):
        return {
            "Accept": "application/json",
            "User-Agent": "dams4k/minecraft-cpsdisplay"
        }
    
    def get_url(self, url: str):
        return urljoin(self.BASE_URL, url)

    async def get_data(self, url) -> dict:
        async with aiohttp.ClientSession(headers=self.headers) as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()
        return {}

    async def get_files(self, game_version: str="*") -> dict:
        url = self.get_url(self.END_POINTS.GET_VERSION)
        if game_version != "*":
            url = self.END_POINTS.game_version(url, game_version)
        
        files = await self.get_data(url)
        
        if isinstance(files, list):
            return files
        return []
    
    async def get_downloads(self, game_version: str = "*") -> int:
        files = await self.get_files(game_version)
        return sum([file.get("downloads", 0) for file in files])


ModrinthData = _ModrinthData()