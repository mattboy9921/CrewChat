package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.google.common.base.CaseFormat;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.mattlabs.crewchat.util.TextColorSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@CommandAlias("crewchat|cc")
@CommandPermission("crewchat.use")
public class CrewChatCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final BukkitAudiences platform = crewChat.getPlatform();

    private final ChannelManager channelManager = crewChat.getChannelManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();

    ArrayList<String> properties;
    HashMap<String, Method> channelMethods;
    HashMap<String, Field> channelFields;

    public CrewChatCommand() {
        // Get Channel Properties
        channelMethods = new HashMap<>();
        for (Method method : Channel.class.getDeclaredMethods()) channelMethods.put(method.getName(), method);
        channelFields = new HashMap<>();
        for (Field field : Channel.class.getDeclaredFields()) channelFields.put(field.getName(), field);
        properties = new ArrayList<>();

        // Register ACF Completions
        crewChat.getPaperCommandManager().getCommandCompletions().registerStaticCompletion("properties", () -> {
            for (String field : channelFields.keySet())
                // Camel case to kebab case
                properties.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, field));
            return properties;
        });

        crewChat.getPaperCommandManager().getCommandCompletions().registerAsyncCompletion("values", context -> {
            String property = context.getContextValueByName(String.class, "property");
            Type type;
            type = channelFields.get(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, property)).getType();
            if (type.equals(boolean.class)) return new ArrayList<>(Arrays.asList("true", "false"));
            else if (type.equals(TextColor.class)) return NamedTextColor.NAMES.keys();
            else return new ArrayList<>();
        });
    }

    @Default
    @Description("CrewChat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatBaseCommand());
        else CrewChat.getInstance().getLogger().info("Version " +
                CrewChat.getInstance().getDescription().getVersion() + ". For help, run '/crewchat help'");
    }

    @Subcommand("reload")
    @Description("Reloads CrewChat configuration files.")
    @CommandPermission("crewchat.reload")
    public void onReload(CommandSender commandSender) {
        CrewChat.getInstance().reload();

        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().configReloaded());
        CrewChat.getInstance().getLogger().info("CrewChat reloaded.");
    }

    @Subcommand("info")
    @CommandPermission("crewchat.info")
    public class Info extends BaseCommand {

        @Default
        @Description("List all info for the CrewChat plugin.")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                // Header
                platform.player(player).sendMessage(crewChat.getMessages().crewChatInfoHeader());
                // Channels
                platform.player(player).sendMessage(crewChat.getMessages().crewChatChannelsLoaded(String.valueOf(channelManager.getChannels().size())));
                // Players
                platform.player(player).sendMessage(crewChat.getMessages().crewChatPlayersLoaded(String.valueOf(playerManager.getPlayerCount())));
                // Online Players
                platform.player(player).sendMessage(crewChat.getMessages().crewChatOnlinePlayersLoaded(String.valueOf(playerManager.getOnlinePlayerCount())));
                // Discord Integration
                platform.player(player).sendMessage(crewChat.getMessages().crewChatDiscordIntegration(crewChat.getDiscordSRVEnabled()));
            }
        }

        @Subcommand("channel")
        @Description("Lists info about specified channel.")
        @CommandCompletion("@channels")
        public void onChannel(CommandSender commandSender, @Optional Channel channel) {
            if (channel == null) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    platform.player(player).sendMessage(crewChat.getMessages().crewChatChannelListHeader());
                    for (Channel listChannel : channelManager.getChannels())
                        platform.player(player).sendMessage(crewChat.getMessages().crewChatChannelListEntry(listChannel.getName(), listChannel.getTextColor()));
                }
                else {
                    CrewChat.getInstance().getLogger().info("Channel list: (Run /crewchat info channel [channel] for more info)");
                    for (Channel listChannel : channelManager.getChannels())
                        CrewChat.getInstance().getLogger().info(" - " + listChannel.getName());
                }
            }
            else {
                // Check if channel exists
                if (!channelManager.getChannels().contains(channel)) {
                    if (commandSender instanceof Player)
                        platform.player((Player) commandSender).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
                    else CrewChat.getInstance().getLogger().info("Channel " + channel.getName() + " doesn't exist!");
                }
                else {
                    Channel requestedChannel = channelManager.channelFromString(channel.getName());

                    if (commandSender instanceof Player)
                        platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatChannelInfo(requestedChannel.getName(),
                                requestedChannel.getDescription(),
                                requestedChannel.getTextColor(),
                                playerManager.getSubscribedPlayers(requestedChannel.getName()).size(),
                                requestedChannel.isAutoSubscribe(),
                                requestedChannel.isExcludeFromDiscord(),
                                requestedChannel.isShowChannelNameDiscord()));
                    else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                            + " info: " +
                            "\n - Name: " + requestedChannel.getName() +
                            "\n - Chat Color: " + requestedChannel.getTextColor().toString() +
                            "\n - Subscribed players: " + playerManager.getSubscribedPlayers(requestedChannel.getName()).size() +
                            "\n - Auto Subscribe: " + (requestedChannel.isAutoSubscribe() ? "True" : "False") +
                            "\n - Exclude from Discord: " + (requestedChannel.isExcludeFromDiscord() ? "True" : "False") +
                            "\n - Show Channel Name on Discord: " + (requestedChannel.isShowChannelNameDiscord() ? "True" : "False"));
                }
            }
        }
    }

    @Subcommand("set")
    @CommandPermission("crewchat.set")
    public class Set extends BaseCommand {

        @Subcommand("channel")
        @Description("Allows player to set properties of channel.")
        @CommandCompletion("@channels @properties @values")
        public void onChannel(CommandSender commandSender, Channel channel, @Single String property, String value) {
            // Channel doesn't exist
            if (!channelManager.getChannels().contains(channel)) platform.sender(commandSender).sendMessage(crewChat.getMessages().crewChatChannelNoExist(channel.getName()));
            // Property doesn't exist
            else if (!properties.contains(property)) platform.sender(commandSender).sendMessage(crewChat.getMessages().crewChatPropertyNoExist(property));
            else {
                Object valueObj = value;
                Channel actualChannel = channelManager.channelFromString(channel.getName());
                try {
                    // Change property
                    if (channelFields.get(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, property)).getType().equals(boolean.class))
                        valueObj = Boolean.parseBoolean(value);
                    else if (channelFields.get(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, property)).getType().equals(TextColor.class))
                        valueObj = TextColorSerializer.INSTANCE.deserialize(TextColor.class, value);
                    channelMethods.get("set" + CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, property)).invoke(actualChannel, valueObj);
                    // Save and reload
                    crewChat.getConfigurateManager().save("config.conf");
                    crewChat.getChannelManager().reloadChannel(actualChannel);

                    platform.sender(commandSender).sendMessage(crewChat.getMessages().crewChatPropertyChanged(actualChannel.getName(), property, value));
                }
                catch (IllegalAccessException | InvocationTargetException | NullPointerException | SerializationException e) {
                    e.printStackTrace();
                    throw new InvalidCommandArgument();
                }
            }
        }

    }

    @Subcommand("help")
    @Description("CrewChat help command.")
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatHelpCommand());
        else CrewChat.getInstance().getLogger().info("Command Help:\n"
                + "Alias: /cc <args>\n"
                + "/crewchat - Base CrewChat command.\n"
                + "/crewchat help - Shows this screen.\n"
                + "/crewchat reload - Reloads configuration files.");
    }
}
