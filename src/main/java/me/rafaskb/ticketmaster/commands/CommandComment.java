package me.rafaskb.ticketmaster.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.TicketComment;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.CooldownManager;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;
import me.rafaskb.ticketmaster.utils.TicketActionBuilder;
import me.rafaskb.ticketmaster.utils.Utils;
import me.rafaskb.ticketmaster.utils.CooldownManager.CooldownType;

public class CommandComment extends Command {
	private static final int INDEX_ID = 1;
	private static final int INDEX_MESSAGE_START = 2;
	
	public CommandComment() {
		super(new String[] {Perm.PRIORITY_LOW, Perm.USER_MANAGE});
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length < 3) {
			Lang.sendErrorMessage(sender, Lang.COMMENT_COMMAND_USAGE);
			return;
		}
		
		// Check cooldown
		if(CooldownManager.isUnderCooldown(sender.getName(), CooldownType.COMMENT)) {
			Lang.sendErrorMessage(sender, Lang.COMMENT_COMMAND_COOLDOWN);
			return;
		}
		

		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.COMMENT_COMMAND_USAGE);
			return;
		}
		
		// If ID not found, show 'ticket_not_found' error (replace <id>)
		if(!Controller.ticketExists(id)) {
			Lang.sendTicketNotFoundMessage(sender, id);
			return;
		}
		
		/*
		 * If sender is the ticket submitter, continue.
		 * Otherwise, check the permission to manage the ticket.
		 */
		boolean ownTicket = false;
		checkSubmitter:
		{
			String senderName = sender.getName();
			String submitterName = Controller.getTicketSubmitter(id);
			if(senderName.equalsIgnoreCase(submitterName)) {
				ownTicket = true;
				break checkSubmitter;
			}
			
			// Sender is NOT the submitter. Check management permission.
			else {
				TicketPriority priority = Controller.getTicketPriority(id);
				if(!Perm.check(sender, priority.getRequiredPermission())) {
					Lang.sendErrorMessage(sender, Lang.TICKET_MANAGEMENT_NO_PERMS);
					return;
				}
			}
		}
		
		// Ticket is closed
		TicketStatus status = Controller.getTicketStatus(id);
		if(status == TicketStatus.CLOSED) {
			Lang.sendErrorMessage(sender, Lang.COMMENT_COMMAND_CLOSED);
			return;
		}
		
		// Create comment
		String message = Utils.convertArgumentsToString(args, INDEX_MESSAGE_START);
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = ChatColor.stripColor(message);
		TicketComment comment = new TicketComment(id, sender.getName(), message);
		
		// Push to database
		boolean success = Controller.pushTicketComment(comment);
		
		// Success
		if(success) {
			// Message command sender
			String msg = LangMacro.replaceId(Lang.COMMENT_COMMAND_SUCCESS, id);
			Lang.sendMessage(sender, msg);
			
			// Message ticket submitter
			if(!ownTicket) {
				String actionMessage = TicketActionBuilder.commented(sender.getName(), id, message);
				String ticketSubmitter = Controller.getTicketSubmitter(id);
				if(!Utils.sendActionMessage(ticketSubmitter, actionMessage))
					Controller.insertPendingMessage(ticketSubmitter, actionMessage);
			}
			
			// Add Cooldown if sender is commenting on it's own ticket
			if(ownTicket) {
				CooldownManager.addCooldown(sender.getName(), CooldownType.COMMENT);
			}
			
			return;
		}
		
		// Failure
		String msg = LangMacro.replaceId(Lang.COMMENT_COMMAND_FAILURE, id);
		Lang.sendErrorMessage(sender, msg);
	}
	
}
