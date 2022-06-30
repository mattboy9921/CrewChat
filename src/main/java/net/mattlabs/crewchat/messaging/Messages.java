package net.mattlabs.crewchat.messaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.HSVLike;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MessageUtil;
import net.mattlabs.crewchat.util.Versions;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
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
    
    // Click here for help
    @Comment("\nAppears in the chat base command.")
    private String clickForHelp = "<dark_green><bold>Click<reset> <white>here for help.";
    
    public Component chatBaseCommand() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(welcomeToChat))
                .append(Component.text(" [" + WordUtils.capitalize(help) + "]", BLUE, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(MiniMessage.miniMessage().deserialize(clickForHelp))))
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
                .append(MiniMessage.miniMessage().deserialize(playerDeafened))
                .build();
    }

    // Player Undeafened
    @Comment("\nAppears when a player undeafens themself.")
    private String playerUndeafened = "<white>You are no longer deafened. You will receive all chat messages.";

    public Component playerUndeafened() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerUndeafened))
                .build();
    }

    // Player Is Deafened
    @Comment("\nAppears when a player tries to send a chat message while deafened.")
    private String playerIsDeafened = "<white>You are deafened and cannot see chat messages!";

    public Component playerIsDeafened() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerIsDeafened))
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
                .append(MiniMessage.miniMessage().deserialize(channelNoExist, TagResolver.resolver(Placeholder.parsed("channel_name", name))))
                .build();
    }

    // ** Help **
    public Component chatHelpCommand() {
        return Component.text()
                // Header
                .append(chatHeader)
                .append(Component.text("Command Help:", WHITE))
                .append(Component.text(" - Alias: /c <args> - (Click to run) -", GRAY))
                .append(Component.text("\n"))
                // chat
                .append(Component.text("/chat", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.runCommand("/chat ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Base Chat command.", WHITE))
                .append(Component.text("\n"))
                // chat help
                .append(Component.text("/chat help", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/chat help")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows this screen.", WHITE))
                .append(Component.text("\n"))
                // chat info
                .append(Component.text("/chat info", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/chat info")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows channel list, party list, active channel, subscribed channels and joined parties.", WHITE))
                .append(Component.text("\n"))
                // chat info channel <channel>
                .append(Component.text("/chat info channel <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)))
                    .clickEvent(ClickEvent.suggestCommand("/chat info channel ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Lists info about specified channel.", WHITE))
                .append(Component.text("\n"))
                // chat status [status]
                .append(Component.text("/chat status [status]", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat status ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Sets (or shows) your status.", WHITE))
                .append(Component.text("\n"))
                // chat subscribe <channel>
                .append(Component.text("/chat subscribe <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat subscribe ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Subscribes you to specified channel.", WHITE))
                .append(Component.text("\n"))
                // chat unsubscribe <channel>
                .append(Component.text("/chat unsubscribe <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat unsubscribe ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Unsubscribes you from specified channel.", WHITE))
                .append(Component.text("\n"))
                // chat switch <channel>
                .append(Component.text("/chat switch <channel>", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.suggestCommand("/chat switch ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Switches your active channel.", WHITE))
                .append(Component.text("\n"))
                // chat mute <player>
                .append(Component.text("/chat mute <player>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/chat mute ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Mutes the specified player for 24 hours.", WHITE))
                .append(Component.text("\n"))
                // chat mute <player>
                .append(Component.text("/chat mute <player>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/chat mute ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Mutes specified player for 24 hours.", WHITE))
                .append(Component.text("\n"))
                // chat deafen
                .append(Component.text("/chat deafen", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/chat mute ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Toggles your deafened state (block all chat).", WHITE))
                .append(Component.text("\n"))
                // chat send <channel> <message>
                .append(Component.text("/chat send <channel> <message>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/chat send ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Sends a message to the specified channel without switching to the channel.", WHITE))
                .append(Component.text("\n"))
                // chat mention <player>
                .append(Component.text("/chat mention <player>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/chat mention ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Provides a list of players/Discord accounts to mention.", WHITE))
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
                .append(Component.text(WordUtils.capitalize(chat), DARK_GREEN))
                .append(Component.text("]", DARK_GRAY))
                .append(Component.text("------------------------", GRAY))
                .append(Component.text("\n"))
                .append(Component.text(channelListHeader, GRAY))
                .build();
    }

    // Party List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListHeader = "Party List:";

    public Component partyListHeader() {
        return Component.text()
                .append(Component.text(partyListHeader, GRAY))
                .build();
    }

    // Channel List Active
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListActive = "You are currently active in: <channel_name>.";

    public Component channelListActive(String name, TextColor textColor) {
        return MiniMessage.miniMessage().deserialize("<gray>" + channelListActive,
                Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + name + "</bold></color:" + textColor.asHexString() + "><gray>"));
    }

    // Channel List Subscribe Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListSubscribedHeader = "Your subscribed channels are:";

    public Component channelListSubscribedHeader() {
        return Component.text()
                .append(Component.text(channelListSubscribedHeader, GRAY))
                .build();
    }

    // Party List Joined Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListJoinedHeader = "Your joined parties are:";

    public Component partyListJoinedHeader() {
        return Component.text()
                .append(Component.text(partyListJoinedHeader, GRAY))
                .build();
    }

    // Muted List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String mutedListHeader = "Your muted players are:";

    public Component mutedListHeader() {
        return Component.text()
                .append(Component.text(mutedListHeader, GRAY))
                .build();
    }

    // Click to unmute
    @Comment("\nAppears in the chat info command.")
    private String clickToUnmute = "<bold><dark_green>Click</bold></dark_green> <white>to unmute.";


    // Time remaining
    @Comment("\nAppears in the chat info command.\n" +
            "Possible tags: <time_remaining>")
    private String timeRemaining = "<white>Time remaining: <time_remaining>";

    public Component mutedListEntry(String player, String timeRemaining) {
        return Component.text()
                .append(hyphenHeader)
                .append(Component.text(player, WHITE)
                        .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize(
                                this.timeRemaining + "\n" + clickToUnmute, TagResolver.resolver(Placeholder.parsed("time_remaining", timeRemaining))))))
                        .clickEvent(ClickEvent.runCommand("/chat unmute " + player))
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
                .append(Component.text(name, textColor, BOLD))
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
                .append(MiniMessage.miniMessage().deserialize(channelInfoHeader + "\n", TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + color.toString() + ">" + name + "</color:" + color.toString() + ">"))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoName + "\n", TagResolver.resolver(Placeholder.parsed("channel_name", name))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoDescription + "\n", TagResolver.resolver(Placeholder.parsed("channel_description", description))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoColor, TagResolver.resolver(Placeholder.parsed("channel_color", "<color:" + color.toString() + ">" + closestColor + " (" + color.asHexString() + ")" + "</color:" + color.toString() + ">"))))
                .build();
    }

    // ** Mute/Unmute **

    // Can't Mute Self
    @Comment("\nAppears when a player tries to mute themself.")
    private String cantMuteSelf = "<white>You can't mute yourself!";

    public Component cantMuteSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantMuteSelf))
                .build();
    }

    // Can't Unmute Self
    @Comment("\nAppears when a player tries to unmute themself.")
    private String cantUnmuteSelf = "<white>You can't unmute yourself!";

    public Component cantUnmuteSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantUnmuteSelf))
                .build();
    }

    // Player Already Muted
    @Comment("\nAppears when a player tries to mute someone they've already muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyMuted = "<white><player_prefix><player_name><white> is already muted!";

    public Component playerAlreadyMuted(String playerPrefix, String playerName) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerAlreadyMuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.parsed("player_name", playerName))))
                .build();
    }

    // Player Already Unmuted
    @Comment("\nAppears when a player tries to unmute someone who isn't muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyUnmuted = "<white><player_prefix><player_name><white> is not muted!";

    public Component playerAlreadyUnmuted(String playerPrefix, String playerName) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerAlreadyUnmuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.parsed("player_name", playerName))))
                .build();
    }

    // Player Muted
    @Comment("\nAppears when a player mutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerMuted = "<white><player_prefix><player_name><white> has been muted.";

    public Component playerMuted(String playerPrefix, String player) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerMuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.parsed("player_name", player))))
                .build();
    }

    // Player Unmuted
    @Comment("\nAppears when a player unmutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerUnmuted = "<white><player_prefix><player_name><white> has been unmuted.";

    public Component playerUnmuted(String playerPrefix, String player) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerUnmuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.parsed("player_name", player))))
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
                .append(MiniMessage.miniMessage().deserialize(
                        statusSet,
                        Placeholder.component("status", MessageUtil.getSafeMM().deserialize(status))))
                .build();
    }

    // Current Status
    @Comment("\nAppears when a player checks their status.\n" +
            "Possible tags: <status>")
    private String statusIs = "<white>Your status is: \"<status>\".";

    public Component statusIs(String status) throws ParsingException {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(
                        statusIs,
                        Placeholder.component("status", MessageUtil.getSafeMM().deserialize(status))))
                .build();
    }

    // Status Syntax Error
    @Comment("\nAppears if a player's status contains a syntax error.")
    private String statusSyntaxError = "<white>Your status could not be set. Please check the syntax.";

    public Component statusSyntaxError() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(statusSyntaxError))
                .build();
    }

    // ** Subscribe/Unsubscribe **

    // Already Subscribed
    @Comment("\nAppears when a player tries to subscribe to a channel they already subscribe to.\n" +
            "Possibe tags: <channel_name>")
    private String alreadySubscribed = "<white>You are already subscribed to <channel_name>.";

    public Component alreadySubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(alreadySubscribed,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Can't Subscribe
    @Comment("\nAppears when a player can't subscribe to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSubscribe = "<white>You can't subscribe to <channel_name>!";

    public Component cantSubscribe(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantSubscribe,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Can't Unsubscribe
    @Comment("\nAppears when a player can't unsubscribe from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribe = "<white>You can't unsubscribe from <channel_name>!";

    public Component cantUnsubscribe(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantUnsubscribe,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Can't Unsubscribe Active
    @Comment("\nAppears when a player can't unsubscribe from a channel because it is their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribeActive = "<white>You can't unsubscribe from <channel_name>, it is your active channel!";

    public Component cantUnsubscribeActive(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantUnsubscribeActive,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Not Subscribed
    @Comment("\nAppears when a player unsubscribes from a channel they are not subscribed to.\n" +
            "Possibe tags: <channel_name>")
    private String notSubscribed = "<white>You aren't subscribed to <channel_name>.";

    public Component notSubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(notSubscribed,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Now Subscribed
    @Comment("\nAppears when a player subscribes to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowSubscribed = "<white>You are now subscribed to <channel_name>.";

    public Component nowSubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(nowSubscribed,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Now Unsubscribed
    @Comment("\nAppears when a player unsubscribes from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowUnsubscribed = "<white>You are no longer subscribed to <channel_name>.";

    public Component nowUnsubscribed(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(nowUnsubscribed,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // ** Switch **

    // New Active Channel
    @Comment("\nAppears when a player changes their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String newActiveChannel = "<white>Your active channel is now <channel_name>.";

    public Component newActiveChannel(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(newActiveChannel,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Can't Set Active
    @Comment("\nAppears when a player can't change their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSetActive = "<white>You can't set <channel_name> as your active channel!";

    public Component cantSetActive(String channelName, TextColor textColor) {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantSetActive,
                        TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</bold></color:" + textColor.asHexString() + ">"))))
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
                .append(MiniMessage.miniMessage().deserialize(crewChatInfoHeader,
                TagResolver.resolver(Placeholder.parsed("version", CrewChat.getInstance().getDescription().getVersion())))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelsLoaded + "\n", TagResolver.resolver(Placeholder.parsed("channels", String.valueOf(channelsLoaded)))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatPlayersLoaded + "\n", TagResolver.resolver(Placeholder.parsed("players", String.valueOf(playersLoaded)))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatOnlinePlayersLoaded + "\n", TagResolver.resolver(Placeholder.parsed("players", String.valueOf(onlinePlayersLoaded)))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatDiscordIntegration, TagResolver.resolver(Placeholder.parsed("enabled", discordIntegration ? "True" : "False")))))
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
                .append(Component.text("\n"))
                .append(MiniMessage.miniMessage().deserialize(channelInfoHeader + "\n", TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + color.toString() + ">" + name + "</color:" + color.toString() + ">"))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoName + "\n", TagResolver.resolver(Placeholder.parsed("channel_name", name))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoDescription + "\n", TagResolver.resolver(Placeholder.parsed("channel_description", description))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(channelInfoColor + "\n", TagResolver.resolver(Placeholder.parsed("channel_color", "<color:" + color + ">" + closestColor + " (" + color.asHexString() + ")"))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelInfoSubscribers + "\n", TagResolver.resolver(Placeholder.parsed("subscribers", String.valueOf(subscribers)))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelInfoAutoSubscribe + "\n", TagResolver.resolver(Placeholder.parsed("auto_subscribe", isAutoSubscribe ? "True" : "False"))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelInfoExcludeFromDiscord + "\n", TagResolver.resolver(Placeholder.parsed("exclude_from_discord", isExcludeFromDiscord ? "True" : "False"))))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelInfoShowChannelNameDiscord, TagResolver.resolver(Placeholder.parsed("show_channel_name_discord", isShowChannelNameDiscord ? "True" : "False"))))
                .build();
    }

    // ** Info Player **

    // Header
    private transient String crewChatInfoPlayerHeader = "<white>Player info for <bold><player></bold>:";

    public Component crewChatInfoPlayerHeader(String player) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatInfoPlayerHeader, TagResolver.resolver(Placeholder.parsed("player", player))))
                .build();
    }

    // Subscribed Channels Header
    private transient String crewChatChannelListHeader = "<gray>Subscribed channel(s):";

    public Component crewChatChannelListHeaderSmall() {
        return MiniMessage.miniMessage().deserialize(crewChatChannelListHeader);
    }

    // Active Channel
    private transient String crewChatActiveChannel = "<gray>Active channel: <active_channel>";

    public Component crewChatActiveChannel(String activeChannel, TextColor textColor) {
        return MiniMessage.miniMessage().deserialize(crewChatActiveChannel,
                TagResolver.resolver(Placeholder.parsed("active_channel", "<color:" + textColor.asHexString() + "><bold>" + activeChannel + "</color:" + textColor.asHexString() + "></bold><gray>")));
    }

    // Status
    private transient String crewChatStatus = "<gray>Status:<reset> <status>";

    public Component crewChatStatus(String status) {
        return MiniMessage.miniMessage().deserialize(crewChatStatus,
                TagResolver.resolver(Placeholder.parsed("status", MessageUtil.sanitizeMessageColor(status))));
    }

    // Mute Header
    private transient String crewChatMuteHeader = "<gray>Muted player(s):";

    public Component crewChatMuteHeader() {
        return MiniMessage.miniMessage().deserialize(crewChatMuteHeader);
    }

    // ** Set **

    // Channel No Exist
    private transient String crewChatChannelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component crewChatChannelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatChannelNoExist, TagResolver.resolver(Placeholder.parsed("channel_name", name))))
                .build();
    }

    // Property No Exist
    private transient String crewChatPropertyNoExist = "<white>Property <bold><property></bold> does not exist!";

    public Component crewChatPropertyNoExist(String property) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatPropertyNoExist, TagResolver.resolver(Placeholder.parsed("property", property))))
                .build();
    }
    
    // Value Incorrect
    private transient String crewChatValueIncorrect = "<white>Value <bold><value></bold> is incorrect!";

    public Component crewChatValueIncorrect(String value) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatValueIncorrect, TagResolver.resolver(Placeholder.parsed("value", value))))
                .build();
    }

    // Property Changed
    private transient String crewChatPropertyChanged = "<white>Channel <bold><channel></bold> property <bold><property></bold> has been changed to <bold><value></bold>.";

    public Component crewChatPropertyChanged(String channel, String property, String value) {
        return Component.text()
                .append(crewChatHeader)
                .append(MiniMessage.miniMessage().deserialize(crewChatPropertyChanged,
                TagResolver.resolver(Placeholder.parsed("channel", channel),
                        Placeholder.parsed("property", property),
                        Placeholder.parsed("value", value))))
                .build();
    }

    // ** Help **

    // Help
    public Component crewChatHelpCommand() {
        return Component.text()
                // Header
                .append(crewChatHeader)
                .append(Component.text("Command Help:", WHITE))
                .append(Component.text(" - Alias: /cc <args> - (Click to run) -", GRAY))
                .append(Component.text("\n"))
                // crewchat
                .append(Component.text("/crewchat", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/crewchat")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Base CrewChat command.", WHITE))
                .append(Component.text("\n"))
                // crewchat help
                .append(Component.text("/crewchat help", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(clickToRun)
                            .build()))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows this screen.", WHITE))
                .append(Component.text("\n"))
                // crewchat info
                .append(Component.text("/crewchat info", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.runCommand("/crewchat info")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows CrewChat general info.", WHITE))
                .append(Component.text("\n"))
                // crewchat info channel <channel>
                .append(Component.text("/crewchat info channel <channel>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/crewchat info channel ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows all information about specified channel.", WHITE))
                .append(Component.text("\n"))
                // crewchat info player <player>
                .append(Component.text("/crewchat info player <player>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(clickToRun)
                                .build()))
                        .clickEvent(ClickEvent.suggestCommand("/crewchat info player ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows all information about specified player.", WHITE))
                .append(Component.text("\n"))
                // crewchat reload
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

    // ** Debug **

    // Invalid Message
    private transient Component invalidMessage = Component.text()
            .append(Component.text("There is no message method with that name.", WHITE))
            .build();

    public Component invalidMessage() {
        return Component.text()
                .append(crewChatHeader)
                .append(invalidMessage)
                .build();
    }

    /*================================================================================

                                       Me Command

    ================================================================================*/

    public Component meMessage(String playerName, String message, TextColor textColor) {
        // * %playerName% %message% *
        return MiniMessage.miniMessage().deserialize("<" + textColor.asHexString() + "><italic>* " + playerName + " " + message + " *");
    }

    /*================================================================================

                                    Broadcast Command

    ================================================================================*/

    @Comment("\nThis is the format used before every chat message sent.")
    private String broadcastMessageHeader = "<gray>[<dark_green>Broadcast<gray>] <white>";

    public Component broadcastMessage(String message) {
        // [Broadcast] <message>
        return Component.text()
                .append(MiniMessage.miniMessage().deserialize(broadcastMessageHeader))
                .append(Component.text(message))
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
    private transient Component partyHeader = MiniMessage.miniMessage().deserialize("<gray>[<dark_green>" + WordUtils.capitalize(party) + "<gray>] ");

    // Party Exists
    @Comment("\nAppears if a party already exists during creation.\n" +
            "Possible tags: <party>")
    private String partyAlreadyExists = "<white>Party <party_name> already exists!";

    public Component partyAlreadyExists(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyAlreadyExists,
                TagResolver.resolver(Placeholder.parsed("party_name", "<" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Channel Exists
    @Comment("\nAppears if a channel already exists with desired party name during creation.\n" +
            "Possible tags: <channel_name>")
    private String partyChannelAlreadyExists = "<white>Channel <channel_name> already exists with that name!";

    public Component partyChannelAlreadyExists(String channelName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyChannelAlreadyExists,
                TagResolver.resolver(Placeholder.parsed("channel_name", "<color:" + textColor.asHexString() + "><bold>" + channelName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Party No Exist
    @Comment("\nAppears when joining a party that doesn't exist.\n" +
            "Possible tags: <party_name>")
    private String partyNoExist = "<white>Party <bold><party_name></bold> doesn't exist!";

    public Component partyNoExist(String partyName) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyNoExist, TagResolver.resolver(Placeholder.parsed("party_name", partyName))))
                .build();
    }

    // Party will be created
    @Comment("\nAppears when creating a party.\n" +
            "Possible tags: <party_name>")
    private String partyWillBeCreated = "<white>Party <bold><party_name></bold> will be created.";

    // Color picker
    @Comment("\nAppears above the party color picker.\n" +
            "Possible tags: <hex_color>")
    private String pickAColor = "<white>Please choose a color <hex_color>:";

    // Hex
    @Comment("\nAppears above the party color picker.")
    private String hexColor = "<grey>(or enter #hex)";

    // Click to select
    @Comment("\nAppears when hovering over the color picker.")
    private String clickToPick = "<dark_green><bold>Click</bold> <white>to select this color.";

    // Preview
    @Comment("\nValue for the word \"preview\".\n" +
            "(Does not accept color codes.)")
    private String preview = "preview";

    // Pick a Color Swatch Hover Text
    private HoverEvent<Component> pickAColorHover(String partyName, TextColor textColor) {
        return HoverEvent.showText(Component.text()
                .append(Component.text(WordUtils.capitalize(preview) + ": ", WHITE))
                .append(Component.text(partyName, textColor, BOLD))
                .append(Component.text("\n"))
                .append(MiniMessage.miniMessage().deserialize(clickToPick))
                .build());
    }

    // Color Palette
    public Component pickAColor(String partyName) {
        Component message = Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyWillBeCreated, TagResolver.resolver(Placeholder.parsed("party_name", partyName))))
                .append(Component.text("\n"))
                .append(MiniMessage.miniMessage().deserialize(pickAColor,
                        TagResolver.resolver(Placeholder.component("hex_color", MiniMessage.miniMessage().deserialize(hexColor))))
                        .hoverEvent(HoverEvent.showText(clickToRun))
                        .clickEvent(ClickEvent.suggestCommand("/party create " + partyName + " #")))
                .append(Component.text("\n"))
                .build();

        // Determine version
        String version = Bukkit.getVersion();
        int start = version.indexOf("MC: ") + 4;
        int end = version.length() - 1;
        version = version.substring(start, end);

        // Show all colors if version 1.16 or higher
        if (Versions.versionCompare("1.16.0", version) < 0) {
            // Color squares
            for (float value = 1.0f; value >= 0.5f; value -= 0.5f) {
                for (float hue = 0.0f; hue <= 1.0f; hue += 0.05f) {
                    message = message.append(Component.text("█")
                            .color(TextColor.color(HSVLike.hsvLike(hue, 1.0f, value)))
                            .hoverEvent(pickAColorHover(partyName, TextColor.color(HSVLike.hsvLike(hue, 1.0f, value))))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + TextColor.color(HSVLike.hsvLike(hue, 1.0f, value)).asHexString())));
                }
                if (value == 1.0f) {
                    message = message.append(Component.text("█")
                            .color(GRAY)
                            .hoverEvent(pickAColorHover(partyName, GRAY))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + GRAY.asHexString())));
                    message = message.append(Component.text("█\n")
                            .color(WHITE)
                            .hoverEvent(pickAColorHover(partyName, WHITE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + WHITE.asHexString())));
                } else if (value == 0.5f) {
                    message = message.append(Component.text("█")
                            .color(DARK_GRAY)
                            .hoverEvent(pickAColorHover(partyName, DARK_GRAY))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_GRAY.asHexString())));
                    message = message.append(Component.text("█", BLACK)
                            .hoverEvent(pickAColorHover(partyName, BLACK))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + BLACK.asHexString())));
                }
            }
        }
        // Show named colors
        else {
            message = Component.text()
                    .append(message)
                    .append(Component.text("█", DARK_RED)
                            .hoverEvent(pickAColorHover(partyName, DARK_RED))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_RED.asHexString())))
                    .append(Component.text("█", RED)
                            .hoverEvent(pickAColorHover(partyName, RED))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + RED.asHexString())))
                    .append(Component.text("█", GOLD)
                            .hoverEvent(pickAColorHover(partyName, GOLD))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + GOLD.asHexString())))
                    .append(Component.text("█", YELLOW)
                            .hoverEvent(pickAColorHover(partyName, YELLOW))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + YELLOW.asHexString())))
                    .append(Component.text("█", GREEN)
                            .hoverEvent(pickAColorHover(partyName, GREEN))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + GREEN.asHexString())))
                    .append(Component.text("█", DARK_GREEN)
                            .hoverEvent(pickAColorHover(partyName, DARK_GREEN))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_GREEN.asHexString())))
                    .append(Component.text("█", AQUA)
                            .hoverEvent(pickAColorHover(partyName, AQUA))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + AQUA.asHexString())))
                    .append(Component.text("█", DARK_AQUA)
                            .hoverEvent(pickAColorHover(partyName, DARK_AQUA))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_AQUA.asHexString())))
                    .append(Component.text("█", BLUE)
                            .hoverEvent(pickAColorHover(partyName, BLUE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + BLUE.asHexString())))
                    .append(Component.text("█", DARK_BLUE)
                            .hoverEvent(pickAColorHover(partyName, DARK_BLUE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_BLUE.asHexString())))
                    .append(Component.text("█", LIGHT_PURPLE)
                            .hoverEvent(pickAColorHover(partyName, LIGHT_PURPLE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + LIGHT_PURPLE.asHexString())))
                    .append(Component.text("█", DARK_PURPLE)
                            .hoverEvent(pickAColorHover(partyName, DARK_PURPLE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_PURPLE.asHexString())))
                    .append(Component.text("█", WHITE)
                            .hoverEvent(pickAColorHover(partyName, WHITE))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + WHITE.asHexString())))
                    .append(Component.text("█", GRAY)
                            .hoverEvent(pickAColorHover(partyName, GRAY))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + GRAY.asHexString())))
                    .append(Component.text("█", DARK_GRAY)
                            .hoverEvent(pickAColorHover(partyName, DARK_GRAY))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + DARK_GRAY.asHexString())))
                    .append(Component.text("█", BLACK)
                            .hoverEvent(pickAColorHover(partyName, BLACK))
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + BLACK.asHexString())))
                    .build();
        }
        return message;
    }

    // Invalid color
    @Comment("\nAppears when specifying an invalid hex color for a party.\n" +
            "Possible tags: <hex_color>")
    private String invalidColor = "<white>Color \"<hex_color>\" is invalid!";

    public Component invalidColor(String invalidHex) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(invalidColor, TagResolver.resolver(Placeholder.parsed("hex_color", invalidHex))))
                .build();
    }

    // Party Created
    @Comment("\nAppears when a party is successfully created.\n" +
            "Possible tags: <party_name>")
    private String partyCreated = "<white>Party <party_name> has been created successfully.";

    public Component partyCreated(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyCreated,
                        TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Party Joined
    @Comment("\nAppears when joining a party.\n" +
            "Possible tags: <party_name>")
    private String partyJoined = "<white>You have joined <party_name>.";

    public Component partyJoined(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyJoined,
                TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Already in Party
    @Comment("\nAppears when trying to join a party a player is already in.\n" +
            "Possible tags: <party_name>")
    private String alreadyInParty = "<white>You are already in <party_name>!";

    public Component alreadyInParty(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(alreadyInParty,
                        TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Party Left
    @Comment("\nAppears when leaving a party.\n" +
            "Possible tags: <party_name>")
    private String partyLeft = "<white>You have left <party_name>.";

    public Component partyLeft(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyLeft,
                        TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Not in Party
    @Comment("\nAppears when trying to leave a party a player is not in.\n" +
            "Possible tags: <party_name>")
    private String notInParty = "<white>You are not in <party_name>!";

    public Component notInParty(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(notInParty,
                        TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</color:" + textColor.asHexString() + "></bold>"))))
                .build();
    }

    // Player Joined Party
    @Comment("\nAppears when another player joins a party.\n" +
            "Possible tags: <player_name>, <party_name>")
    private String playerJoinedParty = "<white><player_name> has joined <party_name>.";

    public Component playerJoinedParty(String prefix, String playerName, String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(playerJoinedParty,
                        TagResolver.resolver(Placeholder.parsed("player_name", prefix + playerName),
                                Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    // Player Joined Party
    @Comment("\nAppears when another player leaves a party.\n" +
            "Possible tags: <player_name>, <party_name>")
    private String playerLeftParty = "<white><player_name> has left <party_name>.";

    public Component playerLeftParty(String prefix, String playerName, String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(playerLeftParty,
                        TagResolver.resolver(Placeholder.parsed("player_name", prefix + playerName),
                                Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    @Comment("\nAppears in the party player list command.\n" +
            "Possible tags: <party_name>")
    private String partyPlayerListHeader = "<white>List of players in <party_name>:";

    public Component partyPlayerListHeader(String partyName, TextColor textColor) {
        return Component.text()
                .append(partyHeader)
                .append(MiniMessage.miniMessage().deserialize(partyPlayerListHeader,
                        TagResolver.resolver(Placeholder.parsed("party_name", "<color:" + textColor.asHexString() + "><bold>" + partyName + "</bold></color:" + textColor.asHexString() + ">"))))
                .build();
    }

    public Component partyPlayerListEntry(String prefix, String playerName) {
        return Component.text()
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(prefix + playerName))
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
                .append(MiniMessage.miniMessage().deserialize(privateMessageHeader,
                TagResolver.resolver(TagResolver.resolver("sender_prefix", Tag.inserting(MessageUtil.legacyDeserialize(senderPrefix))),
                        Placeholder.parsed("sender_name", "<hover:show_text:'<white>" + time + "\n" + senderStatus + "'>" + WordUtils.capitalize(you)),
                        TagResolver.resolver("recipient_prefix", Tag.inserting(MessageUtil.legacyDeserialize(recipientPrefix))),
                        Placeholder.parsed("recipient_name", "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>" + recipientName))))
                .append(MessageUtil.parsePrivateMessage(message))
                .build();
    }

    // Click to reply
    @Comment("\nAppears when hovering over private messages.")
    private String clickToReply = "<bold><dark_green>Click</bold><white> here to reply.";

    public Component privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                           String senderStatus, String recipientStatus, String time, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return Component.text()
                .append(MiniMessage.miniMessage().deserialize(privateMessageHeader,
                TagResolver.resolver(TagResolver.resolver("sender_prefix", Tag.inserting(MessageUtil.legacyDeserialize(senderPrefix))),
                        Placeholder.parsed("sender_name", "<hover:show_text:'<white>" + time + "\n" + senderStatus + "\n" + clickToReply + "'><click:suggest_command:/msg " + senderName + " >" + senderName),
                        TagResolver.resolver("recipient_prefix", Tag.inserting(MessageUtil.legacyDeserialize(recipientPrefix))),
                        Placeholder.parsed("recipient_name", "<hover:show_text:'<white>" + time + "\n" + recipientStatus + "'>" + WordUtils.capitalize(you)))))
                .append(MessageUtil.parsePrivateMessage(message))
                .build();
    }

    // PM Can't Message Self
    @Comment("\nAppears when a player tries to private message themself.")
    private String cantMessageSelf = "<white>You can't send a message to yourself!";

    public Component cantMessageSelf() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(cantMessageSelf))
                .build();
    }

    // PM No PM Received
    @Comment("\nAppears when a player tries to reply without receiving a private message.")
    private String noPMReceived = "<white>You haven't received any messages!";

    public Component noPMReceived() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(noPMReceived))
                .build();
    }

    // PM Player Doesn't Exist
    @Comment("\nAppears when a player tries to private message a player that doesn't exist.")
    private String playerNoExist = "<white>Player doesn't exist!";

    public Component playerNoExist() {
        return Component.text()
                .append(chatHeader)
                .append(MiniMessage.miniMessage().deserialize(playerNoExist))
                .build();
    }

    /*================================================================================

                                     Chat Messages

    ================================================================================*/

    // Chat Message
    @Comment("\nThis is the format used before every chat message sent.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String chatMessageHeader = "<player_prefix><player_name><gray>: ";

    public Component chatMessage(String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor, boolean showChannelName, boolean isParty) {
        // %prefix%%playerName%: %message%

        TagResolver placeholders = TagResolver.resolver(
                TagResolver.resolver("player_prefix", Tag.inserting(MessageUtil.legacyDeserialize(prefix))),
                Placeholder.parsed("player_name", playerName));

        return Component.text()
                // Message header
                .append(MiniMessage.miniMessage().deserialize(
                        // Click action
                        "<click:suggest_command:/msg " + playerName + " >" +
                        // * Hover text
                        // Message sent time
                        "<hover:show_text:'<white>" + time + "\n" +
                        // Message sender status
                        WordUtils.capitalize(this.status) + ": " + MessageUtil.sanitizeMessageColor(MessageUtil.escapeSingleQuotes(status)) + "<reset>\n" +
                        // Message sender channel or party
                        (isParty ? WordUtils.capitalize(party) : WordUtils.capitalize(this.channel)) + ": " + "<" + textColor.asHexString() + ">" + activeChannel + "'>" +
                        // Channel shown in prefix
                        (showChannelName ? "<gray>[</gray><" + textColor.asHexString() + ">" + activeChannel + "</color:" + textColor.asHexString() + "><gray>]</gray> " : "") +
                        // Header
                        chatMessageHeader + "<reset><" + textColor.asHexString() + ">",
                        placeholders))
                // Message body
                .append(message)
                .build();
    }

    // Discord Message
    @Comment("\nThis is the format used before every Discord message sent.\n" +
            "Possible tags: <discord>, <player_prefix>, <player_name>")
    private String discordMessageHeader = "<gray>[<color:#7289DA><discord><gray>] <player_prefix><player_name><gray>: ";

    public Component discordMessage(String discordHeader, String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {
        // [Discord] %prefix%%playerName%: %message%

        TagResolver placeholders = TagResolver.resolver(
                Placeholder.parsed("discord", discordHeader),
                Placeholder.parsed("player_prefix", prefix),
                Placeholder.parsed("player_name", playerName));

        return Component.text()
                // Message header
                .append(MiniMessage.miniMessage().deserialize(
                        // * Hover text
                        // Message sent time
                        "<hover:show_text:'<white>" + time + "<newline>" +
                        // Message sender status
                        WordUtils.capitalize(this.status) + ": " + MessageUtil.escapeSingleQuotes(status) + "<newline>" +
                        // Message channel
                        WordUtils.capitalize(this.channel) + ": " + "<" + textColor.asHexString() + ">" + activeChannel + "'>" +
                        // Header
                        discordMessageHeader + "</hover>",
                        placeholders))
                // Message body
                .append(message)
                .build();
    }

    /*================================================================================

                                        General

    ================================================================================*/

    // General fields used in many strings
    @Comment("\n\"Chat\" text that appears before many messages.\n" +
            "(Does not accept color codes)")
    private String chat = "chat";

    @Comment("\nValue for the word \"Status\".\n" +
            "(Does not accept color codes)")
    private String status = "status";

    @Comment("\nValue for the word \"Channel\".\n" +
            "(Does not accept color codes)")
    private String channel = "channel";

    @Comment("\nValue for the word \"You\".\n" +
            "(Does not accept color codes)")
    private String you = "you";

    @Comment("\nValue for the word \"Help\".\n" +
            "(Does not accept color codes)")
    private String help = "help";

    // [Chat]
    private transient Component chatHeader = Component.text()
            .append(Component.text("[", GRAY))
            .append(Component.text(WordUtils.capitalize(chat), DARK_GREEN))
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
                .append(MiniMessage.miniMessage().deserialize(pluginNotConfigured))
                .append(Component.text("\n"))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(permError))
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
        return MiniMessage.miniMessage().deserialize(noPermission);
    }
}