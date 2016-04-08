package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.TicketComment;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;
import me.rafaskb.ticketmaster.utils.TicketActionBuilder;
import me.rafaskb.ticketmaster.utils.TicketLogBuilder;
import me.rafaskb.ticketmaster.utils.Utils;

public class CommandClose extends Command {
	private static final int INDEX_ID = 1;
	private static final int INDEX_REASON_START = 2;
	
	public CommandClose() {
		super(new String[] {Perm.PRIORITY_LOW, Perm.USER_MANAGE});
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length < 2) {
			Lang.sendErrorMessage(sender, Lang.CLOSE_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.CLOSE_COMMAND_USAGE);
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
		String senderName = sender.getName();
		String submitterName = Controller.getTicketSubmitter(id);
		boolean ownTicket = false;
		checkSubmitter:
		{
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
		
		// If ticket is already closed
		TicketStatus status = Controller.getTicketStatus(id);
		if(status == TicketStatus.CLOSED) {
			Lang.sendErrorMessage(sender, Lang.CLOSE_COMMAND_CLOSED);
			return;
		}
		
		// Extract reason, if any
		String reason = null;
		if(args.length > INDEX_REASON_START)
			reason = Utils.convertArgumentsToString(args, INDEX_REASON_START);
		
		// Change status
		boolean success = Controller.setTicketStatus(id, TicketStatus.CLOSED) & Controller.setTicketAssignee(id, "");
		
		// Success
		if(success) {
			// Add log to ticket as a comment, including reason
			TicketComment log = new TicketComment(id, sender.getName(), TicketLogBuilder.closed(reason));
			Controller.pushTicketComment(log);
			
			// Message command sender
			String msg = LangMacro.replaceId(Lang.CLOSE_COMMAND_SUCCESS, id);
			Lang.sendMessage(sender, msg);
			
			// Message ticket submitter
			if(!ownTicket) {
				String actionMessage = TicketActionBuilder.closed(sender.getName(), id, reason);
				if(!Utils.sendActionMessage(submitterName, actionMessage))
					Controller.insertPendingMessage(submitterName, actionMessage);
			}
			
			return;
		}
		
		// Failure
		else {
			String msg = LangMacro.replaceId(Lang.CLOSE_COMMAND_FAILURE, id);
			Lang.sendErrorMessage(sender, msg);
			return;
		}
		
	}
}
