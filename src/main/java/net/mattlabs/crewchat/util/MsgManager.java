package net.mattlabs.crewchat.util;

import java.util.HashMap;
import java.util.Map;

public class MsgManager {

    Map<String, String> replyMap;

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

    public boolean playerExists(String recipient) {
        if (replyMap.containsKey(recipient)) return true;
        else return false;
    }
}
