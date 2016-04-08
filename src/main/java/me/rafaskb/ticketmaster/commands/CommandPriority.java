package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.TicketComment;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;
import me.rafaskb.ticketmaster.utils.TicketActionBuilder;
import me.rafaskb.ticketmaster.utils.TicketLogBuilder;
import me.rafaskb.ticketmaster.utils.Utils;

public class CommandPriority extends Command {
	private static final int INDEX_ID = 1;
	private static final int INDEX_PRIORITY = 2;
	
	public CommandPriority() {
		super(Perm.PRIORITY_LOW);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length != 3) {
			Lang.sendErrorMessage(sender, Lang.PRIORITY_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.PRIORITY_COMMAND_USAGE);
			return;
		}
		
		// If specified priority is invalid
		TicketPriority newPriority = null;
		try {
			newPriority = TicketPriority.valueOf(args[INDEX_PRIORITY].toUpperCase());
		} catch (Exception e) {
			Lang.sendErrorMessage(sender, Lang.PRIORITY_COMMAND_USAGE);
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
		
		// Call Final Run
		finalRun(sender, id, newPriority);
	}
	
	protected static void finalRun(CommandSender sender, int id, TicketPriority newPriority) {
		// Change priority
		boolean success = Controller.setTicketPriority(id, newPriority);
		
		// Success
		if(success) {
			// Add log to ticket as a comment
			TicketComment log = new TicketComment(id, sender.getName(), TicketLogBuilder.priorityChanged(newPriority));
			Controller.pushTicketComment(log);
			
			// Message command sender
			String msg = LangMacro.replacePriority(Lang.PRIORITY_COMMAND_SUCCESS, newPriority);
			Lang.sendMessage(sender, msg);
			
			// Message ticket submitter
			String actionMessage = TicketActionBuilder.priorityChanged(sender.getName(), id, newPriority);
			String ticketSubmitter = Controller.getTicketSubmitter(id);
			if(!Utils.sendActionMessage(ticketSubmitter, actionMessage))
			     Controller.insertPendingMessage(ticketSubmitter, actionMessage);

			return;
		}
		
		// Failure
		else {
			Lang.sendErrorMessage(sender, Lang.PRIORITY_COMMAND_FAILURE);
			return;
		}
	}
	
}
