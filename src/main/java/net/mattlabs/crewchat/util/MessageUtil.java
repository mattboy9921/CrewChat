package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.transformation.TransformationType.*;
import static net.kyori.adventure.text.minimessage.transformation.TransformationType.RESET;

public class MessageUtil {

    // Parses message for player names given, returns list of players
    public static ArrayList<Player> getMentionedPlayers(String message, ArrayList<Player> playersToCheck) {
        String[] parts = message.split(" ");
        ArrayList<Player> mentionedPlayers = new ArrayList<>();

        for (String part : parts) {
            // Match player names
            for (Player player : playersToCheck)
                if (Pattern.matches("[@]?" + player.getName() + "((?=([^\\w\\s]|_)).*)?", part))
                    mentionedPlayers.add(player);
        }

        return mentionedPlayers;
    }

    // Removes any legacy codes/MiniMessage tags
    public static String sanitizeMessage(String message) {
        message = MiniMessage.get().serialize(LegacyComponentSerializer.legacy('&').deserialize(message));
        message = MiniMessage.get().serialize(LegacyComponentSerializer.legacy('§').deserialize(message));
        message = PlainComponentSerializer.plain().serialize(MiniMessage.get().parse(message));
        return message;
    }

    // Parse Discord markdown
    public static Component parseMarkdown(Component message) {
        String messageSerialized = MiniMessage.get().serialize(message);
        return MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(messageSerialized);
    }

    // Remove dangerous MiniMessage tags
    public static String sanitizeMessageColor(String message) {
        //noinspection unchecked
        return MiniMessage.get().serialize(MiniMessage.builder().strict(true).transformations(COLOR, DECORATION, FONT, GRADIENT, RAINBOW, RESET).build().parse(message));
    }

    // Standard process to parse a message
    public static Component parseMessage(String message, TextColor textColor, ArrayList<Player> subscribedPlayers, String discordChannelID, boolean allowColor) {
        // Filter out any legacy codes/MiniMessage tags
        if (allowColor) message = sanitizeMessageColor(message);
        else message = sanitizeMessage(message);

        String[] parts = message.split(" ");
        Component componentMessage = Component.text("");
        ArrayList<Player> mentionedPlayers = getMentionedPlayers(message, subscribedPlayers);

        for (String part : parts) {
            Component nextComponent = Component.text(part).color(textColor);
            String mentionedName = null;

            // Match player names
            for (Player player : subscribedPlayers)
                if (Pattern.matches("[@]?" + player.getName() + "((?=([^\\w\\s]|_)).*)?", part)) {
                    mentionedPlayers.add(player);
                    mentionedName = player.getName();
                }

            // Match Discord names
            if (CrewChat.getInstance().getDiscordSRVEnabled()) {
                if (mentionedName == null)
                    for (Member member :  DiscordUtil.getTextChannelById(discordChannelID).getMembers())
                        if (Pattern.matches("[@]?" + member.getEffectiveName() + "((?=([^\\w\\s]|_)).*)?", part)) mentionedName = member.getEffectiveName();
            }

            if (mentionedName != null) {
                String split = (part.startsWith("@")) ? "@" + mentionedName : mentionedName;
                String[] mentionParts = part.split(split);
                nextComponent = Component.text("@" + mentionedName).color(NamedTextColor.GOLD);
                if (mentionParts.length > 0) {
                    Component afterMention = Component.text(mentionParts[1]).color(textColor);
                    nextComponent = nextComponent.append(afterMention);
                }
            }
            // Match links
            else if (Pattern.matches("^(http://www\\.|https://www\\.|http://|https://)[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?.*", part)) {
                part = part.replaceAll("^(http://www\\.|https://www\\.|http://|https://)[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?", "$0 ");
                String[] linkParts = part.split(" ");
                part = linkParts[0];
                // Get website description with Jsoup
                String description = "No info found...";
                try {
                    Elements elements = Jsoup.connect(part).get().select("meta[name=description]");
                    if (!elements.isEmpty()) description = elements.get(0).attr("content");
                }
                catch (IOException ignored) {}

                nextComponent = Component.text(part).color(NamedTextColor.BLUE).hoverEvent(HoverEvent.showText(Component.text(description).color(NamedTextColor.WHITE))).clickEvent(ClickEvent.openUrl(part));
                if (linkParts.length == 2) {
                    Component afterLink = Component.text(linkParts[1]).color(textColor);
                    nextComponent = nextComponent.append(afterLink);
                }
            }
            componentMessage = componentMessage.append(nextComponent);
            if (!part.equals(parts[parts.length - 1]))
                componentMessage = componentMessage.append(Component.space());
        }
        return componentMessage;
    }
}
