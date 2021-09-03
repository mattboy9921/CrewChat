package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.transformation.TransformationType.*;

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
        message = PlainTextComponentSerializer.plainText().serialize(MiniMessage.get().parse(message));
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
        return MiniMessage.get().serialize(MiniMessage.builder().transformations(COLOR, DECORATION, FONT, GRADIENT, RAINBOW, RESET, PRE).build().parse(message));
    }

    // Upgrade legacy color codes to MiniMessage tags
    public static String serialize(String legacyColorText) {
        return MiniMessage.get().serialize(LegacyComponentSerializer.legacy('&').deserialize(MiniMessage.get().serialize(LegacyComponentSerializer.legacy('§').deserialize(legacyColorText))));
    }

    // Hopefully *temporary* fix for MiniMessage 4.2.0 weirdness
    public static Component reparse(Component component) {
        return MiniMessage.get().parse(MiniMessage.get().serialize(component));
    }

    // Standard process to parse a message
    public static Component parseMessage(String message, TextColor textColor, ArrayList<Player> subscribedPlayers, String discordChannelID, boolean allowColor) {
        // Filter out any legacy codes/MiniMessage tags
        if (!allowColor) message = sanitizeMessage(message);

        String[] parts = message.split(" ");
        StringBuilder finalMessage = new StringBuilder().append("<color:" + textColor.asHexString() + ">");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String nextPart = part;
            String mentionedName = null;

            // Match player names
            for (Player player : subscribedPlayers)
                if (Pattern.matches("[@]?" + player.getName() + "((?=([^\\w\\s]|_)).*)?", MiniMessage.get().stripTokens(part))) {
                    mentionedName = player.getName();
                }

            // Match Discord names
            if (CrewChat.getInstance().getDiscordSRVEnabled()) {
                if (mentionedName == null)
                    for (Member member :  DiscordUtil.getTextChannelById(discordChannelID).getMembers())
                        if (Pattern.matches("[@]?" + member.getEffectiveName() + "((?=([^\\w\\s]|_)).*)?", MiniMessage.get().stripTokens(part))) mentionedName = member.getEffectiveName();
            }

            if (mentionedName != null) {
                String split = (part.startsWith("@")) ? "@" + mentionedName : mentionedName;
                String[] mentionParts = MiniMessage.get().stripTokens(part).split(split);
                nextPart = "<gold>@" + mentionedName + "</gold>";
                if (mentionParts.length > 0) {
                    Component afterMention = Component.text(mentionParts[1]).color(textColor);
                    nextPart = nextPart + mentionParts[1];
                }
            }
            // Match links
            else if (Pattern.matches("^(http://www\\.|https://www\\.|http://|https://)[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?.*", MiniMessage.get().stripTokens(part))) {
                part = MiniMessage.get().stripTokens(part).replaceAll("^(http://www\\.|https://www\\.|http://|https://)[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?", "$0 ");
                String[] linkParts = part.split(" ");
                part = linkParts[0];
                // Get website description with Jsoup
                String description = "No info found...";
                try {
                    Elements elements = Jsoup.connect(part).get().select("meta[name=description]");
                    if (!elements.isEmpty()) description = elements.get(0).attr("content");
                }
                catch (IOException ignored) {}

                nextPart = "<hover:show_text:'<white>" + description + "'><click:open_url:" + part + "><color:#0000EE>" + part + "</color:#0000EE></click></hover>";
                if (linkParts.length == 2) {
                    Component afterLink = Component.text(linkParts[1]).color(textColor);
                    nextPart = nextPart + linkParts[1];
                }
            }
            // Add to whole string
            finalMessage.append(nextPart);
            // Add spaces
            if (i != parts.length - 1) finalMessage.append(" ");
        }
        // Parse colors if allowed
        String finalMessageString = finalMessage.toString();
        if (allowColor) finalMessageString = sanitizeMessageColor(finalMessageString);
        return MiniMessage.get().parse(finalMessageString);
    }
}
