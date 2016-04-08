package me.rafaskb.ticketmaster.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perm {
	public static final String USER_CREATE = "ticketmaster.user.create";
	public static final String USER_MANAGE = "ticketmaster.user.manage";
	public static final String HELPER_NOTIFY = "ticketmaster.helper.notify";
	public static final String HELPER_TELEPORT = "ticketmaster.helper.teleport";
	public static final String HELPER_STATUS = "ticketmaster.helper.status";
	public static final String HELPER_DELETE = "ticketmaster.helper.delete";
	public static final String PRIORITY_LOW = "ticketmaster.priority.low";
	public static final String PRIORITY_NORMAL = "ticketmaster.priority.normal";
	public static final String PRIORITY_HIGH = "ticketmaster.priority.high";
	public static final String PRIORITY_CRITICAL = "ticketmaster.priority.critical";
	public static final String RELOAD = "ticketmaster.reload";
	
	public static boolean check(Player player, String perm) {
		if(player != null)
			return player.hasPermission(perm);
		return false;
	}
	
	public static boolean check(CommandSender sender, String perm) {
		return sender.hasPermission(perm);
	}
}
