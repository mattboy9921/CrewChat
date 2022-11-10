package net.mattlabs.crewchat.util;

import java.util.HashMap;
import java.util.Map;

public class MsgManager {

    private final Map<String, String> replyMap;

    public MsgManager() {
        replyMap = new HashMap<>();
    }

    public void updatePlayer(String recipient, String sender) {
        replyMap.put(recipient, sender);
    }

    public void removePlayer(String recipient) {
        replyMap.remove(recipient);
    }

    public String getLastSender(String sender) {
        return replyMap.get(sender);
    }

    public boolean playerExists(String sender) {
        return replyMap.containsKey(sender);
    }
}
