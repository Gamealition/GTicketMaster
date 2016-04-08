package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;

public class CommandDelete extends Command {
	private static final int INDEX_ID = 1;
	
	public CommandDelete() {
		super(Perm.HELPER_DELETE);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If not enough arguments
		if(args.length != 2) {
			Lang.sendErrorMessage(sender, Lang.DELETE_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.DELETE_COMMAND_USAGE);
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
		
		// Delete ticket
		boolean success = Controller.deleteTicket(id);
		
		if(success) {
			String msg = LangMacro.replaceId(Lang.DELETE_COMMAND_SUCCESS, id);
			Lang.sendMessage(sender, msg);
		} else {
			String msg = LangMacro.replaceId(Lang.DELETE_COMMAND_FAILURE, id);
			Lang.sendErrorMessage(sender, msg);
		}
	}
	
}
