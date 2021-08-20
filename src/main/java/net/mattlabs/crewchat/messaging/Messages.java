package net.mattlabs.crewchat.messaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MessageUtil;
import org.apache.commons.lang.WordUtils;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class Messages {

    public Messages() {

    }

    // Header fields
    @Setting(value = "_schema-version")
    @Comment("#######################################################################################################\n" +
            "    ________  ________  _______   ___       __   ________  ___  ___  ________  _________   \n" +
            "   |\\   ____\\|\\   __  \\|\\  ___ \\ |\\  \\     |\\  \\|\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\ \n" +
            "   \\ \\  \\___|\\ \\  \\|\\  \\ \\   __/|\\ \\  \\    \\ \\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_| \n" +
            "    \\ \\  \\    \\ \\   _  _\\ \\  \\_|/_\\ \\  \\  __\\ \\  \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\  \n" +
            "     \\ \\  \\____\\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\|\\__\\_\\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\ \n" +
            "      \\ \\_______\\ \\__\\\\ _\\\\ \\_______\\ \\____________\\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\\n" +
            "       \\|_______|\\|__|\\|__|\\|_______|\\|____________|\\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|\n\n" +

            "CrewChat Messages Configuration\n" +
            "By Mattboy9921\n" +
            "https://github.com/mattboy9921/CrewChat\n\n" +

            "This configuration contains every string of text found in this plugin.\n\n" +

            "For values that contain variables, they are shown as \"<some_value>\"\n" +
            "and the possible tags are shown in the comment above the line.\n" +
            "It is not necessary to include every variable, but certain strings won't make sense otherwise.\n\n" +

            "Colors and text style can be specified using XML-like tags, for example: \"<white>\".\n" +
            "Standard Minecraft colors/styles are available. Hex colors can be specified with \"<color:#XXXXXX>\".\n" +
            "Please note, some values cannot use color codes (\"<white>\") as denoted in the comment above the value.\n\n" +
            
            "#######################################################################################################\n\n" +

            "Config version. Do not change this!")
    private int schemaVersion = 0;

    // *** Chat Command ***

    // ** Base **

    // Welcome to chat
    @Comment("\nAppears in the chat base command.")
    private String welcomeToChat = "<white>Welcome to chat!";

    public Component chatBaseCommand() {
        return chatHeader
                .append(MiniMessage.get().parse(welcomeToChat))
                .append(Component.text("[" + help + "]")
                    .decoration(BOLD, true)
                    .color(BLUE)
                    .hoverEvent(HoverEvent.showText(
                        Component.text("Click")
                            .color(DARK_GREEN)
                            .decoration(BOLD, true)
                            .append(Component.text(" here for help.")
                                .color(WHITE)
                                .decoration(BOLD, false))))
                    .clickEvent(ClickEvent.runCommand("/chat help")));
    }

    // ** Deafen **

    // Player Deafened
    @Comment("\nAppears when a player deafens themself.")
    private String playerDeafened = "<white>You have been deafened. You will not receive any chat messages.";

    public Component playerDeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerDeafened));
    }

    // Player Undeafened
    @Comment("\nAppears when a player undeafens themself.")
    private String playerUndeafened = "<white>You are no longer deafened. You will receive all chat messages.";

    public Component playerUndeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerUndeafened));
    }

    // Player Is Deafened
    @Comment("\nAppears when a player tries to send a chat message while deafened.")
    private String playerIsDeafened = "<white>You are deafened and cannot see chat messages!";

    public Component playerIsDeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerIsDeafened));
    }

    // ** General **

    // Channel No Exist
    @Comment("\nAppears when a command contains a channel that doesn't exist.\n" +
            "Possible tags: <channel_name>")
    private String channelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component channelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return chatHeader.append(MiniMessage.get().parse(channelNoExist, "channel_name", name));
    }

    // ** Help **
    public Component chatHelpCommand() {
        return chatHeader
                .append(Component.text("Command Help:")
                    .color(WHITE))
                .append(Component.text(" - Alias: /c <args> - (Click to run) -")
                    .color(GRAY))
                .append(Component.text("/chat")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Base Chat command.")
                    .color(WHITE))
                .append(Component.text("/chat help")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat help")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Shows this screen.")
                    .color(WHITE))
                .append(Component.text("/chat info")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat info")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Lists all channels, active channel and subscribed channels.")
                    .color(WHITE))
                .append(Component.text("/chat info channel <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat info channel ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Lists info about specified channel.")
                    .color(WHITE))
                .append(Component.text("/chat status <status>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat status ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Sets player's status.")
                    .color(WHITE))
                .append(Component.text("/chat subscribe <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat subscribe ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Subscribes player to channel.")
                    .color(WHITE))
                .append(Component.text("/chat unsubscribe <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat unsubscribe ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Unsubscribes player from channel.")
                    .color(WHITE))
                .append(Component.text("/chat switch <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat switch ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Switches active channel.")
                    .color(WHITE));
    }

    // ** Info **

    // Channel List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListHeader = "Channel List: (Click for more info)";

    // Channel List Active
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListActive = "Your active channel is: <channel_name>.";

    // Channel List Subscribe Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListSubscribedHeader = "Your subscribed channels are:";

    // Muted List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String mutedListHeader = "Your muted players are:";

    // Click to unmute
    @Comment("\nAppears in the chat info command.")
    private String clickToUnmute = "<bold><aqua>Click<reset> to unmute.";

    // Time remaining
    @Comment("\nAppears in the chat info command.\n" +
            "Possibel tags: <time_remaining>")
    private String timeRemaining = "Time remaining: <time_remaining>";

    public Component channelListHeader() {
        return Component.text("------------------------")
                .color(GRAY)
                .append(Component.text("[")
                        .color(DARK_GRAY))
                .append(Component.text(chat)
                        .color(DARK_GREEN))
                .append(Component.text("]")
                        .color(DARK_GRAY))
                .append(Component.text("------------------------")
                        .color(GRAY))
                .append(Component.text(channelListHeader)
                        .color(GRAY));
    }

    public Component channelListEntry(String name, TextColor textColor) {
        return Component.text(" - ")
                .color(DARK_GREEN)
                .append(Component.text(name)
                        .color(textColor)
                        .decoration(BOLD, true)
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Click")
                                        .color(DARK_GREEN)
                                        .decoration(BOLD, true)
                                        .append(Component.text(" for more info.")
                                                .color(WHITE))))
                        .clickEvent(ClickEvent.runCommand("/chat info channel " + name)));
    }

    public Component channelListActive(String name, TextColor textColor) {
        return MiniMessage.get().parse("<gray>" + channelListActive,
                "channel_name", "<" + textColor.toString() + "><bold>" + name + "<reset><gray>");
    }

    public Component channelListSubscribedHeader() {
        return Component.text(channelListSubscribedHeader)
                .color(GRAY);
    }

    public Component mutedListHeader() {
        return Component.text(mutedListHeader)
                .color(GRAY);
    }

    public Component mutedListEntry(String player, String timeRemaining) {
        return hyphenHeader
                .append(Component.text(player)
                        .color(WHITE)
                        .hoverEvent(HoverEvent.showText(MiniMessage.get().parse(this.timeRemaining + "\n" + clickToUnmute,
                                "time_remaining", timeRemaining)))
                        .clickEvent(ClickEvent.runCommand("/chat unmute " + player)));
    }

    // ** Info Channel **

    // Header
    private transient String channelInfoHeader = "<white>Channel <bold><channel_name></bold> info:";

    // Name
    private transient String channelInfoName = "<white>Name: <channel_name>";

    // Description
    private transient String channelInfoDescription = "<white>Description: <channel_description>";

    // Color
    private transient String channelInfoColor = "<white>Color: <channel_color>";

    public Component channelInfo(String name, String description, TextColor color) {
        // &7[&2Chat&7] &fChannel &l%name%&r info:
        // &2- &fName: %name%
        // &2- &fChat Color: %color%
        // &2- &fDescription: %desc#

        // Get closest color to hex code, remove underscores, capitalize first letter of each word
        String closestColor = nearestTo(color).toString();
        closestColor = closestColor.replaceAll("_", " ");
        closestColor = WordUtils.capitalize(closestColor);

        return chatHeader
                .append(MiniMessage.get().parse(channelInfoHeader + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoName + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoDescription + "\n", "channel_description", description))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoColor, "channel_color", "<color:" + color.toString() + ">" + closestColor + " (" + color.asHexString() + ")"));
    }

    // ** Mute/Unmute **

    // Chat Can't Mute Self
    @Comment("\nAppears when a player tries to mute themself.")
    private String cantMuteSelf = "<white>You can't mute yourself!";

    public Component cantMuteSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantMuteSelf));
    }

    // Chat Can't Unmute Self
    @Comment("\nAppears when a player tries to unmute themself.")
    private String cantUnmuteSelf = "<white>You can't unmute yourself!";

    public Component cantUnmuteSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantUnmuteSelf));
    }

    // Chat Player Already Muted
    @Comment("\nAppears when a player tries to mute someone they've already muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyMuted = "<white><player_prefix><player_name><white> is already muted!";

    public Component playerAlreadyMuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.get().parse(playerAlreadyMuted,
                "player_prefix", serialize(playerPrefix),
                "player_name", playerName));
    }

    // Chat Player Already Unmuted
    @Comment("\nAppears when a player tries to unmute someone who isn't muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyUnmuted = "<white><player_prefix><player_name><white> is not muted!";

    public Component playerAlreadyUnmuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.get().parse(playerAlreadyUnmuted,
                "player_prefix", serialize(playerPrefix),
                "player_name", playerName));
    }

    // Chat Player Muted
    @Comment("\nAppears when a player mutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerMuted = "<white><player_prefix><player_name><white> has been muted.";

    public Component playerMuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.get().parse(playerMuted,
                "player_prefix", serialize(playerPrefix),
                "player_name", player));
    }

    // Chat Player Unmuted
    @Comment("\nAppears when a player unmutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerUnmuted = "<white><player_prefix><player_name><white> has been unmuted.";

    public Component playerUnmuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.get().parse(playerUnmuted,
                "player_prefix", serialize(playerPrefix),
                "player_name", player));
    }

    // ** Status **

    @Comment("\nAppears when a player sets their status.\n" +
            "Possible tags: <status>")
    private String statusSet = "<white>Your status has been set to: \"<status>\".";

    public Component statusSet(String status) throws ParsingException {
        //noinspection unchecked
        return chatHeader.append(MiniMessage.get().parse(MessageUtil.sanitizeMessageColor(statusSet), "status", serialize(status) + "<reset>"));
    }

    @Comment("\nAppears when a player checks their status.\n" +
            "Possible tags: <status>")
    private String statusIs = "<white>Your status is: \"<status>\".";

    public Component statusIs(String status) throws ParsingException {
        //noinspection unchecked
        return chatHeader.append(MiniMessage.get().parse(MessageUtil.sanitizeMessageColor(statusIs), "status", serialize(status) + "<reset>"));
    }

    @Comment("\nAppears if a player's status contains a syntax error.")
    private String statusSyntaxError = "<white>Your status could not be set. Please check the syntax.";

    public Component statusSyntaxError() {
        return chatHeader.append(MiniMessage.get().parse(statusSyntaxError));
    }

    // ** Subscribe/Unsubscribe **

    // Already Subscribed
    @Comment("\nAppears when a player tries to subscribe to a channel they already subscribe to.\n" +
            "Possibe tags: <channel_name>")
    private String alreadySubscribed = "<white>You are already subscribed to <bold><channel_name></bold>.";

    public Component alreadySubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(alreadySubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Subscribe
    @Comment("\nAppears when a player can't subscribe to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSubscribe = "<white>You can't subscribe to <bold><channel_name></bold>!";

    public Component cantSubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantSubscribe, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Unsubscribe
    @Comment("\nAppears when a player can't unsubscribe from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribe = "<white>You can't unsubscribe from <bold><channel_name></bold>!";

    public Component cantUnsubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantUnsubscribe, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Unsubscribe Active
    @Comment("\nAppears when a player can't unsubscribe from a channel because it is their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribeActive = "<white>You can't unsubscribe from <bold><channel_name></bold>, it is your active channel!";

    public Component cantUnsubscribeActive(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantUnsubscribeActive, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Not Subscribed
    @Comment("\nAppears when a player unsubscribes from a channel they are not subscribed to.\n" +
            "Possibe tags: <channel_name>")
    private String notSubscribed = "<white>You aren't subscribed to <bold><channel_name></bold>.";

    public Component notSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(notSubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Now Subscribed
    @Comment("\nAppears when a player subscribes to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowSubscribed = "<white>You are now subscribed to <bold><channel_name></bold>.";

    public Component nowSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(nowSubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Now Unsubscribed
    @Comment("\nAppears when a player unsubscribes from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowUnsubscribed = "<white>You are no longer subscribed to <bold><channel_name></bold>.";

    public Component nowUnsubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(nowUnsubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // ** Switch **

    // New Active Channel
    @Comment("\nAppears when a player changes their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String newActiveChannel = "<white>Your active channel is now <bold><channel_name></bold>.";

    public Component newActiveChannel(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(newActiveChannel, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Set Active
    @Comment("\nAppears when a player can't change their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSetActive = "<white>You can't set <bold><channel_name></bold> as your active channel!";

    public Component cantSetActive(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantSetActive, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // *** CrewChat ***

    // [CrewChat]
    private transient Component crewChatHeader =
            Component.text("[")
                        .color(GRAY)
                    .append(Component.text("CrewChat")
                        .color(DARK_GREEN))
                    .append(Component.text("] ")
                        .color(GRAY))
                    .append(Component.text()
                        .resetStyle());

    // ** Base **

    public Component crewChatBaseCommand() {
        return crewChatHeader
                .append(Component.text("Version " +
                    CrewChat.getInstance().getDescription().getVersion() +
                    ". For help, click ")
                    .color(WHITE))
                .append(Component.text("[Help]")
                    .color(BLUE)
                    .decoration(BOLD, true)
                    .hoverEvent(HoverEvent.showText(
                        Component.text("Click")
                            .color(DARK_GREEN)
                            .decoration(BOLD, true)
                            .append(Component.text(" here for help.")
                                .color(WHITE)
                                .decoration(BOLD, false))))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")));
    }

    // ** Info **

    // Info Header
    private transient String crewChatInfoHeader = "<white>Version <bold><version></bold> Info:";

    public Component crewChatInfoHeader() {
        return crewChatHeader.append(MiniMessage.get().parse(crewChatInfoHeader,
                "version", CrewChat.getInstance().getDescription().getVersion()));
    }

    // Info Channels
    private transient String crewChatChannelsLoaded = "<white><bold><channels></bold> channel(s) loaded.";

    public Component crewChatChannelsLoaded(String channels) {
        return hyphenHeader.append(MiniMessage.get().parse(crewChatChannelsLoaded, "channels", channels));
    }

    // Info Players
    private transient String crewChatPlayersLoaded = "<white><bold><players></bold> player(s) loaded.";

    public Component crewChatPlayersLoaded(String players) {
        return hyphenHeader.append(MiniMessage.get().parse(crewChatPlayersLoaded, "players", players));
    }

    // Info Online Players
    private transient String crewChatOnlinePlayersLoaded = "<white><bold><players></bold> online player(s) loaded.";

    public Component crewChatOnlinePlayersLoaded(String players) {
        return hyphenHeader.append(MiniMessage.get().parse(crewChatOnlinePlayersLoaded, "players", players));
    }

    // Info Discord Integration
    private transient String crewChatDiscordIntegration = "<white>Discord integration enabled: <bold><enabled></bold>.";

    public Component crewChatDiscordIntegration(Boolean enabled) {
        return hyphenHeader.append(MiniMessage.get().parse(crewChatDiscordIntegration, "enabled", enabled ? "True" : "False"));
    }

    // Info
    public Component crewChatInfo(int channelsLoaded, int playersLoaded, int onlinePlayersLoaded, boolean discordIntegration) {
        return crewChatHeader.append(MiniMessage.get().parse(crewChatInfoHeader,
                "version", CrewChat.getInstance().getDescription().getVersion())
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelsLoaded + "\n", "channels", String.valueOf(channelsLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatPlayersLoaded + "\n", "players", String.valueOf(playersLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatOnlinePlayersLoaded + "\n", "players", String.valueOf(onlinePlayersLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatDiscordIntegration, "enabled", discordIntegration ? "True" : "False")));
    }

    // ** Info Channel **
    // Base command
    // Header
    public Component crewChatChannelListHeader() {
        return Component.text("----------------------")
                .color(GRAY)
                .append(Component.text("[")
                        .color(DARK_GRAY))
                .append(Component.text("CrewChat")
                        .color(DARK_GREEN))
                .append(Component.text("]")
                        .color(DARK_GRAY))
                .append(Component.text("----------------------")
                        .color(GRAY))
                .append(Component.text(channelListHeader)
                        .color(GRAY));
    }

    // Channel Entry
    public Component crewChatChannelListEntry(String name, TextColor textColor) {
        return Component.text(" - ")
                .color(DARK_GREEN)
                .append(Component.text(name)
                        .color(textColor)
                        .decoration(BOLD, true)
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Click")
                                        .color(DARK_GREEN)
                                        .decoration(BOLD, true)
                                        .append(Component.text(" for more info.")
                                                .color(WHITE))))
                        .clickEvent(ClickEvent.runCommand("/crewchat info channel " + name)));
    }

    // Subscribers
    private transient String crewChatChannelInfoSubscribers = "<white><bold><subscribers></bold> subscriber(s).";

    // Auto-Subscribe
    private transient String crewChatChannelInfoAutoSubscribe = "<white>Auto-Subscribe enabled: <bold><auto_subscribe>";

    // Exclude From Discord
    private transient String crewChatChannelInfoExcludeFromDiscord = "<white>Exclude from Discord enabled: <bold><exclude_from_discord>";

    // Show Channel Name Discord
    private transient String crewChatChannelInfoShowChannelNameDiscord = "<white>Show Channel Name on Discord enabled: <bold><show_channel_name_discord>";

    // Channel Info
    public Component crewChatChannelInfo(String name, String description, TextColor color, int subscribers, boolean isAutoSubscribe, boolean isExcludeFromDiscord, boolean isShowChannelNameDiscord) {

        // Get closest color to hex code, remove underscores, capitalize first letter of each word
        String closestColor = nearestTo(color).toString();
        closestColor = closestColor.replaceAll("_", " ");
        closestColor = WordUtils.capitalize(closestColor);

        return crewChatHeader
                .append(MiniMessage.get().parse(channelInfoHeader + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoName + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoDescription + "\n", "channel_description", description))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoColor + "\n", "channel_color", "<color:" + color + ">" + closestColor + " (" + color.asHexString() + ")"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelInfoSubscribers + "\n", "subscribers", String.valueOf(subscribers)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelInfoAutoSubscribe + "\n", "auto_subscribe", isAutoSubscribe ? "True" : "False"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelInfoExcludeFromDiscord + "\n", "exclude_from_discord", isExcludeFromDiscord ? "True" : "False"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelInfoShowChannelNameDiscord, "show_channel_name_discord", isShowChannelNameDiscord ? "True" : "False"));
    }

    // ** Set **
    // Channel No Exist
    private transient String crewChatChannelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component crewChatChannelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return crewChatHeader.append(MiniMessage.get().parse(channelNoExist, "channel_name", name));
    }

    // Property No Exist
    private transient String crewChatPropertyNoExist = "<white>Property <bold><property></bold> does not exist!";

    public Component crewChatPropertyNoExist(String property) {
        return crewChatHeader.append(MiniMessage.get().parse(crewChatPropertyNoExist, "property", property));
    }

    // Value Incorrect
    private transient String crewChatValueIncorrect = "<white>Value <bold><value></bold> is incorrect!";

    public Component crewChatValueIncorrect(String value) {
        return crewChatHeader.append(MiniMessage.get().parse(crewChatPropertyNoExist, "value", value));
    }

    // Property Changed
    private transient String crewChatPropertyChanged = "<white>Channel <bold><channel></bold> property <bold><property></bold> has been changed to <bold><value></bold>.";

    public Component crewChatPropertyChanged(String channel, String property, String value) {
        return crewChatHeader.append(MiniMessage.get().parse(crewChatPropertyChanged,
                "channel", channel,
                "property", property,
                "value", value));
    }

    // ** Help **

    // Help
    public Component crewChatHelpCommand() {
        return crewChatHeader
                .append(Component.text("Command Help:")
                    .color(WHITE))
                .append(Component.text(" - Alias: /cc <args> - (Click to run) -")
                    .color(GRAY))
                .append(Component.text("/crewchat")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Base CrewChat command.")
                    .color(WHITE))
                .append(Component.text("/crewchat help")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Shows this screen.")
                    .color(WHITE))
                .append(Component.text("/crewchat reload")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat reload")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Reload CrewChat configuration files.")
                    .color(WHITE));
    }

    // ** Reload **

    // Config Reloaded
    private transient Component configReloaded =
            Component.text("Configuration reloaded.")
                    .color(WHITE);

    public Component configReloaded() {
        // &7[&2CrewChat&7] &fConfiguration reloaded.
        return crewChatHeader.append(configReloaded);
    }

    // *** Me ***

    public Component meMessage(String playerName, String message, TextColor textColor) {
        // * %playerName% %message% *
        return MiniMessage.get().parse("<" + textColor.toString() + "><italic>* " + playerName + " " + message + " *");
    }

    // *** Broadcast ***

    @Comment("\nThis is the format used before every chat message sent.")
    private String broadcastMessageHeader = "<gray>[<dark_green>Broadcast<gray>] <white>";

    public Component broadcastMessage(String message) {
        // [Broadcast] <message>
        return MiniMessage.get().parse(broadcastMessageHeader)
                .append(MiniMessage.get().parse(message));
    }

    // *** Private Message ***

    // PM Header
    @Comment("\nThis is the format used before every private message sent.\n" +
            "Possible tags: <sender_prefix>, <sender_name>, <recipient_prefix>, <recipient_name>")
    private String privateMessageHeader = "<gray>[<reset><sender_prefix><sender_name><reset> <gray>-><reset> <recipient_prefix><recipient_name><reset><gray>]<reset> ";

    public Component privateMessageSend(String senderPrefix, String recipientPrefix, String recipientName,
                                        String senderStatus, String recipientStatus, String time, String message) {
        // &7[%senderPrefix%me &7-> %recipientPrefix%%recipientName%&7] &r%message%
        return MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", serialize(senderPrefix),
                "sender_name", you + "<hover:show_text:'<white>" + time + "\n" + senderStatus + "'>",
                "recipient_prefix", serialize(recipientPrefix),
                "recipient_name", recipientName + "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>")
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(message));
    }

    // Click to reply
    @Comment("\nAppears when hovering over private messages.")
    private String clickToReply = "<bold><aqua>Click<reset> this message to reply.";

    public Component privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                           String senderStatus, String recipientStatus, String time, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", serialize(senderPrefix),
                "sender_name", "<hover:show_text:'<white>" + time + "\n" + senderStatus + "'>" + senderName,
                "recipient_prefix", serialize(recipientPrefix),
                "recipient_name", "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>" + you)
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(
                        "<hover:show_text:'" + clickToReply + "'><click:suggest_command:/msg " + senderName + " >" + message));
    }

    // PM Can't Message Self
    @Comment("\nAppears when a player tries to private message themself.")
    private String cantMessageSelf = "<white>You can't send a message to yourself!";

    public Component cantMessageSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantMessageSelf));
    }

    // PM No PM Received
    @Comment("\nAppears when a player tries to reply without receiving a private message.")
    private String noPMReceived = "<white>You haven't received any messages!";

    public Component noPMReceived() {
        return chatHeader.append(MiniMessage.get().parse(noPMReceived));
    }

    // PM Player Doesn't Exist
    @Comment("\nAppears when a player tries to private message a player that doesn't exist.")
    private String playerNoExist = "<white>Player doesn't exist!";

    public Component playerNoExist() {
        return chatHeader.append(MiniMessage.get().parse(playerNoExist));
    }

    // *** Chat Messages ***

    // Chat Message
    @Comment("\nThis is the format used before every chat message sent.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String chatMessageHeader = "<player_prefix><player_name><gray>: ";

    public Component chatMessage(String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {
        // %prefix%%playerName%: %message%
        return MiniMessage.get().parse("<click:suggest_command:/msg " + playerName + " >" +
                        "<hover:show_text:'<white>" + time + "\n" +
                        this.status + ": " + status + "<reset>\n" +
                        this.channel + ": " + "<" + textColor.toString() + ">" + activeChannel + "'>" +
                        chatMessageHeader + "<reset><" + textColor.toString() + ">",
                "player_prefix", serialize(prefix),
                "player_name", playerName)
                .append(message);
    }

    // Discord Message
    @Comment("\nThis is the format used before every Discord message sent.\n" +
            "Possible tags: <discord>, <player_prefix>, <player_name>")
    private String discordMessageHeader = "<gray>[<color:#7289DA><discord><gray>] <player_prefix><player_name><gray>: ";

    public Component discordMessage(String discordHeader, String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {
        // [Discord] %prefix%%playerName%: %message%
        return MiniMessage.get().parse("<hover:show_text:'<white>" + time + "\n" +
                        this.status + ": " + status + "\n" +
                        this.channel + ": " + "<" + textColor.toString() + ">" + activeChannel + "'>" +
                        discordMessageHeader,
                "discord", discordHeader,
                "player_prefix", serialize(prefix),
                "player_name", playerName)
                .append(message);
    }

    // *** General ***

    // General fields used in many strings
    @Comment("\n\"Chat\" text that appears before many messages.\n" +
            "(Does not accept color codes)")
    private String chat = "Chat";

    @Comment("\nValue for the word \"Status\".")
    private String status = "Status";

    @Comment("\nValue for the word \"Channel\".")
    private String channel = "Channel";

    @Comment("\nValue for the word \"You\".")
    private String you = "You";

    @Comment("\nValue for the word \"Help\".\n" +
            "(Does not accept color codes)")
    private String help = "Help";

    // [Chat]
    private transient Component chatHeader =
            Component.text("[")
                    .color(GRAY)
                .append(Component.text(chat)
                    .color(DARK_GREEN))
                .append(Component.text("] ")
                    .color(GRAY))
                .append(Component.text()
                    .resetStyle());

    // Hyphen
    private transient Component hyphenHeader =
            Component.text(" - ")
                .color(DARK_GREEN)
                .append(Component.text()
                     .resetStyle());

    // Click to run
    private transient Component clickToRun =
            Component.text("Click")
                .color(DARK_GREEN)
                .decoration(BOLD, true)
                .append(Component.text(" to run.")
                    .decoration(BOLD, false)
                    .color(WHITE));

    // ** Bad Config **
    // Plugin Not Configured Correctly
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String pluginNotConfigured = "<white>Plugin not configured correctly.";

    // Incorrect Permission
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String permError = "<white>Permissions/configuration error.";

    public Component badConfig() {
        return chatHeader
                .append(MiniMessage.get().parse(pluginNotConfigured))
                .append(Component.text(""))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(permError));
    }

    // ** No Permission **

    // No Permission
    @Comment("\nAppears if player does not have permission to run a command.")
    private String noPermission = "<red>I'm sorry but you do not have permission to perform this" +
            "command. Please contact the server administrators if you" +
            "believe that this is in error.";

    public Component noPermission() {
        // I'm sorry but you do not have permission to perform this
        //  command. Please contact the server administrators if you
        //  believe that this is in error.
        return MiniMessage.get().parse(noPermission);
    }

    // *** Helper Methods ***

    private static String serialize(String legacyColorCode) {
        return MiniMessage.get().serialize(LegacyComponentSerializer.legacy('&').deserialize(MiniMessage.get().serialize(LegacyComponentSerializer.legacy('§').deserialize(legacyColorCode))));
    }
}
