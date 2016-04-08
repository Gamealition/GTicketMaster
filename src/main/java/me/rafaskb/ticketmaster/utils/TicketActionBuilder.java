package me.rafaskb.ticketmaster.utils;

import org.bukkit.ChatColor;

import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;

public class TicketActionBuilder {
	
	public static String closed(String performer, int id, String reason) {
		if(reason == null || reason.equals(""))
			return closed(performer, id);
		return build("&a&o" + performer + " has closed your ticket &e&o#" + id + "&f&o for &c&o" + reason);
	}
	
	public static String closed(String performer, int id) {
		return build("&a&o" + performer + " has closed your ticket &e&o#" + id);
	}
	
	public static String reopened(String performer, int id) {
		return build("&a&o" + performer + " has reopened your ticket &e&o#" + id);
	}
	
	public static String claimed(String performer, int id) {
		return build("&a&o" + performer + " has claimed your ticket &e&o#" + id);
	}
	
	public static String commented(String performer, int id, String comment) {
		return build("&a&o" + performer + " has commented on your ticket &e&o#" + id + "&a&o: &f&o" + comment);
	}
	
	public static String statusChanged(String performer, int id, TicketStatus status) {
		if(status != null)
			switch(status) {
				case CLAIMED:
					return claimed(performer, id);
				case CLOSED:
					return closed(performer, id);
				default:
					String s = "&a&o" + performer + " has changed the status of your ticket &e&o#" + id +
						"&a&o to " + status.getColor() + ChatColor.ITALIC + status.getScreenName();
					return build(s);
			}
		return "";
	}
	
	public static String priorityChanged(String performer, int id, TicketPriority priority) {
		String s = "&a&o" + performer + " has changed the priority of your ticket &e&o#" + id +
			"&a&o to " + priority.getColor() + ChatColor.ITALIC + priority.getScreenName();
		return build(s);
	}
	
	private static String build(String s) {
		return ChatColor.translateAlternateColorCodes('&', s) + ChatColor.RESET;
	}
}
