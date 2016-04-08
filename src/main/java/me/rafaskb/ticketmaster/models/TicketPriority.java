package me.rafaskb.ticketmaster.models;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import me.rafaskb.ticketmaster.utils.Perm;

public enum TicketPriority {
	LOW,
	NORMAL,
	HIGH,
	CRITICAL;
	
	public String getScreenName() {
		return WordUtils.capitalizeFully(this.toString());
	}
	
	public TicketPriority getNext() {
		switch(this) {
			case LOW:
				return NORMAL;
			case NORMAL:
				return HIGH;
			default:
				return CRITICAL;
		}
	}
	
	public TicketPriority getPrevious() {
		switch(this) {
			case CRITICAL:
				return HIGH;
			case HIGH:
				return NORMAL;
			default:
				return LOW;
		}
	}
	
	public String getRequiredPermission() {
		switch(this) {
			case CRITICAL:
				return Perm.PRIORITY_CRITICAL;
			case HIGH:
				return Perm.PRIORITY_HIGH;
			case NORMAL:
				return Perm.PRIORITY_NORMAL;
			case LOW:
				return Perm.PRIORITY_LOW;
			default:
				return "";
		}
	}
	
	public ChatColor getColor() {
		switch(this) {
			case LOW:
				return ChatColor.GREEN;
			case NORMAL:
				return ChatColor.YELLOW;
			case HIGH:
				return ChatColor.GOLD;
			case CRITICAL:
				return ChatColor.DARK_RED;
			default:
				return ChatColor.WHITE;
		}
	}
	
	public String getColoredTag() {
		switch(this) {
			case LOW:
				return getColor() + "[L]";
			case NORMAL:
				return getColor() + "[N]";
			case HIGH:
				return getColor() + "[H]";
			case CRITICAL:
				return getColor() + "[!!!]";
			default:
				return getColor() + "[?]";
		}
	}
	
}
