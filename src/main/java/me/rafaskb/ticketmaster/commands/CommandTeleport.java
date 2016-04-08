package me.rafaskb.ticketmaster.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.rafaskb.ticketmaster.models.TicketLocation;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;

public class CommandTeleport extends Command {
	private static final int INDEX_ID = 1;
	
	public CommandTeleport() {
		super(Perm.HELPER_TELEPORT);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// Not a player
		if(!(sender instanceof Player)) {
			Lang.sendErrorMessage(sender, Lang.CANNOT_RUN_FROM_CONSOLE);
			return;
		}
		Player player = ((Player) sender);
		
		// If not enough arguments
		if(args.length != 2) {
			Lang.sendErrorMessage(sender, Lang.TELEPORT_COMMAND_USAGE);
			return;
		}
		
		// If can't parse ID argument as integer
		int id = 0;
		try {
			id = Integer.parseInt(args[INDEX_ID]);
		} catch (NumberFormatException e) {
			Lang.sendErrorMessage(sender, Lang.TELEPORT_COMMAND_USAGE);
			return;
		}
		
		// If ID not found, show TicketNotFound error
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
		
		// Get location
		TicketLocation ticketLocation = Controller.getTicketLocation(id);
		
		// Success
		if(ticketLocation != null) {
			Location loc = ticketLocation.getLocation();
			if(loc != null) {
				player.leaveVehicle();
				player.teleport(loc, TeleportCause.COMMAND);
				String msg = LangMacro.replaceId(Lang.TELEPORT_COMMAND_SUCCESS, id);
				Lang.sendMessage(sender, msg);
				return;
			}
		}
		
		// Failure
		String msg = LangMacro.replaceId(Lang.TELEPORT_COMMAND_FAILURE, id);
		Lang.sendErrorMessage(sender, msg);
	}
	
}
