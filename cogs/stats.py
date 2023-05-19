from discord import *

from utils.bot_embeds import InformativeEmbed
from utils.mod_data import ModData, CurseForgeData


class StatsCog(Cog):
    def __init__(self, bot):
        self.bot = bot

    @Cog.listener()
    async def on_ready(self):
        await CurseForgeData.get_files()

    @slash_command(
        name="downloads",
        name_localizations={"fr": "téléchargements"}
    )
    @option("mod_version", choices=ModData.get_mod_versions())
    async def downloads_command(self, ctx, mod_version: str):
        await ctx.defer()

        github_downloads = await ModData.get_github_downloads()
        modrinth_downloads = await ModData.get_modrinth_downloads()
        curseforge_downloads = await ModData.get_curseforge_downloads()

        total_downloads = max(0, github_downloads) + max(0, modrinth_downloads) + max(0, curseforge_downloads)

        embed = InformativeEmbed(title="Mod Downloads", description=f"**Total: {total_downloads}**")
        embed.add_field(name="Curseforge", value=f"{curseforge_downloads} downloads")
        embed.add_field(name="Modrinth", value=f"{modrinth_downloads} downloads")
        embed.add_field(name="Github", value=f"{github_downloads} downloads")
        await ctx.respond(embed=embed)


def setup(bot):
    bot.add_cog(StatsCog(bot))