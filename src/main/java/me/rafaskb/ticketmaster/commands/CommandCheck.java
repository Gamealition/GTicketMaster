package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.Ticket;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;

public class CommandCheck extends Command {
	private static final int INDEX_ID = 1;
	
	public CommandCheck() {
		super(new String[] {Perm.PRIORITY_LOW, Perm.USER_MANAGE});
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length < 2) {
			Lang.sendErrorMessage(sender, Lang.CHECK_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.CHECK_COMMAND_USAGE);
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
		checkSubmitter:
		{
			String senderName = sender.getName();
			String submitterName = Controller.getTicketSubmitter(id);
			if(senderName.equalsIgnoreCase(submitterName)) {
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
		
		// Extract ticket
		Ticket ticket = Controller.getTicket(id);
		
		if(ticket == null) {
			// Failure message
			String msg = LangMacro.replaceId(Lang.CHECK_COMMAND_FAILURE, id);
			Lang.sendErrorMessage(sender, msg);
		} else {
			// Success, show ticket full info
			sender.sendMessage(ticket.toString());
		}
	}
	
}
