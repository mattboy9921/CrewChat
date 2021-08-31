package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.Party;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@CommandAlias("party|p")
@CommandPermission("crewchat.party")
public class PartyCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ChannelManager channelManager = crewChat.getChannelManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();

    public PartyCommand() {
        // Register party completion
        crewChat.getPaperCommandManager().getCommandCompletions().registerAsyncCompletion("parties", context -> {
            ArrayList<String> parties = new ArrayList<>();
            for (Channel channel : channelManager.getChannels())
                if (channel instanceof Party) parties.add(channel.getName());
            return parties;
        });
    }

    @Subcommand("create")
    @Description("Creates a party.")
    @CommandPermission("crewchat.party.create")
    @CommandCompletion("@nothing @nothing")
    public void onCreate(Player player, Party party, @Optional @Single String hexColor) {
        // Check if party exists
        if (party != null)
            platform.player(player).sendMessage(crewChat.getMessages().partyAlreadyExists(
                    channelManager.channelFromString(party.getName()).getName(), channelManager.getTextColor(party)));
        // Check if channel exists with same name
        else if (channelManager.channelFromString(getLastCommandOperationContext().getArgs()[0]) != null) {

            platform.player(player).sendMessage(crewChat.getMessages().partyChannelAlreadyExists(
                    channelManager.channelFromString(getLastCommandOperationContext().getArgs()[0]).getName(),
                    channelManager.getTextColor(channelManager.channelFromString(getLastCommandOperationContext().getArgs()[0]))));
        }
        else {
            party = new Party(getLastCommandOperationContext().getArgs()[0], NamedTextColor.WHITE);
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

    @Subcommand("join")
    @Description("Join the specified party.")
    @CommandCompletion("@parties")
    public void onJoin(Player player, Party party) {
        // Check if party exists
        if (party != null) {
            // Check if player not in party
            if (!playerManager.getSubscribedChannels(player).contains(party.getName())) {
                // Join player to party
                playerManager.addSubscription(player, party.getName());
                playerManager.setActiveChannel(player, party.getName());
                platform.player(player).sendMessage(crewChat.getMessages().partyJoined(party.getName(), party.getTextColor()));
                // Notify others in party
                playerManager.getSubscribedPlayers(party.getName()).forEach(subbedPlayer -> {
                    if (subbedPlayer != player)
                        platform.player(subbedPlayer).sendMessage(crewChat.getMessages().playerJoinedParty(
                                CrewChat.getChat().getPlayerPrefix(player),
                                player.getName(),
                                party.getName(),
                                party.getTextColor()));
                });
            }
            else
                platform.player(player).sendMessage(crewChat.getMessages().alreadyInParty(party.getName(), party.getTextColor()));
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().partyNoExist(getLastCommandOperationContext().getArgs()[0]));
        }
    }

    @Subcommand("leave")
    @Description("Leaves the specified party.")
    @CommandCompletion("@parties")
    public void onLeave(Player player, Party party) {
        // Check if party exists
        if (party != null) {
            // Check if player is already in party
            if (playerManager.getSubscribedChannels(player).contains(party.getName())) {
                // Leave party
                playerManager.removeSubscription(player, party.getName());
                playerManager.setActiveChannel(player, playerManager.getSubscribedChannels(player).get(0));
                platform.player(player).sendMessage(crewChat.getMessages().partyLeft(party.getName(), party.getTextColor()));
                // Notify others in party
                playerManager.getSubscribedPlayers(party.getName()).forEach(subbedPlayer -> {
                    if (subbedPlayer != player)
                        platform.player(subbedPlayer).sendMessage(crewChat.getMessages().playerLeftParty(
                                CrewChat.getChat().getPlayerPrefix(player),
                                player.getName(),
                                party.getName(),
                                party.getTextColor()));
                });
            }
            else
                platform.player(player).sendMessage(crewChat.getMessages().notInParty(party.getName(), party.getTextColor()));
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().partyNoExist(getLastCommandOperationContext().getArgs()[0]));
        }
    }

    @Subcommand("list")
    @Description("Lists all members of a party.")
    @CommandCompletion("@parties")
    public void onList(CommandSender commandSender, Party party) {
        // Check if party exists
        if (party != null) {
            platform.sender(commandSender).sendMessage(crewChat.getMessages().partyPlayerListHeader(party.getName(), party.getTextColor()));
            playerManager.getSubscribedPlayers(party.getName()).forEach(subbedPlayer -> {
                platform.sender(commandSender).sendMessage(crewChat.getMessages().partyPlayerListEntry(CrewChat.getChat().getPlayerPrefix(subbedPlayer), subbedPlayer.getName()));
            });
        }
        else
            platform.sender(commandSender).sendMessage(crewChat.getMessages().partyNoExist(getLastCommandOperationContext().getArgs()[0]));
    }
}
