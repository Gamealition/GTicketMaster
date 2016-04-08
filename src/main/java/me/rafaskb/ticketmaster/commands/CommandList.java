package me.rafaskb.ticketmaster.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.models.SimpleTicket;
import me.rafaskb.ticketmaster.models.SimpleTicketComparator;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.Perm;

public class CommandList extends Command {
	private static final int INDEX_STATUS = 1;
	
	public CommandList() {
		super(new String[] {Perm.USER_MANAGE, Perm.PRIORITY_LOW});
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		// If sender has no permission to manage a ticket, that means it's trying to check their own tickets
		if(!Perm.check(sender, Perm.PRIORITY_LOW))
			runUser(sender);
		
		// Otherwise run staff routines
		else
			runStaff(sender, args);
	}
	
	private void runUser(CommandSender sender) {
		// Get all tickets IDs submitted by this sender, except closed ones.
		List<Integer> ids = Controller.getTicketIDsFromSender(sender.getName());
		
		// If the list is empty, send error message
		if(ids.isEmpty()) {
			Lang.sendErrorMessage(sender, Lang.LIST_COMMAND_NO_TICKETS_USER);
			return;
		}
		
		// Get tickets
		List<SimpleTicket> tickets = new ArrayList<>();
		for(int id : ids) {
			SimpleTicket ticket = Controller.getSimpleTicket(id);
			if(ticket != null)
				tickets.add(ticket);
		}
		
		// Run final routines
		runFinal(sender, tickets);
	}
	
	private void runStaff(CommandSender sender, String[] args) {
		// If there is an argument, we try to parse it
		TicketStatus status = null;
		if(args.length > INDEX_STATUS) {
			
			// We have an argument, try to parse it as a valid status
			try {
				status = TicketStatus.valueOf(args[INDEX_STATUS].toUpperCase());
			} catch (Exception e) {
				// Couldn't parse value, so we show the Usage error message and return.
				Lang.sendErrorMessage(sender, Lang.LIST_COMMAND_USAGE);
				return;
			}
		}
		
		// Get priorities sender has permission to manage
		List<TicketPriority> priorityList = new ArrayList<TicketPriority>();
		for(TicketPriority priority : TicketPriority.values()) {
			if(sender.hasPermission(priority.getRequiredPermission()))
				priorityList.add(priority);
		}
		TicketPriority[] priorities = new TicketPriority[priorityList.size()];
		priorities = priorityList.toArray(priorities);
		
		// Get statuses sender wants to see
		TicketStatus[] statuses = null;
		if(status == null)
			statuses = new TicketStatus[] {TicketStatus.PENDING, TicketStatus.ONHOLD, TicketStatus.CLAIMED};
		else
			statuses = new TicketStatus[] {status};
		
		// Get all ticket IDs with the specified status, and that this sender has permission to manage
		List<Integer> ids = Controller.getTicketIDsUnderSpecials(statuses, priorities);
		
		// If the list is empty, send error message
		if(ids.isEmpty()) {
			Lang.sendErrorMessage(sender, Lang.LIST_COMMAND_NO_TICKETS);
			return;
		}
		
		// Get tickets
		List<SimpleTicket> tickets = new ArrayList<>();
		for(int id : ids) {
			SimpleTicket ticket = Controller.getSimpleTicket(id);
			if(ticket != null)
				tickets.add(ticket);
		}
		
		// Run final routines
		runFinal(sender, tickets);
	}
	
	private void runFinal(CommandSender sender, List<SimpleTicket> tickets) {
		// If no tickets returned, send failure message (should never happen)
		if(tickets.isEmpty()) {
			Lang.sendErrorMessage(sender, Lang.LIST_COMMAND_NO_TICKETS);
			return;
		}
		
		// Sort tickets
		Collections.sort(tickets, new SimpleTicketComparator());
		Collections.reverse(tickets);
		
		// List tickets (Header + ticket simple rows)
		Lang.sendMessage(sender, Lang.LIST_COMMAND_HEADER, false);
		for(SimpleTicket ticket : tickets)
			sender.sendMessage(ticket.toString());
	}
}
