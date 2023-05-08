import aiohttp
from utils.references import References

from pprint import pprint

class ModDataVersion:
    LATEST = 0

class _ModData:
    GITHUB_URL = "https://api.github.com/repos/dams4k/minecraft-cpsdisplay/releases"
    MODRINTH_URL = "https://api.modrinth.com/v2/project/cpsdisplay"
    CURSEFORGE_URL = "https://www.curseforge.com/api/v1/mods/search?gameId=432&index=0&classId=6&filterText=cpsdisplay&gameVersion=1.8.9&pageSize=20&sortField=1&categoryIds%5B0%5D=424&categoryIds%5B1%5D=423&gameFlavors%5B0%5D=1"

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
        if releases := await self.get_data(_ModData.GITHUB_URL):
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