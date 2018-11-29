package net.mattlabs.crewchat.commands;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChatCommand implements CommandExecutor{

    private ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 0) return false;
        else if (strings[0].equalsIgnoreCase("info")) {
            if (strings.length == 1) {
                if (commandSender instanceof Player) {
                    if (commandSender.hasPermission("crewchat.chat.info")) {
                        Messages.channelListHeader().send(commandSender);
                        for (Channel channel : channelManager.getChannels())
                            Messages.channelListEntry(channel.getName(), channel.getChatColor()).send(commandSender);
                        Messages.channelListActive(playerManager.getActiveChannel((Player) commandSender),
                                channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender)).getChatColor()).send(commandSender);
                        Messages.channelListSubscribedHeader().send(commandSender);
                        for (String channel : playerManager.getSubscribedChannels((Player) commandSender))
                            Messages.channelListEntry(channel, channelManager.channelFromString(channel).getChatColor()).send(commandSender);
                    }
                    else Messages.noPermission().send(commandSender);
                }
                else {
                    CrewChat.getInstance().getLogger().info("Channel list: (Run /chat info channel [channel] for more info)");
                    for (Channel channel : channelManager.getChannels())
                        CrewChat.getInstance().getLogger().info(" - " + channel.getName());
                }
            }
            else if (strings[1].equalsIgnoreCase("channel")) {
                Channel requestedChannel = null;
                for (Channel channel : channelManager.getChannels()) {
                    if (channel.getName().equalsIgnoreCase(strings[2])) requestedChannel = channel;
                }

                if (requestedChannel != null) {
                    if (commandSender instanceof Player) {
                        if (commandSender.hasPermission("crewchat.chat.info.channel")) {
                            Messages.channelInfo(requestedChannel.getName(),
                                    requestedChannel.getNickname(),
                                    requestedChannel.getChatColor().name(),
                                    requestedChannel.getChatColor()).send(commandSender);
                        }
                        else Messages.noPermission().send(commandSender);
                    }
                    else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                            + " info: " +
                            "\n - Name: " + requestedChannel.getName() +
                            "\n - Nickname: " + requestedChannel.getNickname() +
                            "\n - Chat Color: " + requestedChannel.getChatColor().name() +
                            "\n - Auto Subscribe: " + String.valueOf(requestedChannel.isAutoSubscribe()));
                } else {
                    if (commandSender instanceof Player) {
                        if (commandSender.hasPermission("crewchat.chat.info.channel")) {
                            Messages.channelNoExist(strings[2]).send(commandSender);
                        }
                        else Messages.noPermission().send(commandSender);
                    }
                    else CrewChat.getInstance().getLogger().info("Channel " + strings[2] + " doesn't exist!");
                }
            }
        }
        else if (strings[0].equalsIgnoreCase("status")) {
            if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
            else {
                if (commandSender.hasPermission("crewchat.chat.status")) {
                    String[] status    = Arrays.copyOfRange(strings, 1, strings.length);
                    String   statusStr = String.join(" ", status);
                    Player player = (Player) commandSender;
                    playerManager.setStatus(player, statusStr);
                    Messages.statusSet(statusStr).send(commandSender);
                }
                else Messages.noPermission().send(commandSender);
            }

        }
        else if (strings[0].equalsIgnoreCase("subscribe")) {
            if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
            else {
                String channelName;
                if (channelManager.getChannels().contains(new Channel(strings[1], null, null, false)))
                    channelName = channelManager.channelFromString(strings[1]).getName();
                else if (channelManager.channelFromNickname(strings[1]) != null)
                    channelName = channelManager.channelFromNickname(strings[1]).getName();
                else {
                    Messages.channelNoExist(strings[1]).send(commandSender);
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.subscribe." + channelName)) {
                    if (playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        Messages.alreadySubscribed(channelName).send(commandSender);
                    else {
                        playerManager.addSubscription((Player) commandSender, channelName);
                        Messages.nowSubscribed(channelName).send(commandSender);
                    }
                }
                else Messages.cantSubscribe(channelName).send(commandSender);
            }
        }
        else if (strings[0].equalsIgnoreCase("unsubscribe")) {
            if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
            else {
                String channelName;
                if (channelManager.getChannels().contains(new Channel(strings[1], null, null, false)))
                    channelName = channelManager.channelFromString(strings[1]).getName();
                else if (channelManager.channelFromNickname(strings[1]) != null)
                    channelName = channelManager.channelFromNickname(strings[1]).getName();
                else {
                    Messages.channelNoExist(strings[1]).send(commandSender);
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.unsubscribe." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        Messages.notSubscribed(channelName).send(commandSender);
                    else if (channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender))
                            .equals(channelManager.channelFromString(channelName)))
                        Messages.cantUnsubscribeActive(channelName).send(commandSender);
                    else {
                        playerManager.removeSubscription((Player) commandSender, channelName);
                        Messages.nowUnsubscribed(channelName).send(commandSender);
                    }
                }
                else Messages.cantUnsubscribe(strings[1]).send(commandSender);
            }
        }
        else if (strings[0].equalsIgnoreCase("switch")) {
            if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
            else {
                String channelName;
                if (channelManager.getChannels().contains(new Channel(strings[1], null, null, false)))
                    channelName = channelManager.channelFromString(strings[1]).getName();
                else if (channelManager.channelFromNickname(strings[1]) != null)
                    channelName = channelManager.channelFromNickname(strings[1]).getName();
                else {
                    Messages.channelNoExist(strings[1]).send(commandSender);
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.switch." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        Messages.notSubscribed(channelName).send(commandSender);
                    else {
                        playerManager.setActiveChannel((Player) commandSender, channelName);
                        Messages.newActiveChannel(channelName, channelManager.channelFromString(channelName).getChatColor()).send(commandSender);
                    }
                }
                else Messages.cantSetActive(channelName).send(commandSender);
            }
        }
        else return false;
        return true;
    }
}
