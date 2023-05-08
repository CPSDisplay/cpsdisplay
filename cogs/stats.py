from discord import *
from utils.bot_embeds import InformativeEmbed

class StatsCog(Cog):
    def __init__(self, bot):
        self.bot = bot

    @slash_command(
        name="statistics",
        name_localizations={"fr": "statistiques"}
    )
    async def stats_command(self, ctx):
        await ctx.defer()

        embed = InformativeEmbed(title="Mod Statistics")
        # embed.add_field()
        await ctx.respond(embed=embed)


def setup(bot):
    bot.add_cog(StatsCog(bot))