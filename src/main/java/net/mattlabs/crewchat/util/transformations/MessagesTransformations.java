package net.mattlabs.crewchat.util.transformations;

import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.transformation.TransformAction.rename;

public class MessagesTransformations {

    private static final int VERSION_LATEST = 1;

    private MessagesTransformations() {}

    public static ConfigurationTransformation.Versioned create() {
        return ConfigurationTransformation.versionedBuilder()
                .versionKey("_schema-version")
                .addVersion(VERSION_LATEST, zeroToOne())
                .build();
    }

    public static ConfigurationTransformation zeroToOne() {
        return ConfigurationTransformation.builder()
                // Rename chat string field
                .addAction(NodePath.path("chat"), rename("chat-string"))
                // Move Chat fields
                .addAction(NodePath.path(), (path, value) -> {
                    value.node("chat").set(new BlankNode());
                    return null;
                })
                // Base Command
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("base").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("click-for-help"), (path, value) -> new Object[]{"chat", "base", "click-for-help"})
                .addAction(NodePath.path("welcome-to-chat"), (path, value) -> new Object[]{"chat", "base", "welcome-to-chat"})
                // Deafen
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("deafen").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("player-deafened"), (path, value) -> new Object[]{"chat", "base", "player-deafened"})
                .addAction(NodePath.path("player-is-deafened"), (path, value) -> new Object[]{"chat", "base", "player-is-deafened"})
                .addAction(NodePath.path("player-undeafened"), (path, value) -> new Object[]{"chat", "base", "player-undeafened"})
                // General
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("general").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("channel-no-exist"), (path, value) -> new Object[]{"chat", "base", "channel-no-exist"})
                // Info
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("info").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("channel-list-active"), (path, value) -> new Object[]{"chat", "base", "channel-list-active"})
                .addAction(NodePath.path("channel-list-header"), (path, value) -> new Object[]{"chat", "base", "channel-list-header"})
                .addAction(NodePath.path("channel-list-subscribed-header"), (path, value) -> new Object[]{"chat", "base", "channel-list-subscribed-header"})
                .addAction(NodePath.path("click-to-unmute"), (path, value) -> new Object[]{"chat", "base", "click-to-unmute"})
                .addAction(NodePath.path("party-list-header"), (path, value) -> new Object[]{"chat", "base", "party-list-header"})
                .addAction(NodePath.path("party-list-joined-header"), (path, value) -> new Object[]{"chat", "base", "party-list-joined-header"})
                .addAction(NodePath.path("muted-list-header"), (path, value) -> new Object[]{"chat", "base", "muted-list-header"})
                .addAction(NodePath.path("time-remaining"), (path, value) -> new Object[]{"chat", "base", "time-remaining"})
                // Mute
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("mute").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("cant-mute-self"), (path, value) -> new Object[]{"chat", "base", "cant-mute-self"})
                .addAction(NodePath.path("cant-unmute-self"), (path, value) -> new Object[]{"chat", "base", "cant-unmute-self"})
                .addAction(NodePath.path("player-already-muted"), (path, value) -> new Object[]{"chat", "base", "player-already-muted"})
                .addAction(NodePath.path("player-already-unmuted"), (path, value) -> new Object[]{"chat", "base", "player-already-unmuted"})
                .addAction(NodePath.path("player-muted"), (path, value) -> new Object[]{"chat", "base", "player-muted"})
                .addAction(NodePath.path("player-unmuted"), (path, value) -> new Object[]{"chat", "base", "player-unmuted"})
                // Status
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("status").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("status-is"), (path, value) -> new Object[]{"chat", "base", "status-is"})
                .addAction(NodePath.path("status-set"), (path, value) -> new Object[]{"chat", "base", "status-set"})
                .addAction(NodePath.path("status-syntax-error"), (path, value) -> new Object[]{"chat", "base", "status-syntax-error"})
                // Subscription
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("subscription").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("already-subscribed"), (path, value) -> new Object[]{"chat", "base", "already-subscribed"})
                .addAction(NodePath.path("cant-subscribe"), (path, value) -> new Object[]{"chat", "base", "cant-subscribe"})
                .addAction(NodePath.path("cant-unsubscribe"), (path, value) -> new Object[]{"chat", "base", "cant-unsubscribe"})
                .addAction(NodePath.path("cant-unsubscribe-active"), (path, value) -> new Object[]{"chat", "base", "cant-unsubscribe-active"})
                .addAction(NodePath.path("not-subscribed"), (path, value) -> new Object[]{"chat", "base", "not-subscribed"})
                .addAction(NodePath.path("now-subscribed"), (path, value) -> new Object[]{"chat", "base", "now-subscribed"})
                .addAction(NodePath.path("now-unsubscribed"), (path, value) -> new Object[]{"chat", "base", "now-unsubscribed"})
                // Switch
                .addAction(NodePath.path("chat"), (path, value) -> {
                    value.node("switch").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("cant-set-active"), (path, value) -> new Object[]{"chat", "base", "cant-set-active"})
                .addAction(NodePath.path("new-active-channel"), (path, value) -> new Object[]{"chat", "base", "new-active-channel"})
                // Move Broadcast fields
                .addAction(NodePath.path(), (path, value) -> {
                    value.node("broadcast").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("broadcast-message-header"), (path, value) -> new Object[]{"broadcast", "broadcast-message-header"})
                // Move Party fields
                // Rename party string field
                .addAction(NodePath.path("party"), rename("party-string"))
                // Rename preview string field
                .addAction(NodePath.path("preview"), rename("preview-string"))
                .addAction(NodePath.path(), (path, value) -> {
                    value.node("party").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("already-in-party"), (path, value) -> new Object[]{"party", "already-in-party"})
                .addAction(NodePath.path("click-to-pick"), (path, value) -> new Object[]{"party", "click-to-pick"})
                .addAction(NodePath.path("hex-color"), (path, value) -> new Object[]{"party", "hex-color"})
                .addAction(NodePath.path("invalid-color"), (path, value) -> new Object[]{"party", "invalid-color"})
                .addAction(NodePath.path("not-in-party"), (path, value) -> new Object[]{"party", "not-in-party"})
                .addAction(NodePath.path("party-already-exists"), (path, value) -> new Object[]{"party", "party-already-exists"})
                .addAction(NodePath.path("party-channel-already-exists"), (path, value) -> new Object[]{"party", "party-channel-already-exists"})
                .addAction(NodePath.path("party-created"), (path, value) -> new Object[]{"party", "party-created"})
                .addAction(NodePath.path("party-joined"), (path, value) -> new Object[]{"party", "party-joined"})
                .addAction(NodePath.path("party-left"), (path, value) -> new Object[]{"party", "party-left"})
                .addAction(NodePath.path("party-no-exist"), (path, value) -> new Object[]{"party", "party-no-exist"})
                .addAction(NodePath.path("party-player-list-header"), (path, value) -> new Object[]{"party", "party-player-list-header"})
                .addAction(NodePath.path("party-string"), (path, value) -> new Object[]{"party", "party-string"})
                .addAction(NodePath.path("party-will-be-created"), (path, value) -> new Object[]{"party", "party-will-be-created"})
                .addAction(NodePath.path("pick-a-color"), (path, value) -> new Object[]{"party", "pick-a-color"})
                .addAction(NodePath.path("player-joined-party"), (path, value) -> new Object[]{"party", "player-joined-party"})
                .addAction(NodePath.path("player-left-party"), (path, value) -> new Object[]{"party", "player-left-party"})
                .addAction(NodePath.path("preview-string"), (path, value) -> new Object[]{"party", "preview-string"})
                // Move Private Message fields'
                .addAction(NodePath.path(), (path, value) -> {
                    value.node("private-message").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("cant-message-self"), (path, value) -> new Object[]{"party", "cant-message-self"})
                .addAction(NodePath.path("click-to-reply"), (path, value) -> new Object[]{"party", "click-to-reply"})
                .addAction(NodePath.path("no-pm-received"), (path, value) -> new Object[]{"party", "no-pm-received"})
                .addAction(NodePath.path("player-no-exist"), (path, value) -> new Object[]{"party", "player-no-exist"})
                .addAction(NodePath.path("private-message-header"), (path, value) -> new Object[]{"party", "private-message-header"})
                // Move General fields
                // Rename string fields
                .addAction(NodePath.path("channel"), rename("channel-string"))
                .addAction(NodePath.path("help"), rename("help-string"))
                .addAction(NodePath.path("status"), rename("status-string"))
                .addAction(NodePath.path("you"), rename("you-string"))
                .addAction(NodePath.path(), (path, value) -> {
                    value.node("general").set(new BlankNode());
                    return null;
                })
                .addAction(NodePath.path("channel-string"), (path, value) -> new Object[]{"general", "channel-string"})
                .addAction(NodePath.path("chat-string"), (path, value) -> new Object[]{"general", "chat-string"})
                .addAction(NodePath.path("help-string"), (path, value) -> new Object[]{"general", "help-string"})
                .addAction(NodePath.path("no-permission"), (path, value) -> new Object[]{"general", "no-permission"})
                .addAction(NodePath.path("plugin-not-configured"), (path, value) -> new Object[]{"general", "plugin-not-configured"})
                .addAction(NodePath.path("perm-error"), (path, value) -> new Object[]{"general", "perm-error"})
                .addAction(NodePath.path("status-string"), (path, value) -> new Object[]{"general", "status-string"})
                .addAction(NodePath.path("you-string"), (path, value) -> new Object[]{"general", "you-string"})
                .build();
    }

    @ConfigSerializable
    private static class BlankNode {}
}
