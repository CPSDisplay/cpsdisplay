from bot import CPSDisplayBot
from utils.references import References

bot = CPSDisplayBot()
bot.load_cogs(References.COGS_FOLDER)
bot.run(References.BOT_TOKEN)