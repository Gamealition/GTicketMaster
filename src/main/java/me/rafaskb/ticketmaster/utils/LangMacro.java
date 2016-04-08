package me.rafaskb.ticketmaster.utils;

import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;

public class LangMacro {
	public static String replaceId(String string, int id) {
		return replace(string, "<id>", "#" + id);
	}
	
	public static String replaceAmount(String string, int amount) {
		return replace(string, "<amount>", String.valueOf(amount));
	}
	
	public static String replaceSubmitter(String string, String submitter) {
		return replace(string, "<submitter>", submitter);
	}
	
	public static String replaceStatus(String string, TicketStatus status) {
		if(status == null) return string;
		return replace(string, "<status>", status.getScreenName());
	}
	
	public static String replacePriority(String string, TicketPriority priority) {
		if(priority == null) return string;
		return replace(string, "<priority>", priority.getScreenName());
	}
	
	private static String replace(String string, String find, String replacement) {
		if(string == null) return "";
		if(replacement == null) replacement = "";
		string = Lang.get(string);
		return string.replace(find, replacement);
	}
}
