# CrewChat
CrewChat is a general purpose chat plugin originally developed for the [Crew Craft Server](https://crewcraftserver.net). 
It is now avaiable here as a standalone plugin designed to take over all aspects of chat from general messaging to private messages and even Discord bridging.
CrewChat extends basic chat functionality with the introduction of channels to organize different chat streams.
It also adds fun features like muting, deafening, status messages, mentions and party chat.

![Build Status](https://github.com/mattboy9921/CrewChat/actions/workflows/maven.yml/badge.svg)
[![Servers Using CrewChat](https://img.shields.io/bstats/servers/5799?style=flat&label=Servers&logo=bookmeter&logoColor=94A0A5&labelColor=384142&color=00695C)](https://bstats.org/plugin/bukkit/CrewChat/5799)
[![Players Using CrewChat](https://img.shields.io/bstats/players/5799?style=flat&label=Players&logo=bookmeter&logoColor=94A0A5&labelColor=384142&color=00695C)](https://bstats.org/plugin/bukkit/CrewChat/5799)
![CrewChat Downloads](https://img.shields.io/github/downloads/mattboy9921/crewchat/total?label=Downloads&logo=docusign&logoColor=94A0A5&labelColor=384142)
[![CrewChat Latest Release](https://img.shields.io/github/v/release/mattboy9921/crewchat?label=Release&logo=dropbox&logoColor=94A0A5&labelColor=384142)](https://github.com/mattboy9921/CrewChat/releases/latest)
![CrewChat Tested Versions](https://img.shields.io/badge/Tested%20Versions-1.8.0--1.17.1-success?&logo=verizon&logoColor=94A0A5&labelColor=384142)
![CrewChat Made with Love](https://img.shields.io/badge/Made-with%20Love-red?&logo=undertale&logoColor=94A0A5&labelColor=384142)

**Features**
 - General chat
 - Chat channels
 - Party chat
 - Permissions
 - Private messages
 - Broadcast messages
 - Rich messages with hover and click actions
 - Markdown support
 - Support for clickable links in chat with SEO
 - Player info/status messages
 - Ping players by name
 - Mute/deafen functions
 - DiscordSRV integration

**Requirements**
- [Vault](https://github.com/mattboy9921/CrewChat/releases/latest) ([SpigotMC](https://www.spigotmc.org/resources/vault.34315/))
- [DiscordSRV](https://www.spigotmc.org/resources/vault.34315/) ([SpigotMC](https://www.spigotmc.org/resources/vault.34315/)) (Not required but strongly suggested)

**Downloads**
- [GitHub Release](https://github.com/mattboy9921/CrewChat/releases/latest)

**Commands**

CrewChat:
 - `/crewchat` - Shows version
 - `/crewchat reload` - Reloads plugin
 - `/crewchat help` - Shows CrewChat command help
 - `/crewchat info` - Shows CrewChat general info
 - `/crewchat info channel [channel]` - Lists channels or shows all information about specified channel
 - `/crewchat info player <player>` - Shows all information about specified player
 - Alias: `/cc`
 
Chat:
 - `/chat` - Show Chat welcome message
 - `/chat help` - Shows chat command help
 - `/chat info` - Shows channel list, active channel and subscribed channels
 - `/chat info channel <channel>` - Shows channel name and color
 - `/chat status [status]` - Sets or displays player status message
 - `/chat subscribe <channel>` - Subscribes player to `<channel>`
 - `/chat unsubscribe <channel>` - Unsubscribes player from `<channel>`
 - `/chat switch <channel>` - Switches player's active channel
 - `/chat mute <player>` - Mutes `<player>` for 24 hours
 - `/chat unmute <player>` - Unmutes `<player>`
 - `/chat deafen` - Deafens player from all chat messages
 - `/chat send <channel> <message>` - Sends a message to a specified channel without switching to it
 - `/chat mention <player>` - Mention a player in game or user on Discord
 - Alias: `/c`
 
Me:
 - `/me <message>` - Sends third person chat message

Broadcast:
 - `/broadcast <message>` - Sends a message to every player online
 - Alias: `/bc`, `/yell`
 
Msg:
 - `/msg <player> <message>` - Sends a private message to a player
 - Alias: `/tell`, `/whisper`, `/w`, `/pm`
 
Reply:
 - `/reply <message>` - Replies to the last received private message.
 - Alias: `/r`
 
Party:
 - `/party create <party> [#hex]` Creates a party with the specified name. Without optional hex color code, displays a color picker.
 - `/party join <party>` Joins player to `<party>`
 - `/party leave <party>` Leaves player from `<party>`
 - `/party list <party>` Lists all players currently in `<party>`
 
**Permissions**

General:
- `crewchat.chat` - Allows player to use `/chat`
- `crewchat.me` - Allows player to use `/me`
- `crewchat.pm` - Allows player to use `/msg` and `/reply`

Chat:
- `crewchat.chat.mute` - Allows player to mute/unmute players
- `crewchat.chat.deafen` - Allows player to deafen/undeafen themself
- `crewchat.chat.send` - Allows player to use `/chat send`
- `crewchat.chat.mention` - Allows player to use `/chata mention`
- `crewchat.chat.info` -  Allows player to run `/chat info`
- `crewchat.chat.color` - Allows player to use color codes/MiniMessage tags in chat

Channels:
- `crewchat.chat.default.active.<channel>` - Makes specified `<channel>` the default active channel on first join
- `crewchat.chat.subscribe.<channel>` - Allows player to subscribe to `<channel>`
- `crewchat.chat.unsubscribe.<channel>` - Allows player to unsubscribe from `<channel>`
- `crewchat.chat.switch.<channel>` - Allows player to switch `<channel>` as active channel
- `crewchat.speak.<channel>` - Allows player to speak in `<channel>`

Parties:
- `crewchat.party` - Allows players to join and leave parties
- `crewchat.party.create` - Allows players to create parties

Administrative:
- `crewchat.use` - Allows player to use `/crewchat`
- `crewchat.broadcast` - Allows player to use `/broadcast`
- `crewchat.info` - Allows player to use `/crewchat info`
- `crewchat.reload` - Allows player to use `/crewchat reload`
 
Tested working on Paper 1.8.0-1.17.1.