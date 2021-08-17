CrewChat is a chat plugin developed for the Crew Craft Server.

![Build Status](https://github.com/mattboy9921/CrewChat/actions/workflows/maven.yml/badge.svg)

**Features**
 - General chat
 - Chat channels
 - Permissions
 - Private messages
 - RAW messages with hover and click actions
 - Support for clickable links in chat
 - Player info/status messages
 - Ping players by name
 - Mute/deafen functions
 - DiscordSRV integration

**Commands**

CrewChat:
 - `/crewchat` - Shows version
 - `/crewchat reload` - Reloads plugin
 - `/crewchat help` - Shows CrewChat command help
 - `/crewchat info` - Shows CrewChat general info
 - `/crewchat info channel <channel>` - Shows all information about specified channel
 - Alias: `/cc`
 
 Chat:
 - `/chat info` - Shows channel list, active channel and subscribed channels
 - `/chat info channel <channel>` - Shows channel name and color
 - `/chat status <status>` - Sets player status message
 - `/chat subscribe <channel>` - Subscribes player to `<channel>`
 - `/chat unsubscribe <channel>` - Unsubscribes player from `<channel>`
 - `/chat switch <channel>` - Switches player's active channel
 - `/chat mute <player>` - Mutes `<player>` for 24 hours
 - `/chat deafen` - Deafens player from all chat messages
 - `/chat send <channel> [message]` - Sends a message to a specified channel without switching to it
 - `/chat mention <player>` - Mention a player in game or user on Discord
 - Alias: `/c`
 
 Me:
 - `/me <message>` - Sends third person chat message
 
 Msg:
 - `/msg <player> <message>` - Sends a private message to a player
 - Alias: `/tell`, `/whisper`, `/w`
 
 Reply:
 - `/reply <message>` - Replies to the last received private message.
 - Alias: `/r`
 
**Permissions**
 - `crewchat.use` - Allows player to use `/crewchat`
 - `crewchat.chat` - Allows player to use `/chat`
 - `crewchat.me` - Allows player to use `/me`
 - `crewchat.pm` - Allows player to use `/msg` and `/reply`
 - `crewchat.chat.subscribe.<channel>` - Allows player to subscribe to `<channel>`
 - `crewchat.chat.unsubscribe.<channel>` - Allows player to unsubscribe from `<channel>`
 - `crewchat.chat.switch.<channel>` - Allows player to switch `<channel>` as active channel
 - `crewchat.chat.mute` - Allows player to mute/unmute players
 - `crewchat.chat.deafen` - Allows player to deafen/undeafen themself
 - `crewchat.chat.send` - Allows player to use `/chat send`
 - `crewchat.chat.mention` - Allows player to use `/chata mention`
 - `crewchat.chat.info` -  Allows player to run `/chat info`
 - `crewchat.speak.<channel>` - Allows player to speak in `<channel>`
 - `crewchat.reload` - Allows player to use `/crewchat reload`
 
Tested working on Paper 1.8.0-1.16.5. Not tested but should support 1.17.1.