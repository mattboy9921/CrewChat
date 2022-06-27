package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.markdown.DiscordFlavor;
import net.mattlabs.crewchat.util.markdown.MiniMarkdownParser;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MessageUtil {
    private static final Pattern urlPattern = Pattern.compile("https?://(-\\.)?([^\\s/?.#-]+\\.?)+(/[^\\s]*)?");

    // Parses message for player names given, returns list of players
    public static ArrayList<Player> getMentionedPlayers(String message, ArrayList<Player> playersToCheck) {
        String[] parts = message.split(" ");
        ArrayList<Player> mentionedPlayers = new ArrayList<>();

        for (Player player : playersToCheck) {
            Pattern playerPattern = Pattern.compile(getMentionRegex(player.getName()));
            for (String part : parts)
                if (playerPattern.matcher(part).matches()) {
                    mentionedPlayers.add(player);
                    break;
                }
        }

        return mentionedPlayers;
    }

    // Parses strings for unescaped single quotes (') and escapes them
    public static String escapeSingleQuotes(String message) {
        return message.replace("'", "\\'");
    }

    // Parse Discord markdown
    public static String parseMarkdown(String message) {
        return MiniMarkdownParser.parse(message, DiscordFlavor.get());
    }

    // Remove dangerous MiniMessage tags
    public static String sanitizeMessageColor(String message) {
        //noinspection unchecked
        return MiniMessage.miniMessage().serialize(MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.font())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.rainbow()).build())
                .build().deserialize(message));
    }

    // Upgrade legacy color codes to MiniMessage tags
    public static String serialize(String legacyColorText) {
        return MiniMessage.miniMessage().serialize(LegacyComponentSerializer.legacy('&').deserialize(MiniMessage.miniMessage().serialize(LegacyComponentSerializer.legacy('§').deserialize(legacyColorText))));
    }

    // Standard process to parse a message
    public static Component parseMessage(String message, TextColor textColor, ArrayList<Player> subscribedPlayers, String discordChannelID, boolean allowColor) {
        message = parseMarkdown(message);
        // Filter out any legacy codes/MiniMessage tags
        if (allowColor) message = sanitizeMessageColor(message);
        else message = MiniMessage.miniMessage().escapeTags(message);

        String[] parts = message.split(" ");
        StringBuilder finalMessage = new StringBuilder().append("<color:" + textColor.asHexString() + ">");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String strippedPart = MiniMessage.miniMessage().stripTags(part);
            String nextPart;
            String mentionedName = null;

            // Match player names
            for (Player player : subscribedPlayers)
                if (Pattern.matches(getMentionRegex(player.getName()), strippedPart)) {
                    mentionedName = player.getName();
                }

            // Match Discord names
            if (mentionedName == null && CrewChat.getInstance().getDiscordSRVEnabled()) {
                for (Member member : DiscordUtil.getTextChannelById(discordChannelID).getMembers())
                    if (Pattern.matches(getMentionRegex(member.getEffectiveName()), strippedPart))
                        mentionedName = member.getEffectiveName();
            }

            if (mentionedName != null) {
                nextPart = "<gold>@" + mentionedName + "</gold>" + strippedPart.substring(mentionedName.length() + (strippedPart.startsWith("@") ? 1 : 0));
            } else { // Match links
                nextPart = urlPattern.matcher(part).replaceAll("<click:open_url:$0><color:#0000EE>$0</color:#0000EE></click>");
            }
            // Add to whole string
            finalMessage.append(nextPart);
            // Add spaces
            if (i != parts.length - 1) finalMessage.append(" ");
        }
        return MiniMessage.miniMessage().deserialize(finalMessage.toString());
    }

    private static String getMentionRegex(String name) {
        return "@?" + name + "((?=([^\\w\\s]|_)).*)?";
    }
}
