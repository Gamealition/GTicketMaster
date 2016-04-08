package me.rafaskb.ticketmaster.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CooldownManager {
	public static final int COOLDOWN_NEW_TICKET_IN_SECONDS = 60;
	public static final int COOLDOWN_COMMENT_IN_SECONDS = 10;
	
	private static Map<String, Long> mapNewTicket = new HashMap<>();
	private static Map<String, Long> mapComment = new HashMap<>();
	
	public static boolean isUnderCooldown(String playerName, CooldownType type) {
		long now = System.currentTimeMillis();
		Map<String, Long> map = type.getMap();
		
		// Not in the list
		if(!map.containsKey(playerName))
			return false;
		
		// If still under cooldown
		if(now < map.get(playerName))
			return true;
		
		// Not under cooldown anymore, we can remove this entry
		map.remove(playerName);
		return false;
	}
	
	public static void addCooldown(String playerName, CooldownType type) {
		long now = System.currentTimeMillis();
		Map<String, Long> map = type.getMap();
		
		// Add cooldown in milliseconds
		map.put(playerName, now + type.getCooldownInMillis());
	}
	
	public static void cleanup() {
		// Iterate through all maps
		for(CooldownType type : CooldownType.values()) {
			Map<String, Long> map = type.getMap();
			
			// Iterate through all entries
			Iterator<String> keyIterator = map.keySet().iterator();
			while(keyIterator.hasNext()) {
				String key = keyIterator.next();
				Long value = map.get(key);
				if(value != null) {
					// If cooldown has expired, remove entry
					if(System.currentTimeMillis() > value)
						keyIterator.remove();
				}
			}
		}
	}
	
	public static enum CooldownType {
		NEW_TICKET,
		COMMENT;
		
		Map<String, Long> getMap() {
			switch(this) {
				case COMMENT:
					return mapComment;
				case NEW_TICKET:
					return mapNewTicket;
				default:
					return new HashMap<>();
			}
		}
		
		int getCooldownInSeconds() {
			switch(this) {
				case COMMENT:
					return COOLDOWN_COMMENT_IN_SECONDS;
				case NEW_TICKET:
					return COOLDOWN_NEW_TICKET_IN_SECONDS;
				default:
					return 0;
			}
		}
		
		long getCooldownInMillis() {
			return getCooldownInSeconds() * 1000;
		}
	}
}
