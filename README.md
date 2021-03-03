CrewChat is a chat plugin developed for the Crew Craft Server.

[![Build Status](https://travis-ci.org/mattboy9921/CrewChat.svg?branch=master)](https://travis-ci.org/mattboy9921/CrewChat)

**Features**
 - General chat
 - Chat channels
 - Permissions
 - Private messages
 - RAW messages with hover and click actions
 - Support for clickable links in chat
 - Player info/status messages
 - Ping players by name
 - DiscordSRV integration

**Commands**

CrewChat:
 - `/crewchat` - Shows version
 - `/crewchat reload` - Reloads plugin
 - `/crewchat help` - Shows CrewChat command help
 - Alias: `/cc`
 
 Chat:
 - `/chat info` - Shows channel list, active channel and subscribed channels
 - `/chat info channel <channel>` - Shows channel name and color
 - `/chat status <status>` - Sets player status message
 - `/chat subscribe <channel>` - Subscribes player to `<channel>`
 - `/chat unsubscribe <channel>` - Unsubscribes player from `<channel>`
 - `/chat switch <channel>` - Switches player's active channel
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
 - `crewchat.chat.info` -  Allows player to run `/chat info`
 - `crewchat.speak.<channel>` - Allows player to speak in `<channel>`
 - `crewchat.reload` - Allows player to use `/crewchat reload`
 
Tested working on Paper 1.16.