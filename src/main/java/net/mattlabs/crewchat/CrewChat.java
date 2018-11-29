package net.mattlabs.crewchat;

import github.scarsz.discordsrv.DiscordSRV;
import net.mattlabs.crewchat.commands.*;
import net.mattlabs.crewchat.listeners.ChatListener;
import net.mattlabs.crewchat.listeners.DiscordSRVListener;
import net.mattlabs.crewchat.listeners.JoinListener;
import net.mattlabs.crewchat.listeners.QuitListener;
import net.mattlabs.crewchat.util.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CrewChat extends JavaPlugin{

    private static CrewChat instance;

    private ConfigManager configManager;
    private ChannelManager channelManager;
    private PlayerManager playerManager;
    private MsgManager msgManager;
    private ChatSender chatSender;
    private MeSender meSender;
    private static Chat chat = null;
    private static Permission perms = null;

    private CrewChatCommand crewChatCommand;
    private ChatCommand chatCommand;
    private MeCommand meCommand;
    private MsgCommand msgCommand;
    private ReplyCommand replyCommand;

    private DiscordSRVListener discordSRVListener;
    private boolean discordSRVEnabled;

    public void onEnable() {
        instance = this;

        // Vault Check
        if (!hasVault()) {
            this.getLogger().severe(String.format("Disabled due to no Vault dependency found!"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // DiscordSRV Check
        if (!hasDiscordSRV()) {
            this.getLogger().info(String.format("DiscordSRV not detected, disabling integration."));
            discordSRVEnabled = false;
        }
        else {
            this.getLogger().info(String.format("DiscordSRV detected, enabling integration."));
            discordSRVEnabled = true;
        }

        // Vault Setup
        setupChat();
        setupPermissions();

        // Configuration Section
        configManager = new ConfigManager(this);
        configManager.loadConfigFiles(
                new ConfigManager.ConfigPath(
                        "config.yml",
                        "config.yml",
                        "config.yml"),
                new ConfigManager.ConfigPath(
                        "playerdata.yml",
                        "playerdata.yml",
                        "playerdata.yml"));
        configManager.saveAllConfigs(false);

        // Load Channels
        channelManager = new ChannelManager();
        channelManager.loadChannels();

        // Load Players
        playerManager = new PlayerManager();
        playerManager.loadPlayers();

        // Load Private Messages
        msgManager = new MsgManager();

        // Load Senders
        chatSender = new ChatSender();
        meSender = new MeSender();

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        // Register Commands
        crewChatCommand = new CrewChatCommand();
        getCommand("crewchat").setExecutor(crewChatCommand);
        chatCommand = new ChatCommand();
        getCommand("chat").setExecutor(chatCommand);
        meCommand = new MeCommand();
        getCommand("me").setExecutor(meCommand);
        msgCommand = new MsgCommand();
        getCommand("msg").setExecutor(msgCommand);
        replyCommand = new ReplyCommand();
        getCommand("reply").setExecutor(replyCommand);

        // DiscordSRV Integration
        if (discordSRVEnabled) {
            discordSRVListener = new DiscordSRVListener();
            DiscordSRV.api.subscribe(discordSRVListener);
        }

        this.getLogger().info("CrewChat loaded - By mattboy9921");
    }

    public void onDisable() {
        // DiscordSRV Integration
        if (discordSRVEnabled) DiscordSRV.api.unsubscribe(discordSRVListener);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static CrewChat getInstance() {
        return instance;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public MsgManager getMsgManager() {
        return msgManager;
    }

    public ChatSender getChatSender() {
        return chatSender;
    }

    public MeSender getMeSender() {
        return meSender;
    }

    public boolean getDiscordSRVEnabled() {
        return discordSRVEnabled;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    // Vault Helper Methods

    private boolean hasVault() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    // DiscordSRV Helper Method
    private boolean hasDiscordSRV() {
        return getServer().getPluginManager().getPlugin("DiscordSRV") != null;
    }
}
