package net.mattlabs.crewchat.messaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class Messages {

    public Messages() {

    }

    // *** Chat Command ***

    // ** Base **

    // Welcome to chat
    @Comment("Appears in the chat base command.\n")
    private String welcomeToChat = "<white>Welcome to chat!";

    public Component chatBaseCommand() {
        return chatHeader
                .append(MiniMessage.get().parse(welcomeToChat))
                .append(Component.text("[Help]")
                    .decoration(BOLD, true)
                    .color(BLUE)
                    .hoverEvent(HoverEvent.showText(
                        Component.text("Click")
                            .color(AQUA)
                            .decoration(BOLD, true)
                            .append(Component.text(" here for help.")
                                .color(WHITE)
                                .decoration(BOLD, false))))
                    .clickEvent(ClickEvent.runCommand("/chat help")));
    }

    // ** Deafen **

    // Player Deafened
    @Comment("Appears when a player deafens themself.\n")
    private String playerDeafened = "<white>You have been deafened. You will not receive any chat messages.";

    public Component playerDeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerDeafened));
    }

    // Player Undeafened
    @Comment("Appears when a player undeafens themself.\n")
    private String playerUndeafened = "<white>You are no longer deafened. You will receive all chat messages.";

    public Component playerUndeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerUndeafened));
    }

    // Player Is Deafened
    @Comment("Appears when a player tries to send a chat message while deafened.\n")
    private String playerIsDeafened = "<white>You are deafened and cannot see chat messages!";

    public Component playerIsDeafened() {
        return chatHeader.append(MiniMessage.get().parse(playerIsDeafened));
    }

    // ** General **

    // Channel No Exist
    @Comment("Appears when a command contains a channel that doesn't exist.\n" +
            "Possible tags: <channel_name>")
    private String channelNoExist = "<white>Channel <bold><channel_name></bold> doesn't exist!";

    public Component channelNoExist(String name) {
        // &7[&2Chat&7] &fChannel &l%name%&r doesn't exist!
        return chatHeader.append(MiniMessage.get().parse(channelNoExist, "channel_name", name));
    }

    // ** Help **
    public Component chatHelpCommand() {
        return chatHeader
                .append(Component.text("Command Help:\n")
                    .color(WHITE))
                .append(Component.text(" - Alias: /c <args> - (Click to run) -\n")
                    .color(GRAY))
                .append(Component.text("/chat")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Base Chat command.\n")
                    .color(WHITE))
                .append(Component.text("/chat help")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat help")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Shows this screen.\n")
                    .color(WHITE))
                .append(Component.text("/chat info")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/chat info")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Lists all channels, active channel and subscribed channels.\n")
                    .color(WHITE))
                .append(Component.text("/chat info channel <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat info channel ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Lists info about specified channel.\n")
                    .color(WHITE))
                .append(Component.text("/chat status <status>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat status ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Sets player's status.\n")
                    .color(WHITE))
                .append(Component.text("/chat subscribe <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat subscribe ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Subscribes player to channel.\n")
                    .color(WHITE))
                .append(Component.text("/chat unsubscribe <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat unsubscribe ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Unsubscribes player from channel.\n")
                    .color(WHITE))
                .append(Component.text("/chat switch <channel>")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.suggestCommand("/chat switch ")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Switches active channel.\n")
                    .color(WHITE));
    }

    // ** Info **

    // Channel List Header
    @Comment("Appears in the chat info command.\n" +
            "(Does not accept color codes)\n")
    private String channelListHeader = "Channel List: (Click for more info)";

    // Channel List Active
    @Comment("Appears in the chat info command.\n" +
            "(Does not accept color codes)\n")
    private String channelListActive = "Your active channel is: <channel_name>.";

    // Channel List Subscribe Header
    @Comment("Appears in the chat info command.\n" +
            "(Does not accept color codes)\n")
    private String channelListSubscribedHeader = "Your subscribed channels are:";

    // Muted List Header
    @Comment("Appears in the chat info command.\n" +
            "(Does not accept color codes)\n")
    private String mutedListHeader = "Your muted players are:";

    // Click to unmute
    @Comment("Appears in the chat info command.\n")
    private String clickToUnmute = "<bold><aqua>Click<reset> to unmute.";

    public Component channelListHeader() {
        return Component.text("------------------------")
                .color(GRAY)
                .append(Component.text("[")
                        .color(DARK_GRAY))
                .append(Component.text("Chat")
                        .color(DARK_GREEN))
                .append(Component.text("]")
                        .color(DARK_GRAY))
                .append(Component.text("------------------------\n")
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
                                        .color(AQUA)
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

    public Component mutedListEntry(String player) {
        return hyphenHeader
                .append(Component.text(player)
                        .color(WHITE)
                        .hoverEvent(HoverEvent.showText(MiniMessage.get().parse(clickToUnmute)))
                        .clickEvent(ClickEvent.runCommand("/chat unmute " + player)));
    }

    // ** Info Channel **

    // Header
    private transient String channelInfoHeader = "<white>Channel <bold><channel_name></bold> info:";

    // Name
    private transient String channelInfoName = "<white>Name: <channel_name>";

    // Color
    private transient String channelInfoColor = "<white>Color: <channel_color>";

    // Auto Subscribe
    private transient String channelInfoAutoSubscribe = "<white>Auto Subscribe: <channel_autosubscribe>";

    // TODO Make this part of CrewChat commands
    public Component channelInfo(String name, TextColor color) {
        // &7[&2Chat&7] &fChannel &l%name%&r info:
        // &2- &fName: %name%
        // &2- &fChat Color: %color%
        // &2- &fAuto Subscribe: %autosus% TODO Fix this...

        // Get closest color to hex code, format with capital first letter
        String closestColor = nearestTo(color).toString();
        closestColor = closestColor.substring(0, 1).toUpperCase() + closestColor.substring(1);

        return chatHeader
                .append(MiniMessage.get().parse(channelInfoHeader + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoName + "\n", "channel_name", name))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(channelInfoColor, "channel_color", "<color:" + color.toString() + ">" + closestColor + " (" + color.asHexString() + ")"));
    }

    // ** Mute/Unmute **

    // Chat Can't Mute Self
    @Comment("Appears when a player tries to mute themself.\n")
    private String cantMuteSelf = "<white>You can't mute yourself!";

    public Component cantMuteSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantMuteSelf));
    }

    // Chat Can't Unmute Self
    @Comment("Appears when a player tries to unmute themself.\n")
    private String cantUnmuteSelf = "<white>You can't unmute yourself!";

    public Component cantUnmuteSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantUnmuteSelf));
    }

    // Chat Player Already Muted
    @Comment("Appears when a player tries to mute someone they've already muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyMuted = "<white><player_prefix><player_name> is already muted!";

    public Component playerAlreadyMuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.get().parse(playerAlreadyMuted,
                "player_prefix", playerPrefix,
                "player_name", playerName));
    }

    // Chat Player Already Unmuted
    @Comment("Appears when a player tries to unmute someone who isn't muted.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerAlreadyUnmuted = "<white><player_prefix><player_name> is not muted!";

    public Component playerAlreadyUnmuted(String playerPrefix, String playerName) {
        return chatHeader.append(MiniMessage.get().parse(playerAlreadyUnmuted,
                "player_prefix", playerPrefix,
                "player_name", playerName));
    }

    // Chat Player Muted
    @Comment("Appears when a player mutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerMuted = "<white><player_prefix><player_name> has been muted.";

    public Component playerMuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.get().parse(playerMuted,
                "player_prefix", playerPrefix,
                "player_name", player));
    }

    // Chat Player Unmuted
    @Comment("Appears when a player unmutes someone.\n" +
            "Possible tags: <player_prefix>, <player_name>")
    private String playerUnmuted = "<white><player_prefix><player_name> has been unmuted.";

    public Component playerUnmuted(String playerPrefix, String player) {
        return chatHeader.append(MiniMessage.get().parse(playerUnmuted,
                "player_prefix", playerPrefix,
                "player_name", player));
    }

    // ** Status **

    @Comment("Appears when a player sets their status.\n" +
            "Possible tags: <status>")
    private String statusSet = "<white>Your status has been set to: \"<status>\".";

    public Component statusSet(String status) {
        return chatHeader.append(MiniMessage.get().parse(statusSet, "status", status));
    }

    // ** Subscribe/Unsubscribe **

    // Already Subscribed
    @Comment("Appears when a player tries to subscribe to a channel they already subscribe to.\n" +
            "Possibe tags: <channel_name>")
    private String alreadySubscribed = "<white>You are already subscribed to <bold><channel_name></bold>.";

    public Component alreadySubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(alreadySubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Subscribe
    @Comment("Appears when a player can't subscribe to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantSubscribe = "<white>You can't subscribe to <bold><channel_name></bold>!";

    public Component cantSubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantSubscribe, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Unsubscribe
    @Comment("Appears when a player can't unsubscribe from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribe = "<white>You can't unsubscribe from <bold><channel_name></bold>!";

    public Component cantUnsubscribe(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantUnsubscribe, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Unsubscribe Active
    @Comment("Appears when a player can't unsubscribe from a channel because it is their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String cantUnsubscribeActive = "<white>You can't unsubscribe from <bold><channel_name></bold>, it is your active channel!";

    public Component cantUnsubscribeActive(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(cantUnsubscribeActive, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Not Subscribed
    @Comment("Appears when a player unsubscribes from a channel they are not subscribed to.\n" +
            "Possibe tags: <channel_name>")
    private String notSubscribed = "<white>You aren't subscribed to <bold><channel_name></bold>.";

    public Component notSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(notSubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Now Subscribed
    @Comment("Appears when a player subscribes to a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowSubscribed = "<white>You are now subscribed to <bold><channel_name></bold>.";

    public Component nowSubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(nowSubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Now Unsubscribed
    @Comment("Appears when a player unsubscribes from a channel.\n" +
            "Possibe tags: <channel_name>")
    private String nowUnsubscribed = "<white>You are no longer subscribed to <bold><channel_name></bold>.";

    public Component nowUnsubscribed(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(nowUnsubscribed, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // ** Switch **

    // New Active Channel
    @Comment("Appears when a player changes their active channel.\n" +
            "Possibe tags: <channel_name>")
    private String newActiveChannel = "<white>Your active channel is now <bold><channel_name></bold>.";

    // TODO: Add chat color to other messages that reference a channel, possibly consolidate method signature to just use the channel
    public Component newActiveChannel(String channelName, TextColor textColor) {
        return chatHeader.append(MiniMessage.get().parse(newActiveChannel, "channel_name", "<color:" + textColor.toString() + ">" + channelName));
    }

    // Can't Set Active
    @Comment("Appears when a player can't change their active channel.\n" +
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
                            .color(AQUA)
                            .decoration(BOLD, true)
                            .append(Component.text(" here for help.")
                                .color(WHITE)
                                .decoration(BOLD, false))))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")));
    }

    // ** Help **

    // Help
    public Component crewChatHelpCommand() {
        return crewChatHeader
                .append(Component.text("Command Help:\n")
                    .color(WHITE))
                .append(Component.text(" - Alias: /cc <args> - (Click to run) -\n")
                    .color(GRAY))
                .append(Component.text("/crewchat")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Base CrewChat command.\n")
                    .color(WHITE))
                .append(Component.text("/crewchat help")
                    .color(DARK_GREEN)
                    .hoverEvent(HoverEvent.showText(clickToRun))
                    .clickEvent(ClickEvent.runCommand("/crewchat help")))
                .append(Component.text(" - ")
                    .color(GRAY))
                .append(Component.text("Shows this screen.\n")
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

    // *** Private Message ***

    // PM Header
    @Comment("This is the format used before every private message sent.\n" +
            "Possible tags: <sender_prefix>, <sender_name>, <recipient_prefix>, <recipient_name>")
    private String privateMessageHeader = "<gray>[<reset><sender_prefix><sender_name><reset> <gray>-><reset> <recipient_prefix><recipient_name><reset><gray>]<reset> ";

    // TODO: Add date and channel to this
    public Component privateMessageSend(String senderPrefix, String recipientPrefix, String recipientName,
                                        String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%me &7-> %recipientPrefix%%recipientName%&7] &r%message%
        return MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", serialize(senderPrefix),
                "sender_name", you + "<hover:show_text:'<white>" + senderStatus + "'>",
                "recipient_prefix", serialize(recipientPrefix),
                "recipient_name", recipientName + "<hover:show_text:'<white>" + recipientStatus + "'>")
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(message));
    }

    // Click to reply
    @Comment("Appears when hovering over private messages.\n")
    private String clickToReply = "<bold><aqua>Click<reset> this message to reply.";

    // TODO: Add date and channel to this
    public Component privateMessageReceive(String senderPrefix, String recipientPrefix, String senderName,
                                           String senderStatus, String recipientStatus, String message) {
        // &7[%senderPrefix%%senderName% &7-> %recipientPrefix%Me&7] &r%message%
        return MiniMessage.get().parse(privateMessageHeader,
                "sender_prefix", serialize(senderPrefix),
                "sender_name", "<hover:show_text:'<white>" + senderStatus + "'>" + senderName,
                "recipient_prefix", serialize(recipientPrefix),
                "recipient_name", "<hover:show_text:'<white>" + recipientStatus + "'>" + you)
                .append(MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(
                        "<hover:show_text:'" + clickToReply + "'><click:suggest_command:/msg " + senderName + " >" + message));
    }

    // PM Can't Message Self
    @Comment("Appears when a player tries to private message themself.\n")
    private String cantMessageSelf = "<white>You can't send a message to yourself!";

    public Component cantMessageSelf() {
        return chatHeader.append(MiniMessage.get().parse(cantMessageSelf));
    }

    // PM No PM Received
    @Comment("Appears when a player tries to reply without receiving a private message.\n")
    private String noPMReceived = "<white>You haven't received any messages!";

    public Component noPMReceived() {
        return chatHeader.append(MiniMessage.get().parse(noPMReceived));
    }

    // PM Player Doesn't Exist
    @Comment("Appears when a player tries to private message a player that doesn't exist.\n")
    private String playerNoExist = "<white>Player doesn't exist!";

    public Component playerNoExist() {
        return chatHeader.append(MiniMessage.get().parse(playerNoExist));
    }

    // *** Chat Messages ***

    // Chat Message
    @Comment("This is the format used before every chat message sent.\n" +
            "Possible tags: <player_prefix><player_name>")
    private String chatMessageHeader = "<player_prefix><player_name><gray>: ";

    public Component chatMessage(String prefix, String playerName, String time, String status, Component message, String activeChannel, TextColor textColor) {
        // %prefix%%playerName%: %message%
        return MiniMessage.get().parse("<click:suggest_command:/msg " + playerName + " >" +
                        "<hover:show_text:'<white>" + time + "\n" +
                        this.status + ": <pre>" + status + "</pre>\n" +
                        this.channel + ": " + "<" + textColor.toString() + ">" + activeChannel + "'>" +
                        chatMessageHeader + "<reset><" + textColor.toString() + ">",
                "player_prefix", prefix,
                "player_name", playerName)
                .append(message);
    }

    // *** General ***

    // General fields used in many strings
    @Comment("\"Chat\" text that appears before many messages.\n" +
            "(Does not accept color codes)\n")
    private String chat = "Chat";

    @Comment("Value for the word \"Status\".\n")
    private String status = "Status";

    @Comment("Value for the word \"Channel\".\n")
    private String channel = "Channel";

    @Comment("Value for the word \"You\".\n")
    private String you = "You";

    @Comment("Value for the word \"Help\".\n" +
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
                .color(AQUA)
                .decoration(BOLD, true)
                .append(Component.text(" to run.")
                    .decoration(BOLD, false)
                    .color(WHITE));

    // ** Bad Config **
    // Plugin Not Configured Correctly
    @Comment("Appears when the plugin is configured incorrectly.\n")
    private String pluginNotConfigured = "<white>Plugin not configured correctly.";

    // Incorrect Permission
    @Comment("Appears when the plugin is configured incorrectly.\n")
    private String permError = "<white>Permissions/configuration error.";

    public Component badConfig() {
        return chatHeader
                .append(MiniMessage.get().parse(pluginNotConfigured))
                .append(Component.text("\n"))
                .append(hyphenHeader)
                .append(MiniMessage.get().parse(permError));
    }

    // ** No Permission **

    // No Permission
    @Comment("Appears if player does not have permission to run a command.\n")
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
        return MiniMessage.get().serialize(LegacyComponentSerializer.legacy('&').deserialize(legacyColorCode));
    }
}
