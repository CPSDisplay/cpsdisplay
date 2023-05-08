import os

import discord


class CPSDisplayBot(discord.Bot):
    async def on_ready(self):
        os.system("clear||cls")
        print(self.user, "is now ready")
        print("py-cord version:", discord.__version__)
        print("extensions:", end="")
        print("\n  - ".join([""] + self.extensions_path()))
        
        await self.change_presence(status=discord.Status.idle)


    def load_cogs(self, path: str):
        for cog_file in self.get_cogs_file(path):
            if "debug" in cog_file and not References.DEBUG_MODE: continue
            err = self.load_extension(cog_file.replace("/", ".")[:-3])

    def reload_cogs(self, path: str):
        for cog_file in self.get_cogs_file(path):
            if "debug" in cog_file and not References.DEBUG_MODE: continue
            self.reload_extension(cog_file.replace("/", ".")[:-3])
    
    def get_cogs_file(self, path: str) -> list:
        cogs_file = []

        for filename in os.listdir(path):
            if os.path.isfile(path + "/" + filename):
                if filename.endswith(".py"):
                    cogs_file.append(f"{path}/{filename}")
            
            elif os.path.isdir(path + "/" + filename):
                cogs_file += self.get_cogs_file(path + "/" + filename)

        return cogs_file

    def extensions_path(self):
        return [str(ext.__name__).replace(".", "/") + ".py" for ext in self.extensions.values()]