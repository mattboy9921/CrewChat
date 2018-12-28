package net.mattlabs.crewchat.messaging;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;

import static net.md_5.bungee.api.ChatColor.*;


public class Messages {

    private Messages() {

    }

    public static BaseComponent[] configReloaded() {
        // &7[&2CrewChat&7] &fConfiguration reloaded.
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("CrewChat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Configuration reloaded.")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] channelInfo(String name, String nick, String colorS, ChatColor color) {
        // &7[&2Chat&7] &fChannel &l%name%&r info:
        // &2- &fName: %name%
        // &2- &fNickname: %nick%
        // &2- &fChat Color: %color%
        // &2- &fAuto Subscribe: %autosus%
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Channel ")
                    .color(WHITE)
                .append(name)
                    .color(WHITE)
                    .bold(true)
                .append(" info:")
                    .reset()
                    .color(WHITE)
                .append("\n - ")
                    .color(DARK_GREEN)
                .append("Name: " + name)
                    .color(WHITE)
                .append("\n - ")
                    .color(DARK_GREEN)
                .append("Nickname: " + nick)
                    .color(WHITE)
                .append("\n - ")
                    .color(DARK_GREEN)
                .append("Color: ")
                    .color(WHITE)
                .append(colorS)
                    .color(color)
                .create();
    }

    public static BaseComponent[] channelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Channel ")
                    .color(WHITE)
                .append(name)
                    .color(WHITE)
                    .bold(true)
                .append(" doesn't exist!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] noPermission() {
        // I'm sorry but you do not have permission to perform this
        //  command. Please contact the server administrators if you
        //  believe that this is in error.
        return new ComponentBuilder("I'm sorry but you do not have permission to perform this")
                    .color(RED)
                .append("\n command. Please contact the server administrators if you")
                    .color(RED)
                .append("\n believe that this is in error.")
                    .color(RED)
                .create();
    }

    public static BaseComponent[] chatMessage(String prefix, String playerName, String status, String message, String activeChannel, ChatColor chatColor) {
        // %prefix%%playerName%: %message%
        return new ComponentBuilder(prefix + playerName)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Status: " + status + "\nChannel: " + chatColorTranslator(chatColor) + activeChannel)
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + playerName + " "))
                .append(": ")
                    .color(GRAY)
                .append(message)
                    .color(chatColor)
                .create();
    }

    public static BaseComponent[] privateMessageSend(String senderPrefix, String recipientPrefix, String recipientName,
                                                  String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%me &7-> %recipientPrefix%%recipientName%&7] &r%message%
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append(colorize(senderPrefix) + "Me")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(senderStatus).create()))
                .append(" -> ")
                    .color(GRAY)
                .append(colorize(recipientPrefix) + recipientName)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(recipientStatus).create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + recipientName + " "))
                .append("] ")
                    .color(GRAY)
                .append(message)
                    .color(WHITE)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" this message to reply.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/r "))
                .create();
    }

    public static BaseComponent[] privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                                  String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append(colorize(senderPrefix) + senderName)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(senderStatus).create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + senderName + " "))
                .append(" -> ")
                    .color(GRAY)
                .append(colorize(recipientPrefix) + "Me")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(recipientStatus).create()))
                .append("] ")
                    .color(GRAY)
                .append(message)
                    .color(WHITE)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" this message to reply.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/r "))
                .create();
    }

    public static BaseComponent[] meMessage(String playerName, String message, ChatColor chatColor) {
        // * %playerName% %message% *
        return new ComponentBuilder("* ")
                    .color(chatColor)
                .append(playerName)
                    .color(chatColor)
                .append(" ")
                .append(message)
                    .color(chatColor)
                .append(" *")
                    .color(chatColor)
                .create();
    }

    public static BaseComponent[] crewChatBaseCommand() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("CrewChat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Version " + Bukkit.getPluginManager().getPlugin("CrewChat").getDescription().getVersion())
                    .color(WHITE)
                .append(". For help, click ")
                    .color(WHITE)
                .append("[Help]")
                    .color(BLUE)
                    .bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" here for help.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crewchat help"))
                .create();
    }

    public static BaseComponent[] crewChatHelpCommand() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("CrewChat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Command Help:\n")
                    .color(WHITE)
                .append(" - Alias: /cc <args> - (Click to run) -\n")
                    .color(GRAY)
                .append("/crewchat")
                    .color(DARK_GREEN)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" to run.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crewchat"))
                .append(" - ")
                    .color(GRAY)
                .append("Base CrewChat command.\n")
                    .color(WHITE)
                .append("/crewchat help")
                    .color(DARK_GREEN)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" to run.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crewchat help"))
                .append(" - ")
                    .color(GRAY)
                .append("Shows this screen.\n")
                    .color(WHITE)
                .append("/crewchat reload")
                    .color(DARK_GREEN)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" to run")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crewchat reload"))
                .append(" - ")
                    .color(GRAY)
                .append("Reload CrewChat configuration files.")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] channelListHeader() {
        return new ComponentBuilder("------------------------")
                    .color(GRAY)
                .append("[")
                    .color(DARK_GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("]")
                    .color(DARK_GRAY)
                .append("------------------------\n")
                    .color(GRAY)
                .append("Channel List: (Click for more info)")
                    .color(GRAY)
                .create();
    }

    public static BaseComponent[] channelListEntry(String name, ChatColor chatColor) {
        return new ComponentBuilder(" - ")
                    .color(DARK_GREEN)
                .append(name)
                    .color(chatColor)
                    .bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click")
                                        .color(AQUA)
                                        .bold(true)
                                    .append(" for more info.")
                                        .reset()
                                    .create()))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chat info channel " + name))
                .create();
    }

    public static BaseComponent[] channelListActive(String name, ChatColor chatColor) {
        return new ComponentBuilder("Your active channel is: ")
                    .color(GRAY)
                .append(name)
                    .color(chatColor)
                    .bold(true)
                .append(".")
                    .reset()
                    .color(GRAY)
                .create();
    }

    public static BaseComponent[] channelListSubscribedHeader() {
        return new ComponentBuilder("Your subscribed channels are:")
                    .color(GRAY)
                .create();
    }

    public static BaseComponent[] statusSet(String status) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Your status has been set to: \"")
                    .color(WHITE)
                .append(colorize(status))
                    .color(WHITE)
                .append("\".")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] alreadySubscribed(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You are already subscribed to ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] nowSubscribed(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You are now subscribed to ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] cantSubscribe(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You can't subscribe to ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] cantUnsubscribe(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You can't unsubscribe from ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] cantUnsubscribeActive(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You can't unsubscribe from ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append(", it is your active channel!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] nowUnsubscribed(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You are no longer subscribed to ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] notSubscribed(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You aren't subscribed to ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] newActiveChannel(String channelName, ChatColor chatColor) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Your active channel is now ")
                    .color(WHITE)
                .append(channelName)
                    .color(chatColor)
                    .bold(true)
                .append("!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] cantSetActive(String channelName) {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You can't set ")
                    .color(WHITE)
                .append(channelName)
                    .color(WHITE)
                    .bold(true)
                .append(" as your active channel!")
                    .reset()
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] cantMessageSelf() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You can't send a message to yourself!")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] playerNoExist() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("Player doesn't exist!")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] noPMReceived() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("] ")
                    .color(GRAY)
                .append("You haven't received any messages!")
                    .color(WHITE)
                .create();
    }

    public static BaseComponent[] badConfig() {
        return new ComponentBuilder("[")
                    .color(GRAY)
                .append("Chat")
                    .color(DARK_GREEN)
                .append("]")
                    .color(GRAY)
                .append(" Plugin not configured correctly.")
                    .reset()
                .append("\n - ")
                    .color(DARK_GREEN)
                .append(" Permissions/configuration error.")
                    .reset()
                .create();
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
