package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.markdown.DiscordFlavor;
import net.mattlabs.crewchat.util.markdown.MiniMarkdownParser;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MessageUtil {
    private static final Pattern urlPattern = Pattern.compile("https?://[\\w$\\-.+!*'(),]+\\.[a-z]+\\.?[a-z]+(/([\\w$\\-+!*'()?=&%#]|\\.(?=\\w))*)*", Pattern.CASE_INSENSITIVE);

    // MiniMessage that only allows color and style tags
    private static final MiniMessage safeMM = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.decorations())
                    .resolver(StandardTags.font())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.rainbow()).build()).build();

    // MiniMessage for escaping all but style and color
    private static final MiniMessage escColorStyleMM = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .resolver(StandardTags.insertion())
                    .resolver(StandardTags.keybind())
                    .resolver(StandardTags.newline()).build()).build();

    // MiniMessage for escaping all but style tags
    private static final MiniMessage escStyleMM = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.font())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .resolver(StandardTags.insertion())
                    .resolver(StandardTags.keybind())
                    .resolver(StandardTags.newline()).build()).build();

    // MiniMessage for serializing only style tags, strict
    private static final MiniMessage serStyleStrictMM = MiniMessage.builder().tags(StandardTags.decorations()).strict(true).build();

    // MiniMessage for serializing nothing
    private static final MiniMessage serNothingMM = MiniMessage.builder().build();

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

    // Parse Discord markdown
    public static String parseMarkdown(String message) {
        return MiniMarkdownParser.parse(message, DiscordFlavor.get());
    }

    // Parse MiniMessage style tags to Discord markdown
    public static String discordMarkdown(Component component) {
        return serStyleStrictMM.serialize(component)
                .replace("<italic>", "_")
                .replace("</italic>", "_")
                .replace("<bold>", "**")
                .replace("</bold>", "**")
                .replace("<underlined>", "__")
                .replace("</underlined>", "__")
                .replace("<strikethrough>", "~~")
                .replace("</strikethrough>", "~~")
                .replace("<obfuscated>", "||")
                .replace("</obfuscated>", "||");
    }

    // Upgrade legacy color codes to MiniMessage tags
    public static Component legacyDeserialize(String legacyColorText) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(legacyColorText);
    }

    // Standard process to parse a message
    public static Component parseMessage(String message, TextColor textColor, ArrayList<Player> subscribedPlayers, String discordChannelID, boolean allowColor, boolean parseMentions, boolean parseLinks) {
        message = parseMarkdown(message);
        // Filter out any legacy codes/MiniMessage tags
        if (allowColor) message = escColorStyleMM.escapeTags(message);
        else message = escStyleMM.escapeTags(message);

        String[] parts = message.split(" ");
        StringBuilder finalMessage = new StringBuilder().append("<color:").append(textColor.asHexString()).append(">");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String strippedPart = MiniMessage.miniMessage().stripTags(part);
            String nextPart = "";
            String mentionedName = null;

            // Parse Mentions
            if (parseMentions) {
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
            }

            if (mentionedName != null) {
                nextPart = "<gold>@" + mentionedName + "</gold>" + strippedPart.substring(mentionedName.length() + (strippedPart.startsWith("@") ? 1 : 0));
            }

            // Parse Links
            else if (parseLinks) {
                // Match links
                nextPart = urlPattern.matcher(part).replaceAll(
                        "<hover:show_text:'" + CrewChat.getInstance().getMessages().chatMessage().chatLinkHoverText() + "'>" +
                                "<click:open_url:$0>" +
                                "<color:#5394EC>" +
                                "$0" +
                                "</color:#5394EC>" +
                                "</click>" +
                                "</hover>");
            }

            // Add to whole string
            finalMessage.append(nextPart);
            // Add spaces
            if (i != parts.length - 1) finalMessage.append(" ");
        }
        return MiniMessage.miniMessage().deserialize(finalMessage.toString());
    }

    public static Component parseChatMessage(String message, TextColor textColor, ArrayList<Player> subscribedPlayers, String discordChannelID, boolean allowColor) {
        return parseMessage(message, textColor, subscribedPlayers, discordChannelID, allowColor, true, true);
    }

    public static Component parsePrivateMessage(String message) {
        return parseMessage(message, NamedTextColor.WHITE, null, null, true, false, true);
    }

    private static String getMentionRegex(String name) {
        return "@?" + name + "((?=([^\\w\\s]|_)).*)?";
    }

    public static MiniMessage getSafeMM() {
        return safeMM;
    }

    public static MiniMessage getSerNothingMM() {
        return serNothingMM;
    }
}
