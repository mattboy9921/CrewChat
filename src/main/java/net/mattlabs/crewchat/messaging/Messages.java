package net.mattlabs.crewchat.messaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
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
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

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
        return chatHeader.append(MiniMessage.miniMessage().deserialize(welcomeToChat))
                .append(Component.text(" [" + WordUtils.capitalize(help) + "]", BLUE, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(MiniMessage.miniMessage().deserialize(clickForHelp))))
                        .clickEvent(ClickEvent.runCommand("/chat help")));
    }

    // ** Deafen **

    // Player Deafened
    @Comment("\nAppears when a player deafens themself.")
    private String playerDeafened = "<white>You have been deafened. You will not receive any chat messages.";

    public Component playerDeafened() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerDeafened));
    }

    // Player Undeafened
    @Comment("\nAppears when a player undeafens themself.")
    private String playerUndeafened = "<white>You are no longer deafened. You will receive all chat messages.";

    public Component playerUndeafened() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerUndeafened));
    }

    // Player Is Deafened
    @Comment("\nAppears when a player tries to send a chat message while deafened.")
    private String playerIsDeafened = "<white>You are deafened and cannot see chat messages!";

    public Component playerIsDeafened() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerIsDeafened));
    }

    // ** General **

    // Channel No Exist
    @Comment("\nAppears when a command contains a channel that doesn't exist.\n" +
            "Possible tags: <channel_name>")
    private String channelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component channelNoExist(String name) {
        return chatHeader
                .append(MiniMessage.miniMessage().deserialize(channelNoExist, Placeholder.unparsed("channel_name", name)));
    }

    // ** Help **
    public Component chatHelpCommand() {
        // Header
        return chatHeader.append(Component.text("Command Help:", WHITE))
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
                .append(Component.text("Provides a list of players/Discord accounts to mention.", WHITE));
    }

    // ** Info **

    // Channel List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListHeader = "Channel List: (Click for more info)";

    public Component channelListHeader() {
        return Component.text("------------------------", GRAY)
                .append(Component.text("[", DARK_GRAY))
                .append(Component.text(WordUtils.capitalize(chat), DARK_GREEN))
                .append(Component.text("]", DARK_GRAY))
                .append(Component.text("------------------------", GRAY))
                .append(Component.text("\n"))
                .append(Component.text(channelListHeader, GRAY));
    }

    // Party List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListHeader = "Party List:";

    public Component partyListHeader() {
        return Component.text(partyListHeader, GRAY);
    }

    // Channel List Active
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListActive = "You are currently active in: <channel_name>.";

    public Component channelListActive(String channelName, TextColor textColor) {
        return MessageUtil.getSerNothingMM().deserialize(channelListActive,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD)))
                .color(GRAY);
    }

    // Channel List Subscribe Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String channelListSubscribedHeader = "Your subscribed channels are:";

    public Component channelListSubscribedHeader() {
        return Component.text(channelListSubscribedHeader, GRAY);
    }

    // Party List Joined Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String partyListJoinedHeader = "Your joined parties are:";

    public Component partyListJoinedHeader() {
        return Component.text(partyListJoinedHeader, GRAY);
    }

    // Muted List Header
    @Comment("\nAppears in the chat info command.\n" +
            "(Does not accept color codes)")
    private String mutedListHeader = "Your muted players are:";

    public Component mutedListHeader() {
        return Component.text(mutedListHeader, GRAY);
    }

    // Click to unmute
    @Comment("\nAppears in the chat info command.")
    private String clickToUnmute = "<bold><dark_green>Click</bold></dark_green> <white>to unmute.";


    // Time remaining
    @Comment("\nAppears in the chat info command.\n" +
            "Possible tags: <time_remaining>")
    private String timeRemaining = "<white>Time remaining: <time_remaining>";

    public Component mutedListEntry(String player, String timeRemaining) {
        return hyphenHeader.append(Component.text(player, WHITE)
                        .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize(
                                this.timeRemaining + "\n" + clickToUnmute, Placeholder.unparsed("time_remaining", timeRemaining)))))
                        .clickEvent(ClickEvent.runCommand("/chat unmute " + player));
    }

    // Channel List Entry
    public Component channelListEntry(String name, TextColor textColor) {
        return Component.text(" - ", DARK_GREEN)
                .append(Component.text(name, textColor, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" for more info.", WHITE))))
                        .clickEvent(ClickEvent.runCommand("/chat info channel " + name)));
    }

    // Party List Entry
    public Component partyListEntry(String name, TextColor textColor) {
        return Component.text(" - ", DARK_GREEN)
                .append(Component.text(name, textColor, BOLD));
    }

    // ** Info Channel **

    public Component channelInfo(String channelName, String description, TextColor textColor) {

        // Get closest color to hex code, remove underscores, capitalize first letter of each word
        String closestColor = nearestTo(textColor).toString();
        closestColor = closestColor.replaceAll("_", " ");
        closestColor = WordUtils.capitalize(closestColor);

        return chatHeader
                .append(Component.text("Channel ", WHITE))
                .append(Component.text(channelName, textColor, BOLD))
                .append(Component.text(" info:\n", WHITE))
                .append(hyphenHeader)
                .append(Component.text("Name: " + channelName + "\n", WHITE))
                .append(hyphenHeader)
                .append(Component.text("Description: " + description + "\n", WHITE))
                .append(hyphenHeader)
                .append(Component.text("Color: ", WHITE))
                .append(Component.text(closestColor + " (" + textColor.asHexString() + ")", textColor));
    }

    // ** Mute/Unmute **

    // Can't Mute Self
    @Comment("\nAppears when a player tries to mute themself.")
    private String cantMuteSelf = "<white>You can't mute yourself!";

    public Component cantMuteSelf() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantMuteSelf));
    }

    // Can't Unmute Self
    @Comment("\nAppears when a player tries to unmute themself.")
    private String cantUnmuteSelf = "<white>You can't unmute yourself!";

    public Component cantUnmuteSelf() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantUnmuteSelf));
    }

    // Player Already Muted
    @Comment("\nAppears when a player tries to mute someone they've already muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyMuted = "<white><player_prefix><player_name><white> is already muted!";

    public Component playerAlreadyMuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerAlreadyMuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.unparsed("player_name", playerName))));
    }

    // Player Already Unmuted
    @Comment("\nAppears when a player tries to unmute someone who isn't muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyUnmuted = "<white><player_prefix><player_name><white> is not muted!";

    public Component playerAlreadyUnmuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerAlreadyUnmuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.unparsed("player_name", playerName))));
    }

    // Player Muted
    @Comment("\nAppears when a player mutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerMuted = "<white><player_prefix><player_name><white> has been muted.";

    public Component playerMuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerMuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.unparsed("player_name", player))));
    }

    // Player Unmuted
    @Comment("\nAppears when a player unmutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerUnmuted = "<white><player_prefix><player_name><white> has been unmuted.";

    public Component playerUnmuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerUnmuted,
                TagResolver.resolver(Placeholder.parsed("player_prefix", playerPrefix),
                        Placeholder.unparsed("player_name", player))));
    }

    // ** Status **

    // Status Set
    @Comment("\nAppears when a player sets their status.\n" +
            "Possible tags: <status>")
    private String statusSet = "<white>Your status has been set to: \"<status>\".";

    public Component statusSet(String status) throws ParsingException {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(statusSet,
                        Placeholder.component("status", MessageUtil.getSafeMM().deserialize(status))));
    }

    // Current Status
    @Comment("\nAppears when a player checks their status.\n" +
            "Possible tags: <status>")
    private String statusIs = "<white>Your status is: \"<status>\".";

    public Component statusIs(String status) throws ParsingException {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(statusIs,
                        Placeholder.component("status", MessageUtil.getSafeMM().deserialize(status))));
    }

    // Status Syntax Error
    @Comment("\nAppears if a player's status contains a syntax error.")
    private String statusSyntaxError = "<white>Your status could not be set. Please check the syntax.";

    public Component statusSyntaxError() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(statusSyntaxError));
    }

    // ** Subscribe/Unsubscribe **

    // Already Subscribed
    @Comment("\nAppears when a player tries to subscribe to a channel they already subscribe to.\n" +
            "Possibe tags: <channel_name>")
    private String alreadySubscribed = "<white>You are already subscribed to <channel_name>.";

    public Component alreadySubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(alreadySubscribed,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Can't Subscribe
    @Comment("\nAppears when a player can't subscribe to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSubscribe = "<white>You can't subscribe to <channel_name>!";

    public Component cantSubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantSubscribe,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Can't Unsubscribe
    @Comment("\nAppears when a player can't unsubscribe from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribe = "<white>You can't unsubscribe from <channel_name>!";

    public Component cantUnsubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantUnsubscribe,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Can't Unsubscribe Active
    @Comment("\nAppears when a player can't unsubscribe from a channel because it is their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribeActive = "<white>You can't unsubscribe from <channel_name>, it is your active channel!";

    public Component cantUnsubscribeActive(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantUnsubscribeActive,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Not Subscribed
    @Comment("\nAppears when a player unsubscribes from a channel they are not subscribed to.\n" +
            "Possibe tags: <channel_name>")
    private String notSubscribed = "<white>You aren't subscribed to <channel_name>.";

    public Component notSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(notSubscribed,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Now Subscribed
    @Comment("\nAppears when a player subscribes to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowSubscribed = "<white>You are now subscribed to <channel_name>.";

    public Component nowSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(nowSubscribed,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Now Unsubscribed
    @Comment("\nAppears when a player unsubscribes from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowUnsubscribed = "<white>You are no longer subscribed to <channel_name>.";

    public Component nowUnsubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(nowUnsubscribed,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // ** Switch **

    // New Active Channel
    @Comment("\nAppears when a player changes their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String newActiveChannel = "<white>Your active channel is now <channel_name>.";

    public Component newActiveChannel(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(newActiveChannel,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
    }

    // Can't Set Active
    @Comment("\nAppears when a player can't change their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSetActive = "<white>You can't set <channel_name> as your active channel!";

    public Component cantSetActive(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantSetActive,
                        Placeholder.component("channel_name", Component.text(channelName, textColor, BOLD))));
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
        return crewChatHeader
                .append(Component.text("Version " +
                    CrewChat.getInstance().getDescription().getVersion() +
                    ". For help, click ", WHITE))
                .append(Component.text("[Help]", BLUE, BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text()
                            .append(Component.text("Click", DARK_GREEN, BOLD))
                            .append(Component.text(" here for help.", WHITE))))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")));
    }

    // ** Info **

    // Info
    public Component crewChatInfo(int channelsLoaded, int playersLoaded, int onlinePlayersLoaded, boolean discordIntegration) {
        // Header and version
        return crewChatHeader
                .append(Component.text("Version ", WHITE))
                .append(Component.text(CrewChat.getInstance().getDescription().getVersion(), WHITE, BOLD))
                .append(Component.text(":\n", WHITE))
                // Channels
                .append(hyphenHeader)
                .append(Component.text(channelsLoaded, WHITE, BOLD))
                .append(Component.text(" channel(s) loaded.\n", WHITE))
                // Players
                .append(hyphenHeader)
                .append(Component.text(playersLoaded, WHITE, BOLD))
                .append(Component.text(" player(s) loaded.\n", WHITE))
                // Online players
                .append(hyphenHeader)
                .append(Component.text(onlinePlayersLoaded, WHITE, BOLD))
                .append(Component.text(" online player(s) loaded.\n", WHITE))
                // Discord integration
                .append(hyphenHeader)
                .append(Component.text("Discord integration enabled: ", WHITE))
                .append(Component.text(discordIntegration ? "True" : "False", WHITE, BOLD));
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
        return Component.text(" - ", DARK_GREEN)
                .append(Component.text(name, textColor, BOLD)
                        .hoverEvent(HoverEvent.showText(Component.text()
                                .append(Component.text("Click", DARK_GREEN, BOLD))
                                .append(Component.text(" for more info.", WHITE))))
                        .clickEvent(ClickEvent.runCommand("/crewchat info channel " + name)));
    }

    // Channel Info
    public Component crewChatChannelInfo(String name, String description, TextColor color, int subscribers, boolean isAutoSubscribe, boolean isExcludeFromDiscord, boolean isShowChannelNameDiscord) {

        // Get closest color to hex code, remove underscores, capitalize first letter of each word
        String closestColor = nearestTo(color).toString();
        closestColor = closestColor.replaceAll("_", " ");
        closestColor = WordUtils.capitalize(closestColor);

        // Header
        return crewChatHeader
                .append(Component.text("Channel ", WHITE))
                .append(Component.text(name, color, BOLD))
                .append(Component.text(" info:\n", WHITE))
                // Name
                .append(hyphenHeader)
                .append(Component.text("Name: ", WHITE))
                .append(Component.text(name + "\n", WHITE))
                // Description
                .append(hyphenHeader)
                .append(Component.text("Description: ", WHITE))
                .append(Component.text(description + "\n", WHITE))
                // Color
                .append(hyphenHeader)
                .append(Component.text("Color: ", WHITE))
                .append(Component.text(closestColor + " (" + color.asHexString() + ")\n", color))
                // Subscribers
                .append(hyphenHeader)
                .append(Component.text(String.valueOf(subscribers), WHITE, BOLD))
                .append(Component.text(" subscriber(s).\n", WHITE))
                .append(hyphenHeader)
                // Auto-Subscribe
                .append(Component.text("Auto-Subscribe enabled: ", WHITE))
                .append(Component.text(isAutoSubscribe ? "True\n" : "False\n", WHITE, BOLD))
                // Exclude from Discord
                .append(hyphenHeader)
                .append(Component.text("Exclude from Discord enabled: ", WHITE))
                .append(Component.text(isExcludeFromDiscord ? "True\n" : "False\n", WHITE, BOLD))
                // Show channel name on Discord
                .append(hyphenHeader)
                .append(Component.text("Show Channel Name on Discord enabled: ", WHITE))
                .append(Component.text(isShowChannelNameDiscord ? "True" : "False", WHITE, BOLD));
    }

    // ** Info Player **

    public Component crewChatInfoPlayerHeader(String player) {
        return crewChatHeader
                .append(Component.text("Player info for: ", WHITE))
                .append(Component.text(player, WHITE, BOLD));
    }

    public Component crewChatChannelListHeaderSmall() {
        return Component.text("Subscribed channel(s):", GRAY);
    }

    public Component crewChatActiveChannel(String activeChannel, TextColor textColor) {
        return Component.text("Active channel: ", GRAY)
                .append(Component.text(activeChannel, textColor, BOLD));
    }

    // Status
    public Component crewChatStatus(String status) {
        return Component.text("Status: ", GRAY)
                .append(MessageUtil.getSafeMM().deserialize(status));
    }

    // Mute Header
    public Component crewChatMuteHeader() {
        return Component.text("Muted player(s):", GRAY);
    }

    // ** Set **

    // Channel No Exist
    public Component crewChatChannelNoExist(String name) {
        return crewChatHeader
                .append(Component.text("Channel ", WHITE))
                .append(Component.text(name, WHITE, BOLD))
                .append(Component.text(" doesn't exist!", WHITE));
    }

    // Property No Exist
    public Component crewChatPropertyNoExist(String property) {
        return crewChatHeader
                .append(Component.text("Property ", WHITE))
                .append(Component.text(property, WHITE, BOLD))
                .append(Component.text(" does not exist!", WHITE));
    }
    
    // Value Incorrect
    public Component crewChatValueIncorrect(String value) {
        return crewChatHeader
                .append(Component.text("Value ", WHITE))
                .append(Component.text(value, WHITE, BOLD))
                .append(Component.text(" is incorrect!", WHITE));
    }

    // Property Changed
    public Component crewChatPropertyChanged(String channel, String property, String value) {
        return crewChatHeader
                .append(Component.text("Channel ", WHITE))
                .append(Component.text(channel, WHITE, BOLD))
                .append(Component.text(" property ", WHITE))
                .append(Component.text(property, WHITE, BOLD))
                .append(Component.text(" has been changed to ", WHITE))
                .append(Component.text(value, WHITE, BOLD))
                .append(Component.text(".", WHITE));
    }

    // ** Help **

    // Help
    public Component crewChatHelpCommand() {
        // Header
        return crewChatHeader
                .append(Component.text("Command Help:", WHITE))
                .append(Component.text(" - Alias: /cc <args> - (Click to run) -", GRAY))
                .append(Component.text("\n"))
                // crewchat
                .append(Component.text("/crewchat", DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Base CrewChat command.", WHITE))
                .append(Component.text("\n"))
                // crewchat help
                .append(Component.text("/crewchat help", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows this screen.", WHITE))
                .append(Component.text("\n"))
                // crewchat info
                .append(Component.text("/crewchat info", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(clickToRun))
                        .clickEvent(ClickEvent.runCommand("/crewchat info")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows CrewChat general info.", WHITE))
                .append(Component.text("\n"))
                // crewchat info channel <channel>
                .append(Component.text("/crewchat info channel <channel>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(clickToRun))
                        .clickEvent(ClickEvent.suggestCommand("/crewchat info channel ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows all information about specified channel.", WHITE))
                .append(Component.text("\n"))
                // crewchat info player <player>
                .append(Component.text("/crewchat info player <player>", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(clickToRun))
                        .clickEvent(ClickEvent.suggestCommand("/crewchat info player ")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Shows all information about specified player.", WHITE))
                .append(Component.text("\n"))
                // crewchat reload
                .append(Component.text("/crewchat reload", DARK_GREEN)
                        .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat reload")))
                .append(Component.text(" - ", GRAY))
                .append(Component.text("Reload CrewChat configuration files.", WHITE));
    }

    // ** Reload **

    // Config Reloaded
    public Component configReloaded() {
        return crewChatHeader.append(Component.text("Configuration reloaded.", WHITE));
    }

    // ** Debug **

    // Invalid Message
    public Component invalidMessage() {
        return crewChatHeader.append(Component.text("There is no message method with that name.", WHITE));
    }

    /*================================================================================

                                       Me Command

    ================================================================================*/

    public Component meMessage(String playerName, String message, TextColor textColor) {
        return Component.text("* " + playerName + " " + message + " *", textColor, ITALIC);
    }

    /*================================================================================

                                    Broadcast Command

    ================================================================================*/

    @Comment("\nThis is the format used before every chat message sent.")
    private String broadcastMessageHeader = "<gray>[<dark_green>Broadcast<gray>] <white>";

    public Component broadcastMessage(String message) {
        return MiniMessage.miniMessage().deserialize(broadcastMessageHeader).append(Component.text(message));
    }

    /*================================================================================

                                     Party Command

    ================================================================================*/

    // Party
    @Comment("\nText for the word \"party\"\n" +
            "(Does not accept color codes.")
    private String party = "party";

    // Party Header
    private transient Component partyHeader = Component.text("[", GRAY)
            .append(Component.text(WordUtils.capitalize(party), DARK_GREEN))
            .append(Component.text("] ", GRAY));

    // Party Exists
    @Comment("\nAppears if a party already exists during creation.\n" +
            "Possible tags: <party>")
    private String partyAlreadyExists = "<white>Party <party_name> already exists!";

    public Component partyAlreadyExists(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyAlreadyExists,
                TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Channel Exists
    @Comment("\nAppears if a channel already exists with desired party name during creation.\n" +
            "Possible tags: <channel_name>")
    private String partyChannelAlreadyExists = "<white>Channel <channel_name> already exists with that name!";

    public Component partyChannelAlreadyExists(String channelName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyChannelAlreadyExists,
                TagResolver.resolver("channel_name", Tag.inserting(Component.text(channelName, textColor, BOLD)))));
    }

    // Party No Exist
    @Comment("\nAppears when joining a party that doesn't exist.\n" +
            "Possible tags: <party_name>")
    private String partyNoExist = "<white>Party <bold><party_name></bold> doesn't exist!";

    public Component partyNoExist(String partyName) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyNoExist,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, Style.style(BOLD))))));
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
        return HoverEvent.showText(Component.text(WordUtils.capitalize(preview) + ": ", WHITE)
                .append(Component.text(partyName, textColor, BOLD))
                .append(Component.text("\n"))
                .append(MiniMessage.miniMessage().deserialize(clickToPick)));
    }

    // Color Palette
    public Component pickAColor(String partyName) {
        Component message = partyHeader
                .append(MiniMessage.miniMessage().deserialize(partyWillBeCreated, Placeholder.unparsed("party_name", partyName)))
                .append(Component.text("\n"))
                .append(MiniMessage.miniMessage().deserialize(pickAColor,
                        TagResolver.resolver(Placeholder.component("hex_color", MiniMessage.miniMessage().deserialize(hexColor))))
                        .hoverEvent(HoverEvent.showText(clickToRun))
                        .clickEvent(ClickEvent.suggestCommand("/party create " + partyName + " #")))
                .append(Component.text("\n"));

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
            message = message
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
                            .clickEvent(ClickEvent.runCommand("/party create " + partyName + " " + BLACK.asHexString())));
        }
        return message;
    }

    // Invalid color
    @Comment("\nAppears when specifying an invalid hex color for a party.\n" +
            "Possible tags: <hex_color>")
    private String invalidColor = "<white>Color \"<hex_color>\" is invalid!";

    public Component invalidColor(String invalidHex) {
        return partyHeader
                .append(MiniMessage.miniMessage().deserialize(invalidColor, Placeholder.unparsed("hex_color", invalidHex)));
    }

    // Party Created
    @Comment("\nAppears when a party is successfully created.\n" +
            "Possible tags: <party_name>")
    private String partyCreated = "<white>Party <party_name> has been created successfully.";

    public Component partyCreated(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyCreated,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Party Joined
    @Comment("\nAppears when joining a party.\n" +
            "Possible tags: <party_name>")
    private String partyJoined = "<white>You have joined <party_name>.";

    public Component partyJoined(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyJoined,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Already in Party
    @Comment("\nAppears when trying to join a party a player is already in.\n" +
            "Possible tags: <party_name>")
    private String alreadyInParty = "<white>You are already in <party_name>!";

    public Component alreadyInParty(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(alreadyInParty,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Party Left
    @Comment("\nAppears when leaving a party.\n" +
            "Possible tags: <party_name>")
    private String partyLeft = "<white>You have left <party_name>.";

    public Component partyLeft(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyLeft,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Not in Party
    @Comment("\nAppears when trying to leave a party a player is not in.\n" +
            "Possible tags: <party_name>")
    private String notInParty = "<white>You are not in <party_name>!";

    public Component notInParty(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(notInParty,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    // Player Joined Party
    @Comment("\nAppears when another player joins a party.\n" +
            "Possible tags: <player_name>, <party_name>")
    private String playerJoinedParty = "<white><player_name> has joined <party_name>.";

    public Component playerJoinedParty(String prefix, String playerName, String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(playerJoinedParty,
                        TagResolver.resolver(Placeholder.parsed("player_name", prefix + playerName),
                                TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD))))));
    }

    // Player Joined Party
    @Comment("\nAppears when another player leaves a party.\n" +
            "Possible tags: <player_name>, <party_name>")
    private String playerLeftParty = "<white><player_name> has left <party_name>.";

    public Component playerLeftParty(String prefix, String playerName, String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(playerLeftParty,
                        TagResolver.resolver(Placeholder.parsed("player_name", prefix + playerName),
                                TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD))))));
    }

    @Comment("\nAppears in the party player list command.\n" +
            "Possible tags: <party_name>")
    private String partyPlayerListHeader = "<white>List of players in <party_name>:";

    public Component partyPlayerListHeader(String partyName, TextColor textColor) {
        return partyHeader.append(MiniMessage.miniMessage().deserialize(partyPlayerListHeader,
                        TagResolver.resolver("party_name", Tag.inserting(Component.text(partyName, textColor, BOLD)))));
    }

    public Component partyPlayerListEntry(String prefix, String playerName) {
        return hyphenHeader.append(MiniMessage.miniMessage().deserialize(prefix + playerName));
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

        return MiniMessage.miniMessage().deserialize(privateMessageHeader, TagResolver.resolver( // Header
                        // Sender prefix
                        TagResolver.resolver("sender_prefix", Tag.inserting(MessageUtil.legacyDeserialize(senderPrefix))),
                        // Sender name with time, status on hover
                        TagResolver.resolver("sender_name", Tag.inserting(Component.text(WordUtils.capitalize(you))
                                .hoverEvent(HoverEvent.showText(Component.text(time + "\n", WHITE)
                                        .append(MessageUtil.getSafeMM().deserialize(senderStatus)))))),
                        // Recipient prefix
                        TagResolver.resolver("recipient_prefix", Tag.inserting(MessageUtil.legacyDeserialize(recipientPrefix))),
                        // Recipient name with time, status on hover
                        TagResolver.resolver("recipient_name", Tag.inserting(Component.text(recipientName)
                                .hoverEvent(HoverEvent.showText(Component.text(time + "\n", WHITE)
                                        .append(MessageUtil.getSafeMM().deserialize(recipientStatus))))))))
                .append(MessageUtil.parsePrivateMessage(message)); // Private message
    }

    // Click to reply
    @Comment("\nAppears when hovering over private messages.")
    private String clickToReply = "<bold><dark_green>Click</bold><white> here to reply.";

    public Component privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                           String senderStatus, String recipientStatus, String time, String message) {

        return MiniMessage.miniMessage().deserialize(privateMessageHeader, TagResolver.resolver( // Header
                        // Sender prefix
                        TagResolver.resolver("sender_prefix", Tag.inserting(MessageUtil.legacyDeserialize(senderPrefix))),
                        // Sender name with time, status, click to reply on hover, pm command on click
                        TagResolver.resolver("sender_name", Tag.inserting(Component.text(senderName)
                                .hoverEvent(HoverEvent.showText(Component.text(time + "\n", WHITE)
                                        .append(MessageUtil.getSafeMM().deserialize(senderStatus))
                                        .append(Component.text("\n\n"))
                                        .append(MiniMessage.miniMessage().deserialize(clickToReply))))
                                .clickEvent(ClickEvent.suggestCommand("/msg " + senderName)))),
                        // Recipient prefix
                        TagResolver.resolver("recipient_prefix", Tag.inserting(MessageUtil.legacyDeserialize(recipientPrefix))),
                        // Recipient name (Word "You") with time, status on hover
                        TagResolver.resolver("recipient_name", Tag.inserting(Component.text(WordUtils.capitalize(you))
                                .hoverEvent(Component.text(time + "\n", WHITE)
                                        .append(MessageUtil.getSafeMM().deserialize(recipientStatus)))))))
                .append(MessageUtil.parsePrivateMessage(message)); // Private message
    }

    // PM Can't Message Self
    @Comment("\nAppears when a player tries to private message themself.")
    private String cantMessageSelf = "<white>You can't send a message to yourself!";

    public Component cantMessageSelf() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(cantMessageSelf));
    }

    // PM No PM Received
    @Comment("\nAppears when a player tries to reply without receiving a private message.")
    private String noPMReceived = "<white>You haven't received any messages!";

    public Component noPMReceived() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(noPMReceived));
    }

    // PM Player Doesn't Exist
    @Comment("\nAppears when a player tries to private message a player that doesn't exist.")
    private String playerNoExist = "<white>Player doesn't exist!";

    public Component playerNoExist() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(playerNoExist));
    }

    /*================================================================================

                                     Chat Messages

    ================================================================================*/

    // Chat Message
    @Comment("\nThis is the format used before every chat message sent.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String chatMessageHeader = "<player_prefix><player_name><gray>: ";

    public Component chatMessage(String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor, boolean showChannelName, boolean isParty) {

        TagResolver placeholders = TagResolver.resolver(
                Placeholder.component("status", MessageUtil.getSafeMM().deserialize(status)),
                TagResolver.resolver("player_prefix", Tag.inserting(MessageUtil.legacyDeserialize(prefix))),
                Placeholder.parsed("player_name", playerName));

        String statusHeader = WordUtils.capitalize(this.status) + ": ";
        String channelHeader = (isParty ? WordUtils.capitalize(party) : WordUtils.capitalize(this.channel)) + ": ";
        Component channelNameHeader = showChannelName ? Component.text("[", GRAY).append(Component.text(activeChannel, textColor)).append(Component.text("] ", GRAY)) : Component.text("");

        return channelNameHeader.append(MiniMessage.miniMessage().deserialize(chatMessageHeader, placeholders) // Header, optional channel name
                        // Hover with time, status, player's channel
                        .hoverEvent(HoverEvent.showText(Component.text().append(Component.text(time + "\n" +
                                statusHeader, WHITE).append(MessageUtil.getSafeMM().deserialize(status)).append(Component.text("\n"))
                                .append(Component.text(channelHeader, WHITE).append(Component.text(activeChannel, textColor))))))
                        // Click to message
                        .clickEvent(ClickEvent.suggestCommand("/msg " + playerName + " ")))
                .append(message); // Message
    }

    // Discord Message
    @Comment("\nThis is the format used before every Discord message sent.\n" +
            "Possible tags: <discord>, <player_prefix>, <player_name>")
    private String discordMessageHeader = "<gray>[<color:#7289DA><discord><gray>] <player_prefix><player_name><gray>: ";

    public Component discordMessage(String discordHeader, String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {

        TagResolver placeholders = TagResolver.resolver(
                Placeholder.parsed("discord", discordHeader),
                Placeholder.parsed("player_prefix", prefix),
                Placeholder.parsed("player_name", playerName));

        String statusHeader = WordUtils.capitalize(this.status) + ": ";
        String channelHeader = WordUtils.capitalize(this.channel) + ": ";

        return MiniMessage.miniMessage().deserialize(discordMessageHeader, placeholders) // Header
                        // Hover with time, status, Discord user's channel
                        .hoverEvent(HoverEvent.showText(Component.text().append(Component.text(time + "\n" +
                                statusHeader, WHITE).append(MessageUtil.getSafeMM().deserialize(status)).append(Component.text("\n"))
                                .append(Component.text(channelHeader, WHITE).append(MiniMessage.miniMessage().deserialize("<" + textColor.asHexString() + ">" + activeChannel))))))
                .append(message); // Message
    }

    // Link hover text
    @Comment("\nThis is the text that appears when hovering over a formatted link.")
    private String chatLinkHoverText = "<dark_green><bold>Click</bold></dark_green><white> here to open this link.";

    public String chatLinkHoverText() {
        return chatLinkHoverText;
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
    private transient Component chatHeader = Component.text("[", GRAY)
            .append(Component.text(WordUtils.capitalize(chat), DARK_GREEN))
            .append(Component.text("] ", GRAY));

    // Hyphen
    private transient Component hyphenHeader = Component.text(" - ", DARK_GREEN);

    // Click to run
    private transient Component clickToRun = Component.text("Click", DARK_GREEN, BOLD)
            .append(Component.text(" to run.", WHITE));

    // ** Bad Config **

    // Plugin Not Configured Correctly
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String pluginNotConfigured = "<white>Plugin not configured correctly.";

    // Incorrect Permission
    @Comment("\nAppears when the plugin is configured incorrectly.")
    private String permError = "<white>Permissions/configuration error.";

    public Component badConfig() {
        return chatHeader.append(MiniMessage.miniMessage().deserialize(pluginNotConfigured))
                .append(Component.text("\n"))
                .append(hyphenHeader)
                .append(MiniMessage.miniMessage().deserialize(permError));
    }

    // ** No Permission **

    // No Permission
    @Comment("\nAppears if player does not have permission to run a command.")
    private String noPermission = "<red>I'm sorry but you do not have permission to perform this" +
            "command. Please contact the server administrators if you" +
            "believe that this is in error.";

    public Component noPermission() {
        return MiniMessage.miniMessage().deserialize(noPermission);
    }
}