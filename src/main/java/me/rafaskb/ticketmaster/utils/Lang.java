package me.rafaskb.ticketmaster.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Lang extends LangConfig {
	/* General Messages */
	private static final String PREFIX_NORMAL = "prefix_normal";
	private static final String PREFIX_ERROR = "prefix_error";
	private static final String ERROR_MESSAGE = "error_message";
	private static final String TICKET_NOT_FOUND = "ticket_not_found";
	public static final String TICKET_MANAGEMENT_NO_PERMS = "ticket_management_no_perms";
	public static final String CANNOT_RUN_FROM_CONSOLE = "cannot_run_from_console";
	public static final String USAGE_INFORMATION = "usage_information";
	public static final String UNKNOWN_COMMAND = "unknown_command";
	public static final String RELOAD_MESSAGE = "reload_message";
	public static final String BROADCAST_PENDING_TICKETS = "broadcast_pending_tickets";
	
	/* New Command */
	public static final String NEW_COMMAND_USAGE = "new_command_usage";
	public static final String NEW_COMMAND_SUCCESS = "new_command_success";
	public static final String NEW_COMMAND_FAILURE = "new_command_failure";
	public static final String NEW_COMMAND_COOLDOWN = "new_command_cooldown";
	public static final String NEW_TICKET_BROADCAST = "new_ticket_broadcast";
	
	/* Status Command */
	public static final String STATUS_COMMAND_SUCCESS = "status_command_success";
	public static final String STATUS_COMMAND_FAILURE = "status_command_failure";
	public static final String STATUS_COMMAND_USAGE = "status_command_usage";
	
	/* Priority Command */
	public static final String PRIORITY_COMMAND_SUCCESS = "priority_command_success";
	public static final String PRIORITY_COMMAND_FAILURE = "priority_command_failure";
	public static final String PRIORITY_COMMAND_USAGE = "priority_command_usage";
	public static final String ELEVATE_COMMAND_USAGE = "elevate_command_usage";
	public static final String LOWER_COMMAND_USAGE = "lower_command_usage";
	
	/* Teleport Command */
	public static final String TELEPORT_COMMAND_USAGE = "teleport_command_usage";
	public static final String TELEPORT_COMMAND_SUCCESS = "teleport_command_success";
	public static final String TELEPORT_COMMAND_FAILURE = "teleport_command_failure";
	
	/* Comment Command */
	public static final String COMMENT_COMMAND_USAGE = "comment_command_usage";
	public static final String COMMENT_COMMAND_SUCCESS = "comment_command_success";
	public static final String COMMENT_COMMAND_FAILURE = "comment_command_failure";
	public static final String COMMENT_COMMAND_CLOSED = "comment_command_closed";
	public static final String COMMENT_COMMAND_COOLDOWN = "comment_command_cooldown";
	
	/* Check Command */
	public static final String CHECK_COMMAND_USAGE = "check_command_usage";
	public static final String CHECK_COMMAND_FAILURE = "check_command_failure";
	
	/* Delete Command */
	public static final String DELETE_COMMAND_USAGE = "delete_command_usage";
	public static final String DELETE_COMMAND_SUCCESS = "delete_command_success";
	public static final String DELETE_COMMAND_FAILURE = "delete_command_failure";
	
	/* List Command */
	public static final String LIST_COMMAND_USAGE = "list_command_usage";
	public static final String LIST_COMMAND_NO_TICKETS_USER = "list_command_no_tickets_user";
	public static final String LIST_COMMAND_NO_TICKETS = "list_command_no_tickets";
	public static final String LIST_COMMAND_HEADER = "list_command_header";
	
	/* Claim Command */
	public static final String CLAIM_COMMAND_USAGE = "claim_command_usage";
	public static final String CLAIM_COMMAND_CLOSED = "claim_command_closed";
	public static final String CLAIM_COMMAND_SUCCESS = "claim_command_success";
	public static final String CLAIM_COMMAND_FAILURE = "claim_command_failure";
	
	/* Reopen Command */
	public static final String REOPEN_COMMAND_USAGE = "reopen_command_usage";
	public static final String REOPEN_COMMAND_NOT_CLOSED = "reopen_command_not_closed";
	public static final String REOPEN_COMMAND_SUCCESS = "reopen_command_success";
	public static final String REOPEN_COMMAND_FAILURE = "reopen_command_failure";
	
	/* Close Command */
	public static final String CLOSE_COMMAND_USAGE = "close_command_usage";
	public static final String CLOSE_COMMAND_CLOSED = "close_command_closed";
	public static final String CLOSE_COMMAND_SUCCESS = "close_command_success";
	public static final String CLOSE_COMMAND_FAILURE = "close_command_failure";
	
	/* Help Command + Descriptions */
	public static final String HELP_COMMAND_HEADER = "help_command_header";
	public static final String HELP_COMMAND_DESCRIPTION = "help_command_description";
	public static final String CHECK_COMMAND_DESCRIPTION = "check_command_description";
	public static final String LIST_COMMAND_DESCRIPTION = "list_command_description";
	public static final String NEW_COMMAND_DESCRIPTION = "new_command_description";
	public static final String CLOSE_COMMAND_DESCRIPTION = "close_command_description";
	public static final String REOPEN_COMMAND_DESCRIPTION = "reopen_command_description";
	public static final String DELETE_COMMAND_DESCRIPTION = "delete_command_description";
	public static final String CLAIM_COMMAND_DESCRIPTION = "claim_command_description";
	public static final String COMMENT_COMMAND_DESCRIPTION = "comment_command_description";
	public static final String TELEPORT_COMMAND_DESCRIPTION = "teleport_command_description";
	public static final String STATUS_COMMAND_DESCRIPTION = "status_command_description";
	public static final String PRIORITY_COMMAND_DESCRIPTION = "priority_command_description";
	public static final String ELEVATE_COMMAND_DESCRIPTION = "elevate_command_description";
	public static final String LOWER_COMMAND_DESCRIPTION = "lower_command_description";
	public static final String RELOAD_COMMAND_DESCRIPTION = "reload_command_description";
	
	/* Methods */
	public static String get(String key) {
		if(!getConfig().contains(key))
			saveDefaultValue(key);
		
		String value = getConfig().getString(key, key);
		return ChatColor.translateAlternateColorCodes('&', value + "&r");
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sendMessage(sender, message, true);
	}
	
	public static void sendMessage(CommandSender sender, String message, boolean useHeader) {
		message = get(message);
		if(useHeader)
			message = get(PREFIX_NORMAL) + message;
		sender.sendMessage(message);
	}
	
	public static void sendErrorMessage(CommandSender sender) {
		sendErrorMessage(sender, ERROR_MESSAGE);
	}
	
	public static void sendErrorMessage(CommandSender sender, String message) {
		sendErrorMessage(sender, message, true);
	}
	
	public static void sendErrorMessage(CommandSender sender, String message, boolean useHeader) {
		if(useHeader) message = get(PREFIX_ERROR) + get(message);
		sender.sendMessage(message);
	}
	
	public static void sendTicketNotFoundMessage(CommandSender sender, int id) {
		String msg = LangMacro.replaceId(TICKET_NOT_FOUND, id);
		sendErrorMessage(sender, msg);
	}
	
}
