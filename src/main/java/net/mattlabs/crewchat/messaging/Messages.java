package net.mattlabs.crewchat.messaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.util.HSVLike;
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

    /*================================================================================

                                      Header Fields

    ================================================================================*/

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

    /*================================================================================

                                      Chat Command

    ================================================================================*/

    // ** Base **

    // Welcome to chat
    @Comment("\nAppears in the chat base command.")
    private String welcomeToChat = "<white>Welcome to chat!";

    // TODO: Use user defined strings for hover text
    public Component chatBaseCommand() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(welcomeToChat))
                .append(Component.text(" [" + help + "]", BLUE, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" here for help.", WHITE, BOLD))))
                        .clickEvent(ClickEvent.runCommand("/chat help")))
                .build();
    }

    // ** Deafen **

    // Player Deafened
    @Comment("\nAppears when a player deafens themself.")
    private String playerDeafened = "<white>You have been deafened. You will not receive any chat messages.";

    public Component playerDeafened() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerDeafened))
                .build();
    }

    // Player Undeafened
    @Comment("\nAppears when a player undeafens themself.")
    private String playerUndeafened = "<white>You are no longer deafened. You will receive all chat messages.";

    public Component playerUndeafened() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerUndeafened))
                .build();
    }

    // Player Is Deafened
    @Comment("\nAppears when a player tries to send a chat message while deafened.")
    private String playerIsDeafened = "<white>You are deafened and cannot see chat messages!";

    public Component playerIsDeafened() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerIsDeafened))
                .build();
    }

    // ** General **

    // Channel No Exist
    @Comment("\nAppears when a command contains a channel that doesn't exist.\n" +
            "Possible tags: <channel_name>")
    private String channelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component channelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(channelNoExist, "channel_name", name))
                .build();
    }

    // TODO: **Possibly remove this**, Add new commands, add newline characters
    // ** Help **
    public Component chatHelpCommand() {
        return Component.text()
                .append(chatHeader)
                .append(Component.text("Command Help:", WHITE))
                .append(Component.text(" - Alias: /c <args> - (Click to run) -", GRAY))
                .append(Component.text("/chat", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.runCommand("/chat ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Base Chat command.", WHITE))
                .append(Component.text("/chat help", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/chat help")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows this screen.", WHITE))
                .append(Component.text("/chat info", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/chat info")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Lists all channels, active channel and subscribed channels.", WHITE))
                .append(Component.text("/chat info channel <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)))
                    .clickEvent(ClickEvent.suggestCommand("/chat info channel ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Lists info about specified channel.", WHITE))
                .append(Component.text("/chat status <status>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat status ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Sets player's status.", WHITE))
                .append(Component.text("/chat subscribe <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat subscribe ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Subscribes player to channel.", WHITE))
                .append(Component.text("/chat unsubscribe <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat unsubscribe ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Unsubscribes player from channel.", WHITE))
                .append(Component.text("/chat switch <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat switch ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Switches active channel.", WHITE))
                .build();
    }

    // ** Info **

    // Channel List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListHeader = "Channel List: (Click for more info)";

    public Component channelListHeader() {
        return Component.text()
                .append(Component.text("------------------------", GRAY))
                .append(Component.text("[", DARK_GRAY))
                .append(Component.text(chat, DARK_GREEN))
                .append(Component.text("]", DARK_GRAY))
                .append(Component.text("------------------------", GRAY))
                .append(Component.text(MessageUtil.sanitizeMessage(channelListHeader), GRAY))
                .build();
    }

    // Party List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListHeader = "Party List: (Click for more info)";

    public Component partyListHeader() {
        return Component.text()
                .append(Component.text(MessageUtil.sanitizeMessage(partyListHeader), GRAY))
                .build();
    }

    // Channel List Active
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListActive = "Your active channel is: <channel_name>.";

    public Component channelListActive(String name, TextColor textColor) {
        return MiniMessage.get().parse("<gray>" + MessageUtil.sanitizeMessage(channelListActive),
                "channel_name", "<" + textColor.toString() + "><bold>" + name + "<reset><gray>");
    }

    // Channel List Subscribe Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListSubscribedHeader = "Your subscribed channels are:";

    public Component channelListSubscribedHeader() {
        return Component.text()
                .append(Component.text(MessageUtil.sanitizeMessage(channelListSubscribedHeader), GRAY))
                .build();
    }

    // Party List Joined Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListJoinedHeader = "Your joined parties are:";

    public Component partyListJoinedHeader() {
        return Component.text()
                .append(Component.text(MessageUtil.sanitizeMessage(partyListJoinedHeader), GRAY))
                .build();
    }

    // Muted List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String mutedListHeader = "Your muted players are:";

    public Component mutedListHeader() {
        return Component.text()
                .append(Component.text(MessageUtil.sanitizeMessage(mutedListHeader), GRAY))
                .build();
    }

    // Click to unmute
    @Comment("\nAppears in the chat info command.")
    private String clickToUnmute = "<bold><dark_green>>Click<</bold></dark_green> <white>to unmute.";


    // Time remaining
    @Comment("\nAppears in the chat info command.\n" +
            "Possible tags: <time_remaining>")
    private String timeRemaining = "<white>Time remaining: <time_remaining>";

    public Component mutedListEntry(String player, String timeRemaining) {
        return Component.text()
                .append(hyphenHeader)
                .append(Component.text(player, WHITE)
                        .hoverEvent(HoverEvent.showText(MiniMessage.get().parse(
                                this.timeRemaining + "\n" + clickToUnmute, "time_remaining", timeRemaining)))
                        .clickEvent(ClickEvent.runCommand("/chat unmute " + player)))
                .build();
    }

    // Channel List Entry
    public Component channelListEntry(String name, TextColor textColor) {
        return Component.text()
                .append(Component.text(" - ", DARK_GREEN))
                .append(Component.text(name, textColor, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" for more info.", WHITE))))
                        .clickEvent(ClickEvent.runCommand("/chat info channel " + name)))
                .build();
    }

    // Party List Entry
    public Component partyListEntry(String name, TextColor textColor) {
        return Component.text()
                .append(Component.text(" - ", DARK_GREEN))
                .append(Component.text(name, textColor, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" for more info.", WHITE))))
                        .clickEvent(ClickEvent.runCommand("/chat info channel " + name)))
                .build();
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

        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(channelInfoHeader + "\n", "channel_name", "<color:" + color.toString() + ">" + name + "</color:" + color.toString() + ">"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoName + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoDescription + "\n", "channel_description", description))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoColor, "channel_color", "<color:" + color.toString() + ">" + closestColor + " (" + color.asHexString() + ")" + "</color:" + color.toString() + ">"))
                .build();
    }

    // ** Mute/Unmute **

    // Can't Mute Self
    @Comment("\nAppears when a player tries to mute themself.")
    private String cantMuteSelf = "<white>You can't mute yourself!";

    public Component cantMuteSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantMuteSelf))
                .build();
    }

    // Can't Unmute Self
    @Comment("\nAppears when a player tries to unmute themself.")
    private String cantUnmuteSelf = "<white>You can't unmute yourself!";

    public Component cantUnmuteSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantUnmuteSelf))
                .build();
    }

    // Player Already Muted
    @Comment("\nAppears when a player tries to mute someone they've already muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyMuted = "<white><player_prefix><player_name><white> is already muted!";

    public Component playerAlreadyMuted(String playerPrefix, String playerName) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerAlreadyMuted,
                "player_prefix", MessageUtil.serialize(playerPrefix),
                "player_name", playerName))
                .build();
    }

    // Player Already Unmuted
    @Comment("\nAppears when a player tries to unmute someone who isn't muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyUnmuted = "<white><player_prefix><player_name><white> is not muted!";

    public Component playerAlreadyUnmuted(String playerPrefix, String playerName) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerAlreadyUnmuted,
                "player_prefix", MessageUtil.serialize(playerPrefix),
                "player_name", playerName))
                .build();
    }

    // Player Muted
    @Comment("\nAppears when a player mutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerMuted = "<white><player_prefix><player_name><white> has been muted.";

    public Component playerMuted(String playerPrefix, String player) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerMuted,
                "player_prefix", MessageUtil.serialize(playerPrefix),
                "player_name", player))
                .build();
    }

    // Player Unmuted
    @Comment("\nAppears when a player unmutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerUnmuted = "<white><player_prefix><player_name><white> has been unmuted.";

    public Component playerUnmuted(String playerPrefix, String player) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerUnmuted,
                "player_prefix", MessageUtil.serialize(playerPrefix),
                "player_name", player))
                .build();
    }

    // ** Status **

    // Status Set
    @Comment("\nAppears when a player sets their status.\n" +
            "Possible tags: <status>")
    private String statusSet = "<white>Your status has been set to: \"<status>\".";

    public Component statusSet(String status) throws ParsingException {
        //noinspection unchecked
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(
                        MessageUtil.sanitizeMessageColor(statusSet),
                        "status", MessageUtil.serialize(status) + "<reset>"))
                .build();
    }

    // Current Status
    @Comment("\nAppears when a player checks their status.\n" +
            "Possible tags: <status>")
    private String statusIs = "<white>Your status is: \"<status>\".";

    public Component statusIs(String status) throws ParsingException {
        //noinspection unchecked
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(
                        MessageUtil.sanitizeMessageColor(statusIs),
                        "status", MessageUtil.serialize(status) + "<reset>"))
                .build();
    }

    // Status Syntax Error
    @Comment("\nAppears if a player's status contains a syntax error.")
    private String statusSyntaxError = "<white>Your status could not be set. Please check the syntax.";

    public Component statusSyntaxError() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(statusSyntaxError))
                .build();
    }

    // ** Subscribe/Unsubscribe **

    // Already Subscribed
    @Comment("\nAppears when a player tries to subscribe to a channel they already subscribe to.\n" +
            "Possibe tags: <channel_name>")
    private String alreadySubscribed = "<white>You are already subscribed to <bold><channel_name></bold>.";

    public Component alreadySubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(alreadySubscribed,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    // Can't Subscribe
    @Comment("\nAppears when a player can't subscribe to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSubscribe = "<white>You can't subscribe to <bold><channel_name></bold>!";

    public Component cantSubscribe(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantSubscribe,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    // Can't Unsubscribe
    @Comment("\nAppears when a player can't unsubscribe from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribe = "<white>You can't unsubscribe from <bold><channel_name></bold>!";

    public Component cantUnsubscribe(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantUnsubscribe,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName + "</color:" + textColor.toString() + ">"))
                .build();
    }

    // Can't Unsubscribe Active
    @Comment("\nAppears when a player can't unsubscribe from a channel because it is their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribeActive = "<white>You can't unsubscribe from <bold><channel_name></bold>, it is your active channel!";

    public Component cantUnsubscribeActive(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantUnsubscribeActive,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName + "</color:" + textColor.toString() + ">"))
                .build();
    }

    // Not Subscribed
    @Comment("\nAppears when a player unsubscribes from a channel they are not subscribed to.\n" +
            "Possibe tags: <channel_name>")
    private String notSubscribed = "<white>You aren't subscribed to <bold><channel_name></bold>.";

    public Component notSubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(notSubscribed,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName + "</color:" + textColor.toString() + ">"))
                .build();
    }

    // Now Subscribed
    @Comment("\nAppears when a player subscribes to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowSubscribed = "<white>You are now subscribed to <bold><channel_name></bold>.";

    public Component nowSubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(nowSubscribed,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    // Now Unsubscribed
    @Comment("\nAppears when a player unsubscribes from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowUnsubscribed = "<white>You are no longer subscribed to <bold><channel_name></bold>.";

    public Component nowUnsubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(nowUnsubscribed,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    // ** Switch **

    // New Active Channel
    @Comment("\nAppears when a player changes their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String newActiveChannel = "<white>Your active channel is now <bold><channel_name></bold>.";

    public Component newActiveChannel(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(newActiveChannel,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    // Can't Set Active
    @Comment("\nAppears when a player can't change their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSetActive = "<white>You can't set <bold><channel_name></bold> as your active channel!";

    public Component cantSetActive(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantSetActive,
                        "channel_name", "<color:" + textColor.toString() + ">" + channelName))
                .build();
    }

    /*================================================================================

                                    CrewChat Command

    ================================================================================*/

    // Header
    private transient Component crewChatHeader = Component.text()
            .append(Component.text("[", GRAY))
            .append(Component.text("CrewChat", DARK_GREEN))
            .append(Component.text("] ", GRAY))
            .build();

    // Base
    public Component crewChatBaseCommand() {
        return Component.text()
                .append(crewChatHeader)
                .append(Component.text("Version " +
                    CrewChat.getInstance().getDescription().getVersion() +
                    ". For help, click ", WHITE))
                .append(Component.text("[Help]", BLUE, BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(Component.text("Click", DARK_GREEN, BOLD))
                            .append(Component.text(" here for help.", WHITE))))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .build();
    }

    // ** Info **

    // Info Header
    private transient String crewChatInfoHeader = "<white>Version <bold><version></bold> Info:";

    // Info Channels
    private transient String crewChatChannelsLoaded = "<white><bold><channels></bold> channel(s) loaded.";

    // Info Players
    private transient String crewChatPlayersLoaded = "<white><bold><players></bold> player(s) loaded.";

    // Info Online Players
    private transient String crewChatOnlinePlayersLoaded = "<white><bold><players></bold> online player(s) loaded.";

    // Info Discord Integration
    private transient String crewChatDiscordIntegration = "<white>Discord integration enabled: <bold><enabled></bold>.";
    
    // Info
    public Component crewChatInfo(int channelsLoaded, int playersLoaded, int onlinePlayersLoaded, boolean discordIntegration) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatInfoHeader,
                "version", CrewChat.getInstance().getDescription().getVersion())
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatChannelsLoaded + "\n", "channels", String.valueOf(channelsLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatPlayersLoaded + "\n", "players", String.valueOf(playersLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatOnlinePlayersLoaded + "\n", "players", String.valueOf(onlinePlayersLoaded)))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(crewChatDiscordIntegration, "enabled", discordIntegration ? "True" : "False")))
                .build();
    }

    // ** Info Channel **

    // Header
    public Component crewChatChannelListHeader() {
        return Component.text()
                .append(Component.text("----------------------", GRAY))
                .append(Component.text("[", DARK_GRAY))
                .append(Component.text("CrewChat", DARK_GREEN))
                .append(Component.text("]", DARK_GRAY))
                .append(Component.text("----------------------", GRAY))
                .append(Component.text(channelListHeader, GRAY))
                .build();
    }

    // Channel Entry
    public Component crewChatChannelListEntry(String name, TextColor textColor) {
        return Component.text()
                .append(Component.text(" - ", DARK_GREEN))
                .append(Component.text(name, textColor, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" for more info.", WHITE))))
                        .clickEvent(ClickEvent.runCommand("/crewchat info channel " + name)))
                .build();
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

        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(channelInfoHeader + "\n", "channel_name", "<color:" + color.toString() + ">" + name + "</color:" + color.toString() + ">"))
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
                .append(MiniMessage.get().parse(crewChatChannelInfoShowChannelNameDiscord, "show_channel_name_discord", isShowChannelNameDiscord ? "True" : "False"))
                .build();
    }

    // ** Info Player **

    // Header
    private transient String crewChatInfoPlayerHeader = "<white>Player info for <bold><player></bold>:";

    public Component crewChatInfoPlayerHeader(String player) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatInfoPlayerHeader, "player", player))
                .build();
    }

    // Subscribed Channels Header
    private transient String crewChatChannelListHeader = "<gray>Subscribed channel(s):";

    public Component crewChatChannelListHeaderSmall() {
        return MiniMessage.get().parse(crewChatChannelListHeader);
    }

    // Active Channel
    private transient String crewChatActiveChannel = "<gray>Active channel: <active_channel>";

    public Component crewChatActiveChannel(String activeChannel, TextColor textColor) {
        return MiniMessage.get().parse(crewChatActiveChannel,
                "active_channel", "<" + textColor.toString() + "><bold>" + activeChannel + "<reset><gray>");
    }

    // Status
    private transient String crewChatStatus = "<gray>Status: <status>";

    public Component crewChatStatus(String status) {
        return MiniMessage.get().parse(crewChatStatus,
                "status", MessageUtil.sanitizeMessageColor(status));
    }

    // Mute Header
    private transient String crewChatMuteHeader = "<gray>Muted player(s):";

    public Component crewChatMuteHeader() {
        return MiniMessage.get().parse(crewChatMuteHeader);
    }

    // ** Set **

    // Channel No Exist
    private transient String crewChatChannelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component crewChatChannelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatChannelNoExist, "channel_name", name))
                .build();
    }

    // Property No Exist
    private transient String crewChatPropertyNoExist = "<white>Property <bold><property></bold> does not exist!";

    public Component crewChatPropertyNoExist(String property) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatPropertyNoExist, "property", property))
                .build();
    }
    
    // Value Incorrect
    private transient String crewChatValueIncorrect = "<white>Value <bold><value></bold> is incorrect!";

    public Component crewChatValueIncorrect(String value) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatValueIncorrect, "value", value))
                .build();
    }

    // Property Changed
    private transient String crewChatPropertyChanged = "<white>Channel <bold><channel></bold> property <bold><property></bold> has been changed to <bold><value></bold>.";

    public Component crewChatPropertyChanged(String channel, String property, String value) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.get().parse(crewChatPropertyChanged,
                "channel", channel,
                "property", property,
                "value", value))
                .build();
    }

    // ** Help **

    // TODO: Add new lines, update commands
    // Help
    public Component crewChatHelpCommand() {
        return Component.text()
                .append(crewChatHeader)
                .append(Component.text("Command Help:", WHITE))
                .append(Component.text(" - Alias: /cc <args> - (Click to run) -", GRAY))
                .append(Component.text("/crewchat", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/crewchat")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Base CrewChat command.", WHITE))
                .append(Component.text("/crewchat help", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows this screen.", WHITE))
                .append(Component.text("/crewchat reload", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/crewchat reload")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Reload CrewChat configuration files.", WHITE))
                .build();
    }

    // ** Reload **

    // Config Reloaded
    private transient Component configReloaded = Component.text()
            .append(Component.text("Configuration reloaded.", WHITE))
                    .build();

    public Component configReloaded() {
        // &7[&2CrewChat&7] &fConfiguration reloaded.
        return Component.text()
                .append(crewChatHeader)
                .append(configReloaded)
                .build();
    }

    /*================================================================================

                                       Me Command

    ================================================================================*/

    public Component meMessage(String playerName, String message, TextColor textColor) {
        // * %playerName% %message% *
        return MiniMessage.get().parse("<" + textColor.toString() + "><italic>* " + playerName + " " + message + " *");
    }

    /*================================================================================

                                    Broadcast Command

    ================================================================================*/

    @Comment("\nThis is the format used before every chat message sent.")
    private String broadcastMessageHeader = "<gray>[<dark_green>Broadcast<gray>] <white>";

    public Component broadcastMessage(String message) {
        // [Broadcast] <message>
        return Component.text()
                .append(MiniMessage.get().parse(broadcastMessageHeader))
                .append(MiniMessage.get().parse(message))
                .build();
    }

    /*================================================================================

                                     Party Command

    ================================================================================*/

    // Party
    @Comment("\nText for the word \"party\"\n" +
            "(Does not accept color codes.")
    private String party = "party";

    // Party Header
    private transient Component partyHeader = MiniMessage.get().parse("<gray>[<dark_green>" + WordUtils.capitalize(MessageUtil.sanitizeMessage(party)) + "<gray>] ");

    // Party Exists
    @Comment("\nAppears if a party already exists during creation.\n" +
            "Possible tags: <party>")
    private String partyAlreadyExists = "<white>Party <party> already exists!";

    public Component partyAlreadyExists(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyAlreadyExists,
                "party", "<" + textColor.toString() + ">" + party + "<reset>"))
                .build();
    }

    // Channel Exists
    @Comment("\nAppears if a channel already exists with desired party name during creation.\n" +
            "Possible tags: <party>")
    private String partyChannelAlreadyExists = "<white>Channel <channel> already exists with that name!";

    public Component partyChannelAlreadyExists(String channel, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyChannelAlreadyExists,
                "channel", "<" + textColor.toString() + "><bold>" + channel + "<reset>"))
                .build();
    }

    // Party No Exist
    @Comment("\nAppears when joining a party that doesn't exist.\n" +
            "Possible tags: <party_name>")
    private String partyNoExist = "<white>Party <bold><party_name></bold> doesn't exist!";

    public Component partyNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyNoExist, "party_name", name))
                .build();
    }

    // Party will be created
    @Comment("\nAppears when creating a party.\n" +
            "Possible tags: <party>")
    private String partyWillBeCreated = "<white>Party <bold><party></bold> will be created.";

    // Color picker
    @Comment("\nAppears above the party color picker.\n" +
            "Possible tags: <hex>")
    private String pickAColor = "<white>Please choose a color <hex>:";

    // Hex
    @Comment("\nAppears above the party color picker.")
    private String hex = "<grey>(or enter #hex)";

    // Click to select
    @Comment("\nAppears when hovering over the color picker.")
    private String clickToPick = "<dark_green><bold>Click</bold> <white>to select this color.";

    // Preview
    @Comment("\nValue for the word \"preview\".\n" +
            "(Does not accept color codes.)")
    private String preview = "preview";

    // Pick a Color Swatch Hover Text
    private HoverEvent<Component> pickAColorHover(String party, TextColor textColor) {
        return HoverEvent.showText(Component.text()
                .append(Component.text(WordUtils.capitalize(preview) + ": ", WHITE))
                .append(Component.text(party, textColor, BOLD))
                .append(Component.text("\n"))
                .append(MiniMessage.get().parse(clickToPick))
                .build());
    }

    // Color Palette
    public Component pickAColor(String party) {
        Component message = Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyWillBeCreated, "party", party))
                .append(Component.text("\n"))
                .append(MiniMessage.get().parse(pickAColor,
                        "hex", MiniMessage.get().parse(hex)
                                .hoverEvent(HoverEvent.showText(clickToRun))
                                .clickEvent(ClickEvent.suggestCommand("/party create " + party + " #"))))
                .append(Component.text("\n"))
                .build();


        // Color squares
        for (float value = 1.0f; value >= 0.5f; value -= 0.5f) {
            for (float hue = 0.0f; hue <= 1.0f; hue += 0.05f) {
                message = message.append(Component.text("█")
                        .color(TextColor.color(HSVLike.of(hue, 1.0f, value)))
                        .hoverEvent(pickAColorHover(party, TextColor.color(HSVLike.of(hue, 1.0f, value))))
                        .clickEvent(ClickEvent.runCommand("/party create " + party + " " + TextColor.color(HSVLike.of(hue, 1.0f, value)).asHexString())));
            }
            if (value == 1.0f) {
                message = message.append(Component.text("█")
                        .color(GRAY)
                        .hoverEvent(pickAColorHover(party, GRAY))
                        .clickEvent(ClickEvent.runCommand("/party create " + party + " " + GRAY.asHexString())));
                message = message.append(Component.text("█\n")
                        .color(WHITE)
                        .hoverEvent(pickAColorHover(party, WHITE))
                        .clickEvent(ClickEvent.runCommand("/party create " + party + " " + WHITE.asHexString())));
            }
            else if (value == 0.5f) {
                message = message.append(Component.text("█")
                        .color(DARK_GRAY)
                        .hoverEvent(pickAColorHover(party, DARK_GRAY))
                        .clickEvent(ClickEvent.runCommand("/party create " + party + " " + DARK_GRAY.asHexString())));
                message = message.append(Component.text("█")
                        .color(BLACK)
                        .hoverEvent(pickAColorHover(party, BLACK))
                        .clickEvent(ClickEvent.runCommand("/party create " + party + " " + BLACK.asHexString())));
            }
        }
        return message;
    }

    // Invalid color
    @Comment("Appears when specifying an invalid hex color for a party.\n" +
            "Possible tags: <hex_color>")
    private String invalidColor = "<white>Color \"<hex_color>\" is invalid!";

    public Component invalidColor(String invalidHex) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(invalidColor, "hex_color", invalidHex))
                .build();
    }

    // Party Created
    @Comment("Appears when a party is successfully created.\n" +
            "Possible tags: <party>")
    private String partyCreated = "<white>Party <party> has been created successfully.";

    public Component partyCreated(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyCreated,
                        "party", "<" + textColor.toString() + "><bold>" + party + "</bold><reset>"))
                .build();
    }

    // Party Joined
    @Comment("Appears when joining a party.\n" +
            "Possible tags: <party>")
    private String partyJoined = "<white>You have joined <party>.";

    public Component partyJoined(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyJoined,
                "party", "<" + textColor.toString() + "><bold>" + party + "</bold><reset>"))
                .build();
    }

    // Already in Party
    @Comment("Appears when trying to join a party a player is already in.\n" +
            "Possible tags: <party>")
    private String alreadyInParty = "<white>You are already in <party>!";

    public Component alreadyInParty(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(alreadyInParty,
                        "party", "<" + textColor.toString() + "><bold>" + party + "</bold><reset>"))
                .build();
    }

    // Party Left
    @Comment("Appears when leaving a party.\n" +
            "Possible tags: <party>")
    private String partyLeft = "<white>You have left <party>.";

    public Component partyLeft(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(partyLeft,
                        "party", "<" + textColor.toString() + "><bold>" + party + "</bold><reset>"))
                .build();
    }

    // Not in Party
    @Comment("Appears when trying to leave a party a player is not in.\n" +
            "Possible tags: <party>")
    private String notInParty = "<white>You are not in <party>!";

    public Component notInParty(String party, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.get().parse(notInParty,
                        "party", "<" + textColor.toString() + "><bold>" + party + "</bold><reset>"))
                .build();
    }

    /*================================================================================

                                Private Message Command

    ================================================================================*/

    // PM Header
    @Comment("\nThis is the format used before every private message sent.\n" +
            "Possible tags: <sender_prefix>, <sender_name>, <recipient_prefix>, <recipient_name>")
    private String privateMessageHeader = "<gray>[<reset><sender_prefix><sender_name><reset> <gray>-><reset> <recipient_prefix><recipient_name><reset><gray>]<reset> ";

    public Component privateMessageSend(String senderPrefix, String recipientPrefix, String recipientName,
                                        String senderStatus, String recipientStatus, String time, String message) {
        // &7[%senderPrefix%me &7-> %recipientPrefix%%recipientName%&7] &r%message%
        return Component.text()
                .append(MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", MessageUtil.serialize(senderPrefix),
                "sender_name", you + "<hover:show_text:'<white>" + time + "\n" + senderStatus + "'>",
                "recipient_prefix", MessageUtil.serialize(recipientPrefix),
                "recipient_name", recipientName + "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>"))
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(message))
                .build();
    }

    // Click to reply
    @Comment("\nAppears when hovering over private messages.")
    private String clickToReply = "<bold><aqua>Click<reset> this message to reply.";

    public Component privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                           String senderStatus, String recipientStatus, String time, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return Component.text()
                .append(MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", MessageUtil.serialize(senderPrefix),
                "sender_name", "<hover:show_text:'<white>" + time + "\n" + senderStatus + "'>" + senderName,
                "recipient_prefix", MessageUtil.serialize(recipientPrefix),
                "recipient_name", "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>" + you))
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(
                        "<hover:show_text:'" + clickToReply + "'><click:suggest_command:/msg " + senderName + " >" + message))
                .build();
    }

    // PM Can't Message Self
    @Comment("\nAppears when a player tries to private message themself.")
    private String cantMessageSelf = "<white>You can't send a message to yourself!";

    public Component cantMessageSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(cantMessageSelf))
                .build();
    }

    // PM No PM Received
    @Comment("\nAppears when a player tries to reply without receiving a private message.")
    private String noPMReceived = "<white>You haven't received any messages!";

    public Component noPMReceived() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(noPMReceived))
                .build();
    }

    // PM Player Doesn't Exist
    @Comment("\nAppears when a player tries to private message a player that doesn't exist.")
    private String playerNoExist = "<white>Player doesn't exist!";

    public Component playerNoExist() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(playerNoExist))
                .build();
    }

    /*================================================================================

                                     Chat Messages

    ================================================================================*/

    // Chat Message
    @Comment("\nThis is the format used before every chat message sent.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String chatMessageHeader = "<player_prefix><player_name><gray>: ";

    public Component chatMessage(String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor, boolean isParty) {
        // %prefix%%playerName%: %message%
        return Component.text()
                .append(MiniMessage.get().parse("<click:suggest_command:/msg " + playerName + " >" +
                        "<hover:show_text:'<white>" + time + "\n" +
                        this.status + ": " + status + "<reset>\n" +
                        (isParty ? WordUtils.capitalize(MessageUtil.sanitizeMessage(party)) : this.channel) + ": " + "<" + textColor.toString() + ">" + activeChannel + "'>" +
                        "<gray>[</gray><" + textColor.toString() + ">" + activeChannel + "</" + textColor.toString() + "><gray>]</gray> " +
                        chatMessageHeader + "<reset><" + textColor.toString() + ">",
                "player_prefix", MessageUtil.serialize(prefix),
                "player_name", playerName))
                .append(message)
                .build();
    }

    // Discord Message
    @Comment("\nThis is the format used before every Discord message sent.\n" +
            "Possible tags: <discord>, <player_prefix>, <player_name>")
    private String discordMessageHeader = "<gray>[<color:#7289DA><discord><gray>] <player_prefix><player_name><gray>: ";

    public Component discordMessage(String discordHeader, String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {
        // [Discord] %prefix%%playerName%: %message%
        return Component.text()
                .append(MiniMessage.get().parse("<hover:show_text:'<white>" + time + "\n" +
                        this.status + ": " + status + "\n" +
                        this.channel + ": " + "<" + textColor.toString() + ">" + activeChannel + "'>" +
                        discordMessageHeader,
                "discord", discordHeader,
                "player_prefix", MessageUtil.serialize(prefix),
                "player_name", playerName))
                .append(message)
                .build();
    }

    /*================================================================================

                                        General

    ================================================================================*/

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
    private transient Component chatHeader = Component.text()
            .append(Component.text("[", GRAY))
            .append(Component.text(chat, DARK_GREEN))
            .append(Component.text("] ", GRAY))
            .build();

    // Hyphen
    private transient Component hyphenHeader = Component.text()
            .append(Component.text(" - ", DARK_GREEN))
            .build();

    // Click to run
    private transient Component clickToRun = Component.text()
            .append(Component.text("Click", DARK_GREEN, BOLD))
            .append(Component.text(" to run.", WHITE))
            .build();

    // ** Bad Config **

    // Plugin Not Configured Correctly
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String pluginNotConfigured = "<white>Plugin not configured correctly.";

    // Incorrect Permission
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String permError = "<white>Permissions/configuration error.";

    public Component badConfig() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.get().parse(pluginNotConfigured))
                .append(Component.text("\n"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(permError))
                .build();
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
}