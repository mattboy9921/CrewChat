CrewChat is a chat plugin developed for the Crew Craft Server.

Features:
 - General chat
 - Chat channels
 - Permissions
 - Private messages
 - RAW messages with hover and click actions
 - Player info/status messages
 - DiscordSRV integration

Commands:
 - `/crewchat` - Shows version
 - `/crewchat reload` - Reloads plugin
 - `/crewchat help` - Shows CrewChat command help
 - Alias: `/cc`
 
 
 - `/chat info` - Shows channel list, active channel and subscribed channels
 - `/chat info channel <channel>` - Shows channel name, nickname and color
 - `/chat status <status>` - Sets player status message
 - `/chat subscribe <channel>` - Subscribes player to `<channel>`
 - `/chat unsubscribe <channel>` - Unsubscribes player from `<channel>`
 - `/chat switch <channel>` - Switches player's active channel
 - Alias: `/c`
 
 
 - `/me <message>` - Sends third person chat message
 
 
 - `/msg <player> <message>` - Sends a private message to a player
 - Alias: `/tell`, `/whisper`, `/w`
 
 
 - `/reply <message>` - Replies to the last received private message.
 - Alias: `/r`
 
 
Permissions:
 - `crewchat.use` - Allows player to use `/crewchat`
 - `crewchat.chat` - Allows player to use `/chat`
 - `crewchat.me` - Allows player to use `/me`
 - `crewchat.pm` - Allows player to use `/msg` and `/reply`
 - `crewchat.subscribe.<channel>` - Allows player to subscribe to `<channel>`
 - `crewchat.unsubscribe.<channel>` - Allows player to unsubscribe from `<channel>`
 - `crewchat.active.<channel>` - Allows player to set `<channel>` as active channel
 - `crewchat.speak.<channel>` - Allows player to speak in `<channel>`
 - `crewchat.reload` - Allows player to use `/crewchat reload`
 - `crewchat.chat.info` -  Allows player to run `/chat info`
 
Tested working on Paper 1.13.2.