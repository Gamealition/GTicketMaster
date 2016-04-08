package me.rafaskb.ticketmaster.utils;

import org.bukkit.ChatColor;

import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;

public class TicketLogBuilder {
	
	public static String closed(String reason) {
		if(reason == null || reason.equals(""))
			return closed();
		ChatColor c = TicketStatus.CLOSED.getColor();
		return build(c + "[Ticket closed for: &o" + reason + c + "]");
	}
	
	public static String closed() {
		return build(TicketStatus.CLOSED.getColor() + "[Ticket closed]");
	}
	
	public static String pending() {
		return build(TicketStatus.PENDING.getColor() + "[Ticket set to Pending]");
	}
	
	public static String reopened() {
		return build(TicketStatus.PENDING.getColor() + "[Ticket reopened]");
	}
	
	public static String claimed() {
		return build(TicketStatus.CLAIMED.getColor() + "[Ticket claimed]");
		
	}
	
	public static String onHold() {
		return build(TicketStatus.ONHOLD.getColor() + "[Ticket set to On Hold]");
	}
	
	public static String statusChanged(TicketStatus status) {
		if(status != null)
			switch(status) {
				case CLAIMED:
					return claimed();
				case CLOSED:
					return closed();
				case ONHOLD:
					return onHold();
				case PENDING:
					return pending();
			}
		return "";
	}
	
	public static String priorityChanged(TicketPriority priority) {
		return build("&3[Priority changed to " + priority.getColor() + priority.getScreenName() + "&3]");
	}
	
	private static String build(String s) {
		return ChatColor.translateAlternateColorCodes('&', s) + ChatColor.RESET;
	}
	
}
