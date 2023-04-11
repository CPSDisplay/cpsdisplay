async function loadDiscordPresenceCount() {
    const discordResponse = await fetch("https://discord.com/api/guilds/1094713373758865449/widget.json");
    const discordData = await discordResponse.json();
    element = document.getElementById("discord-presence-count");
    element.innerText = discordData["presence_count"] + " online";
}