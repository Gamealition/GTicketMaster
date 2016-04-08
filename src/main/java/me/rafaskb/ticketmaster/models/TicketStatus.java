package me.rafaskb.ticketmaster.models;

import org.bukkit.ChatColor;

public enum TicketStatus {
	PENDING,
	ONHOLD,
	CLAIMED,
	CLOSED;
	
	public String getScreenName() {
		switch(this) {
			case CLAIMED:
				return "Claimed";
			case CLOSED:
				return "Closed";
			case ONHOLD:
				return "On Hold";
			case PENDING:
				return "Pending";
			default:
				return "";	
		}
	}
	
	public ChatColor getColor() {
		switch(this) {
			case PENDING:
				return ChatColor.AQUA;
			case ONHOLD:
				return ChatColor.BLUE;
			case CLAIMED:
				return ChatColor.DARK_PURPLE;
			case CLOSED:
				return ChatColor.RED;
			default:
				return ChatColor.WHITE;
		}
	}
	
}
