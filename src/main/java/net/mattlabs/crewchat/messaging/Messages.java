package net.mattlabs.crewchat.messaging;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public class Messages {

    private Messages() {

    }

    public static FancyMessage configReloaded() {
        // &7[&2CrewChat&7] &fConfiguration reloaded.
        return new FancyMessage("[")
                    .color(GRAY)
                .then("CrewChat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Configuration reloaded.")
                    .color(WHITE);
    }

    public static FancyMessage channelInfo(String name, String nick, String colorS, ChatColor color) {
        // &7[&2Chat&7] &fChannel &l%name%&r info:
        // &2- &fName: %name%
        // &2- &fNickname: %nick%
        // &2- &fChat Color: %color%
        // &2- &fAuto Subscribe: %autosus%
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Channel ")
                    .color(WHITE)
                .then(name)
                    .color(WHITE)
                    .style(BOLD)
                .then(" info:")
                    .color(WHITE)
                .then("\n - ")
                    .color(DARK_GREEN)
                .then("Name: " + name)
                    .color(WHITE)
                .then("\n - ")
                    .color(DARK_GREEN)
                .then("Nickname: " + nick)
                    .color(WHITE)
                .then("\n - ")
                    .color(DARK_GREEN)
                .then("Color: ")
                    .color(WHITE)
                .then(colorS)
                    .color(color);
    }

    public static FancyMessage channelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Channel ")
                    .color(WHITE)
                .then(name)
                    .color(WHITE)
                    .style(BOLD)
                .then(" doesn't exist!")
                    .color(WHITE);
    }

    public static FancyMessage noPermission() {
        // I'm sorry but you do not have permission to perform this
        //  command. Please contact the server administrators if you
        //  believe that this is in error.
        return new FancyMessage("I'm sorry but you do not have permission to perform this")
                    .color(RED)
                .then("\n command. Please contact the server administrators if you")
                    .color(RED)
                .then("\n believe that this is in error.")
                    .color(RED);
    }

    public static String chatMessage(String prefix, String playerName, String status, String message, String activeChannel, ChatColor chatColor) {
        // %prefix%%playerName%: %message%
        return new FancyMessage(prefix + playerName)
                    .tooltip("Status: " + status + "\nChannel: " + chatColorTranslator(chatColor) + activeChannel)
                    .suggest("/msg " + playerName + " ")
                .then(": ")
                    .color(GRAY)
                .then(message)
                    .color(chatColor)
                .toJSONString();
    }

    public static FancyMessage privateMessageSend(String senderPrefix, String recipientPrefix, String recipientName,
                                                  String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%me &7-> %recipientPrefix%%recipientName%&7] &r%message%
        return new FancyMessage("[")
                    .color(GRAY)
                .then(colorize(senderPrefix) + "Me")
                    .tooltip(senderStatus)
                .then(" -> ")
                    .color(GRAY)
                .then(colorize(recipientPrefix) + recipientName)
                    .tooltip(recipientStatus)
                    .suggest("/msg " + recipientName + " ")
                .then("] ")
                    .color(GRAY)
                .then(message)
                    .color(WHITE)
                    .tooltip(colorize("&b&lClick&r this message to reply."))
                    .suggest("/r ");
    }

    public static FancyMessage privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                                  String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return new FancyMessage("[")
                    .color(GRAY)
                .then(colorize(senderPrefix) + senderName)
                    .tooltip(senderStatus)
                    .suggest("/msg " + senderName + " ")
                .then(" -> ")
                    .color(GRAY)
                .then(colorize(recipientPrefix) + "Me")
                    .tooltip(recipientStatus)
                .then("] ")
                    .color(GRAY)
                .then(message)
                    .color(WHITE)
                    .tooltip(colorize("&b&lClick&r this message to reply."))
                    .suggest("/r ");
    }

    public static FancyMessage meMessage(String playerName, String message, ChatColor chatColor) {
        // * %playerName% %message% *
        return new FancyMessage("* ")
                    .color(chatColor)
                .then(playerName)
                    .color(chatColor)
                .then(" ")
                .then(message)
                    .color(chatColor)
                .then(" *")
                    .color(chatColor);
    }

    public static FancyMessage crewChatBaseCommand() {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("CrewChat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Version " + Bukkit.getPluginManager().getPlugin("CrewChat").getDescription().getVersion())
                    .color(WHITE)
                .then(". For help, click ")
                    .color(WHITE)
                .then("[Help]")
                    .color(BLUE)
                    .style(BOLD)
                    .tooltip("§b§lClick§r here for help.")
                    .suggest("/crewchat help");
    }

    public static FancyMessage crewChatHelpCommand() {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("CrewChat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Command Help:\n")
                    .color(WHITE)
                .then(" - Alias: /cc <args> - (Click to run) -\n")
                    .color(GRAY)
                .then("/crewchat")
                    .color(DARK_GREEN)
                    .tooltip("§b§lClick§r to run.")
                    .suggest("/crewchat")
                .then(" - ")
                    .color(GRAY)
                .then("Base CrewChat command.\n")
                    .color(WHITE)
                .then("/crewchat help")
                    .color(DARK_GREEN)
                    .tooltip("§b§lClick§r to run.")
                    .suggest("/crewchat help")
                .then(" - ")
                    .color(GRAY)
                .then("Shows this screen.\n")
                    .color(WHITE)
                .then("/crewchat reload")
                    .color(DARK_GREEN)
                    .tooltip("§b§lClick§r to run.")
                    .suggest("/crewchat reload")
                .then(" - ")
                    .color(GRAY)
                .then("Reload CrewChat configuration files.")
                    .color(WHITE);
    }

    public static FancyMessage channelListHeader() {
        return new FancyMessage("------------------------")
                    .color(GRAY)
                .then("[")
                    .color(DARK_GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("]")
                    .color(DARK_GRAY)
                .then("------------------------\n")
                    .color(GRAY)
                .then("Channel List: (Click for more info)")
                    .color(GRAY);
    }

    public static FancyMessage channelListEntry(String name, ChatColor chatColor) {
        return new FancyMessage(" - ")
                    .color(DARK_GREEN)
                .then(name)
                    .color(chatColor)
                    .style(BOLD)
                    .tooltip("§b§lClick§r for more info.")
                    .suggest("/chat info channel " + name);
    }

    public static FancyMessage channelListActive(String name, ChatColor chatColor) {
        return new FancyMessage("Your active channel is: ")
                    .color(GRAY)
                .then(name)
                    .color(chatColor)
                    .style(BOLD)
                .then(".")
                    .color(GRAY);
    }

    public static FancyMessage channelListSubscribedHeader() {
        return new FancyMessage("Your subscribed channels are:")
                    .color(GRAY);
    }

    public static FancyMessage statusSet(String status) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Your status has been set to: \"")
                    .color(WHITE)
                .then(colorize(status))
                    .color(WHITE)
                .then("\".")
                    .color(WHITE);
    }

    public static FancyMessage alreadySubscribed(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You are already subscribed to ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage nowSubscribed(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You are now subscribed to ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage cantSubscribe(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You can't subscribe to ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage cantUnsubscribe(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You can't unsubscribe from ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage cantUnsubscribeActive(String channelName) {
        return new FancyMessage("[")
                .color(GRAY)
                .then("Chat")
                .color(DARK_GREEN)
                .then("] ")
                .color(GRAY)
                .then("You can't unsubscribe from ")
                .color(WHITE)
                .then(channelName)
                .color(WHITE)
                .style(BOLD)
                .then(", it is your active channel!")
                .color(WHITE);
    }

    public static FancyMessage nowUnsubscribed(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You are no longer subscribed to ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage notSubscribed(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You aren't subscribed to ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage newActiveChannel(String channelName, ChatColor chatColor) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Your active channel is now ")
                    .color(WHITE)
                .then(channelName)
                    .color(chatColor)
                    .style(BOLD)
                .then("!")
                    .color(WHITE);
    }

    public static FancyMessage cantSetActive(String channelName) {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You can't set ")
                    .color(WHITE)
                .then(channelName)
                    .color(WHITE)
                    .style(BOLD)
                .then(" as your active channel!")
                    .color(WHITE);
    }

    public static FancyMessage cantMessageSelf() {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You can't send a message to yourself!")
                    .color(WHITE);
    }

    public static FancyMessage playerNoExist() {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("Player doesn't exist!")
                    .color(WHITE);
    }

    public static FancyMessage noPMReceived() {
        return new FancyMessage("[")
                    .color(GRAY)
                .then("Chat")
                    .color(DARK_GREEN)
                .then("] ")
                    .color(GRAY)
                .then("You haven't received any messages!")
                    .color(WHITE);
    }

    public static String colorize(String s){
        if(s == null) return null;
        return s.replaceAll("&([0-9a-z])", "\u00A7$1");
    }

    public static String chatColorTranslator(ChatColor color) {
        switch (color) {
            case DARK_RED:
                return colorize("&4");
            case RED:
                return colorize("&c");
            case GOLD:
                return colorize("&6");
            case YELLOW:
                return colorize("&e");
            case DARK_GREEN:
                return colorize("&2");
            case GREEN:
                return colorize("&a");
            case AQUA:
                return colorize("&b");
            case DARK_AQUA:
                return colorize("&3");
            case DARK_BLUE:
                return colorize("&1");
            case BLUE:
                return colorize("&9");
            case LIGHT_PURPLE:
                return colorize("&d");
            case DARK_PURPLE:
                return colorize("&5");
            case WHITE:
                return colorize("&f");
            case GRAY:
                return colorize("&7");
            case DARK_GRAY:
                return colorize("&8");
            case BLACK:
                return colorize("&0");
            case BOLD:
                return colorize("&l");
            case STRIKETHROUGH:
                return colorize("&m");
            case UNDERLINE:
                return colorize("&n");
            case ITALIC:
                return colorize("&o");
            case MAGIC:
                return colorize("&k");
            case RESET:
                return colorize("&r");
            default:
                return "ChatColor Error";
        }
    }
}
