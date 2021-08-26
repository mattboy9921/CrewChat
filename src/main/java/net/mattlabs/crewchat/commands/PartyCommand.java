package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.Party;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.entity.Player;

@CommandAlias("party|p")
@CommandPermission("crewchat.party")
public class PartyCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ChannelManager channelManager = crewChat.getChannelManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();

    @CommandAlias("create")
    @CommandPermission("crewchat.party.create")
    @Description("Creates a party.")
    public void onCreate(Player player, Party party, @Optional @Single String hexColor) {
        // Check if party exists
        if (channelManager.getChannels().contains(party))
            if (channelManager.channelFromString(party.getName()) instanceof Party)
                platform.player(player).sendMessage(crewChat.getMessages().partyAlreadyExists(
                        channelManager.channelFromString(party.getName()).getName(), channelManager.getTextColor(party)));
            else
                platform.player(player).sendMessage(crewChat.getMessages().partyChannelAlreadyExists(
                        channelManager.channelFromString(party.getName()).getName(), channelManager.getTextColor(party)));
        else {
            // Color picker
            if (hexColor == null)
                platform.player(player).sendMessage(crewChat.getMessages().pickAColor(party.getName()));
            else {
                // Color is invalid
                if (TextColor.fromHexString(hexColor) == null) platform.player(player).sendMessage(crewChat.getMessages().invalidColor(hexColor));
                else {
                    // Create party
                    party.setTextColor(TextColor.fromHexString(hexColor));
                    channelManager.addChannel(party);
                    platform.player(player).sendMessage(crewChat.getMessages().partyCreated(party.getName(), party.getTextColor()));

                    // Join player to party
                    playerManager.addSubscription(player, party.getName());
                    playerManager.setActiveChannel(player, party.getName());
                    platform.player(player).sendMessage(crewChat.getMessages().partyJoined(party.getName(), party.getTextColor()));

                }
            }
        }
    }
}
