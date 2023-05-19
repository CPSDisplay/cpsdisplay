import json
import typing
import os

class _References:
    BOT_PATH: str = "datas/bot.json"
    DEFAULT_LOGS_FOLDER = "datas/logs"

    def __init__(self) -> None:
        if not os.path.exists(self.BOT_PATH):
            os.makedirs(os.path.dirname(self.BOT_PATH))
            with open(self.BOT_PATH, "w") as f:
                data = {
                    "bot_token": input("Bot token > ")
                }

                json.dump(data, f, indent=4)


        with open(self.BOT_PATH, "r") as f:
            data = json.load(f)

            self.BOT_TOKEN = data["bot_token"]

            self.CURSEFORGE_API_KEY = data.get("curseforge_api_key")
            self.CURSEFORGE_MOD_ID = data.get("curseforge_modid")

            self.COGS_FOLDER = data.get("cogs_folder", "cogs")
            self.DEBUG_GUILDS = data.get("debug_guilds", [])
    
    def get_guild_folder(self, *end):
        return os.path.join(self.GUILDS_FOLDER, *end)

References: _References = _References()