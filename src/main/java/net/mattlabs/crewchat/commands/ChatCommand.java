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
                        commandSender.spigot().sendMessage(Messages.channelListHeader());
                        for (Channel channel : channelManager.getChannels())
                            commandSender.spigot().sendMessage(Messages.channelListEntry(channel.getName(), channel.getChatColor()));
                        commandSender.spigot().sendMessage(Messages.channelListActive(playerManager.getActiveChannel((Player) commandSender),
                                channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender)).getChatColor()));
                        commandSender.spigot().sendMessage(Messages.channelListSubscribedHeader());
                        for (String channel : playerManager.getSubscribedChannels((Player) commandSender))
                            commandSender.spigot().sendMessage(Messages.channelListEntry(channel, channelManager.channelFromString(channel).getChatColor()));
                    }
                    else commandSender.spigot().sendMessage(Messages.noPermission());
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
                            commandSender.spigot().sendMessage(Messages.channelInfo(requestedChannel.getName(),
                                    requestedChannel.getNickname(),
                                    requestedChannel.getChatColor().name(),
                                    requestedChannel.getChatColor()));
                        }
                        else commandSender.spigot().sendMessage(Messages.noPermission());
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
                            commandSender.spigot().sendMessage(Messages.channelNoExist(strings[2]));
                        }
                        else commandSender.spigot().sendMessage(Messages.noPermission());
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
                    commandSender.spigot().sendMessage(Messages.statusSet(statusStr));
                }
                else commandSender.spigot().sendMessage(Messages.noPermission());
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
                    commandSender.spigot().sendMessage(Messages.channelNoExist(strings[1]));
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.subscribe." + channelName)) {
                    if (playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.alreadySubscribed(channelName));
                    else {
                        playerManager.addSubscription((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.nowSubscribed(channelName));
                    }
                }
                else commandSender.spigot().sendMessage(Messages.cantSubscribe(channelName));
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
                    commandSender.spigot().sendMessage(Messages.channelNoExist(strings[1]));
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.unsubscribe." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.notSubscribed(channelName));
                    else if (channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender))
                            .equals(channelManager.channelFromString(channelName)))
                        commandSender.spigot().sendMessage(Messages.cantUnsubscribeActive(channelName));
                    else {
                        playerManager.removeSubscription((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.nowUnsubscribed(channelName));
                    }
                }
                else commandSender.spigot().sendMessage(Messages.cantUnsubscribe(strings[1]));
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
                    commandSender.spigot().sendMessage(Messages.channelNoExist(strings[1]));
                    return true;
                }
                if (commandSender.hasPermission("crewchat.chat.switch." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.notSubscribed(channelName));
                    else {
                        playerManager.setActiveChannel((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.newActiveChannel(channelName, channelManager.channelFromString(channelName).getChatColor()));
                    }
                }
                else commandSender.spigot().sendMessage(Messages.cantSetActive(channelName));
            }
        }
        else return false;
        return true;
    }
}
