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

public class CommandClaim extends Command {
	private static final int INDEX_ID = 1;
	
	public CommandClaim() {
		super(Perm.PRIORITY_LOW);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length != 2) {
			Lang.sendErrorMessage(sender, Lang.CLAIM_COMMAND_USAGE);
			return;
		}
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.CLAIM_COMMAND_USAGE);
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
		
		// If ticket is closed
		TicketStatus status = Controller.getTicketStatus(id);
		if(status == TicketStatus.CLOSED) {
			Lang.sendErrorMessage(sender, Lang.CLAIM_COMMAND_CLOSED);
			return;
		}
		
		// Change status
		boolean success = Controller.setTicketStatus(id, TicketStatus.CLAIMED)
			& Controller.setTicketAssignee(id, sender.getName());
		
		// Success
		if(success) {
			// Add log to ticket as a comment
			TicketComment log = new TicketComment(id, sender.getName(), TicketLogBuilder.claimed());
			Controller.pushTicketComment(log);

			// Message command sender
			String msg = LangMacro.replaceId(Lang.CLAIM_COMMAND_SUCCESS, id);
			Lang.sendMessage(sender, msg);
			
			// Message ticket submitter
			String actionMessage = TicketActionBuilder.claimed(sender.getName(), id);
			String ticketSubmitter = Controller.getTicketSubmitter(id);
			if(!Utils.sendActionMessage(ticketSubmitter, actionMessage))
			     Controller.insertPendingMessage(ticketSubmitter, actionMessage);

			return;
		}
		
		// Failure
		else {
			String msg = LangMacro.replaceId(Lang.CLAIM_COMMAND_FAILURE, id);
			Lang.sendErrorMessage(sender, msg);
			return;
		}
	}
	
}
