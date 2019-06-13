package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Party;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

public class PartyManager {

    private ArrayList<Party> parties;

    public PartyManager() {
        parties = new ArrayList<>();
    }

    public void clearParties() {
        parties.clear();
    }

    public void addParty(String name, ChatColor chatColor) {
        parties.add(new Party(name, chatColor));
    }

    public void removeParty(String name) {
        parties.remove(new Party(name, null));
    }
    
    public boolean partyExists(String name) {
        return parties.contains(new Party(name, null));
    }

    public ArrayList<Party> getParties() {
        return parties;
    }
    
    public ArrayList<String> getPartyNames() {
        ArrayList<String> partyNames = new ArrayList<>();
        for (Party party : parties) partyNames.add(party.getName());
        return partyNames;
    }

    public Party partyFromString(String partyName) {
        Party party = new Party(partyName, null);
        if (parties.contains(party)) return parties.get(parties.indexOf(party));
        else return null;
    }

    public ChatColor getChatColor(Party party) {
        if (parties.contains(party)) return parties.get(parties.indexOf(party)).getChatColor();
        else return null;
    }
}
