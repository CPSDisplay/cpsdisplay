from pprint import pprint
from urllib.parse import urljoin

import aiohttp
from requests import get

from utils.references import References


class ModDataVersion:
    LATEST = 0

class _ModData:
    GITHUB_RELEASES_URL = "https://api.github.com/repos/dams4k/minecraft-cpsdisplay/releases"
    GITHUB_TAGS_URL = "https://api.github.com/repos/dams4k/minecraft-cpsdisplay/tags"
    MODRINTH_URL = "https://api.modrinth.com/v2/project/cpsdisplay"
    CURSEFORGE_URL = "https://www.curseforge.com/api/v1/mods/search?gameId=432&index=0&classId=6&filterText=cpsdisplay&gameVersion=1.8.9&pageSize=20&sortField=1&categoryIds%5B0%5D=424&categoryIds%5B1%5D=423&gameFlavors%5B0%5D=1"

    def get_mod_versions(self) -> list:
        tags = get(_ModData.GITHUB_TAGS_URL).json()
        return [tag["name"] for tag in tags]

    async def get_data(self, url: str) -> dict:
        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()

    async def get_curseforge_data(self) -> dict:
        if mods_data := (await self.get_data(_ModData.CURSEFORGE_URL)).get("data", []):
            for mod_data in mods_data:
                if mod_data.get("id") == References.CURSEFORGE_MOD_ID:
                    return mod_data
        return {}

    async def get_github_downloads(self) -> int:
        downloads = -1
        if releases := await self.get_data(_ModData.GITHUB_RELEASES_URL):
            downloads = 0
            for release in releases:
                downloads += sum([asset.get("download_count", 0) for asset in release.get("assets", []) if not "source" in asset.get("name", "")])
        
        return downloads

    async def get_modrinth_downloads(self) -> int:
        downloads = -1
        if data := await self.get_data(_ModData.MODRINTH_URL):
            downloads = data.get("downloads", -1)

        return downloads

    async def get_curseforge_downloads(self) -> int:
        downloads = -1
        if data := await self.get_curseforge_data():
            downloads = data.get("downloads", -1)

        return downloads

ModData = _ModData()

class _CurseForgeData:
    BASE_URL = "https://api.curseforge.com/"

    class EndPoint:
        GET_FILES = "/v1/mods/%s/files" % References.CURSEFORGE_MOD_ID

    def __init__(self):
        self.api_key = References.CURSEFORGE_API_KEY
    
    @property
    def headers(self):
        return {
            "Accept": "application/json",
            "x-api-key": "$2a$10$yNvq9o/x2EbNhmuxFeM6XuHa62zEcj8JbaM5LQwXagFadzm9IsVGW"
        }

    def get_url(self, url: str):
        return urljoin(_CurseForgeData.BASE_URL, url)

    async def get_data(self, url) -> dict:
        async with aiohttp.ClientSession(headers=self.headers) as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()
        return {}
    
    async def get_files(self):
        url = self.get_url(_CurseForgeData.EndPoint.GET_FILES)
        
        files = await self.get_data(url)
        pprint(files)

CurseForgeData = _CurseForgeData()