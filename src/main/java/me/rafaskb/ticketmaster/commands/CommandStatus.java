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

public class CommandStatus extends Command {
	private static final int INDEX_ID = 1;
	private static final int INDEX_STATUS = 2;
	
	public CommandStatus() {
		super(Perm.HELPER_STATUS);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length != 3) {
			Lang.sendErrorMessage(sender, Lang.STATUS_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.STATUS_COMMAND_USAGE);
			return;
		}
		
		// If specified status is invalid
		TicketStatus newStatus = null;
		try {
			newStatus = TicketStatus.valueOf(args[INDEX_STATUS].toUpperCase());
		} catch (Exception e) {
			Lang.sendErrorMessage(sender, Lang.STATUS_COMMAND_USAGE);
			return;
		}
		
		// If ID not found, show 'ticket_not_found' error (replace <id>)
		if(!Controller.ticketExists(id)) {
			Lang.sendTicketNotFoundMessage(sender, id);
			return;
		}
		
		// Get ticket priority and check if sender can manage it
		TicketPriority priority = Controller.getTicketPriority(id);
		if(!Perm.check(sender, priority.getRequiredPermission())) {
			Lang.sendErrorMessage(sender, Lang.TICKET_MANAGEMENT_NO_PERMS);
			return;
		}
		
		// Change status
		boolean success = Controller.setTicketStatus(id, newStatus);
		
		// Success
		if(success) {
			
			// If new status is Claimed, set Assignee as well
			if(newStatus == TicketStatus.CLAIMED)
				Controller.setTicketAssignee(id, sender.getName());
			// Otherwise set it to null
			else
				Controller.setTicketAssignee(id, "");
			
			// Add log to ticket as a comment
			TicketComment log = new TicketComment(id, sender.getName(), TicketLogBuilder.statusChanged(newStatus));
			Controller.pushTicketComment(log);
			
			// Message command sender
			String msg = LangMacro.replaceStatus(Lang.STATUS_COMMAND_SUCCESS, newStatus);
			Lang.sendMessage(sender, msg);
			
			// Message ticket submitter
			String actionMessage = TicketActionBuilder.statusChanged(sender.getName(), id, newStatus);
			String ticketSubmitter = Controller.getTicketSubmitter(id);
			if(!Utils.sendActionMessage(ticketSubmitter, actionMessage))
			     Controller.insertPendingMessage(ticketSubmitter, actionMessage);

			return;
		}
		
		// Failure
		else {
			Lang.sendErrorMessage(sender, Lang.STATUS_COMMAND_FAILURE);
			return;
		}
	}
}
