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
                    party.initialize();
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

    @CommandAlias("join")
    @Description("Join the specified party.")
    public void onJoin(Player player, Party party) {
        // Check if party exists
        if (channelManager.getChannels().contains(party) && channelManager.getChannels().get(channelManager.getChannels().lastIndexOf(party)) instanceof Party) {
            // Get party
            party = (Party) channelManager.getChannels().get(channelManager.getChannels().lastIndexOf(party));
            // Join player to party
            playerManager.addSubscription(player, party.getName());
            playerManager.setActiveChannel(player, party.getName());
            platform.player(player).sendMessage(crewChat.getMessages().partyJoined(party.getName(), party.getTextColor()));
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().partyNoExist(party.getName()));
        }
    }

    @CommandAlias("leave")
    @Description("Leaves the specified party.")
    public void onLeave(Player player, Party party) {
        // Check if party exists
        if (channelManager.getChannels().contains(party) && channelManager.getChannels().get(channelManager.getChannels().lastIndexOf(party)) instanceof Party) {
            // Check if player in party
            if (playerManager.getSubscribedChannels(player).contains(party.getName())) {
                // Get party
                party = (Party) channelManager.getChannels().get(channelManager.getChannels().lastIndexOf(party));
                // Leave party
                playerManager.removeSubscription(player, party.getName());
                playerManager.setActiveChannel(player, playerManager.getSubscribedChannels(player).get(0));
                platform.player(player).sendMessage(crewChat.getMessages().partyLeft(party.getName(), party.getTextColor()));
            }
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().partyNoExist(party.getName()));
        }
    }
}
