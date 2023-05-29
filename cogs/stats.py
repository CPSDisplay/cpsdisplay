from discord import *

from utils.bot_embeds import InformativeEmbed
from utils.mod_data import GithubData, ModrinthData, CurseForgeData


class StatsCog(Cog):
    def __init__(self, bot):
        self.bot = bot

    @slash_command(
        name="downloads",
        name_localizations={"fr": "téléchargements"},
        description="Statistics on mod's downloads",
        description_localizations={"fr": "Statistiques sur les téléchargements du mod"}
    )
    # @option("mod_version", choices=ModData.get_mod_versions(), default=None)
    @option("game_version", choices=GithubData.get_game_versions(), default=None)
    async def downloads_command(self, ctx, game_version = None):
        await ctx.defer()

        curseforge_downloads = await CurseForgeData.get_downloads(game_version=game_version or "*")
        modrinth_downloads = await ModrinthData.get_downloads(game_version=game_version or "*")
        github_downloads = await GithubData.get_downloads(game_version=game_version or "*")

        total_downloads = github_downloads + modrinth_downloads + curseforge_downloads

        embed = InformativeEmbed(title="Mod Downloads", description=f"**Total: {total_downloads}**")
        embed.add_field(name="Curseforge", value=f"{curseforge_downloads} downloads" + (" (outdated because curseforge's api suck)" if game_version else ""))
        embed.add_field(name="Modrinth", value=f"{modrinth_downloads} downloads")
        embed.add_field(name="Github", value=f"{github_downloads} downloads")
        await ctx.respond(embed=embed)


def setup(bot):
    bot.add_cog(StatsCog(bot))