package me.rafaskb.ticketmaster.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangMacro;
import me.rafaskb.ticketmaster.utils.Perm;
import me.rafaskb.ticketmaster.utils.Utils;

import org.bukkit.entity.Player;

public class PendingTicketNotifier extends Task {
	private Map<TicketPriority, List<Player>> playerMap;
	
	public PendingTicketNotifier() {
		super(1 * 60, 4 * 60);
		
		playerMap = new HashMap<>();
		for(TicketPriority priority : TicketPriority.values())
			playerMap.put(priority, new ArrayList<Player>());
	}
	
	@Override
	public void run() {
		
		try {
			// Populate map
			populateMap();
			
			// If no players to be notified, stop process
			if(getMapSize() == 0)
				return;
			
			// Get 'pending' tickets count by priority
			Map<TicketPriority, Integer> count = countPendingTickets();
			
			// Check amount of tickets returned, if none, stop process
			int totalTickets = 0;
			for(int c : count.values())
				totalTickets += c;
			if(totalTickets == 0)
				return;
			
			// Notify players
			notifyPlayers(count);
		} finally {
			
			// Cleanup player map
			cleanup();
		}
	}
	
	private void populateMap() {
		for(Player player : Utils.getOnlinePlayers()) {
			if(player.hasPermission(Perm.PRIORITY_CRITICAL))
				playerMap.get(TicketPriority.CRITICAL).add(player);
			else if(player.hasPermission(Perm.PRIORITY_HIGH))
				playerMap.get(TicketPriority.HIGH).add(player);
			else if(player.hasPermission(Perm.PRIORITY_NORMAL))
				playerMap.get(TicketPriority.NORMAL).add(player);
			else if(player.hasPermission(Perm.PRIORITY_LOW))
				playerMap.get(TicketPriority.LOW).add(player);
		}
	}
	
	private int getMapSize() {
		int result = 0;
		for(TicketPriority priority : TicketPriority.values())
			result += playerMap.get(priority).size();
		return result;
	}
	
	private Map<TicketPriority, Integer> countPendingTickets() {
		Map<TicketPriority, Integer> map = new HashMap<>();
		for(TicketPriority priority : TicketPriority.values())
			map.put(priority, Controller.countPendingTicketsWithPriority(priority));
		
		return map;
	}
	
	private void notifyPlayers(Map<TicketPriority, Integer> count) {
		for(TicketPriority playerPriority : playerMap.keySet()) {
			List<Player> playerList = playerMap.get(playerPriority);
			if(playerList == null || playerList.size() == 0)
				continue;
			
			int amount = 0;
			switch(playerPriority) {
				case CRITICAL:
					amount += count.get(TicketPriority.CRITICAL);
				case HIGH:
					amount += count.get(TicketPriority.HIGH);
				case NORMAL:
					amount += count.get(TicketPriority.NORMAL);
				case LOW:
					amount += count.get(TicketPriority.LOW);
			}
			
			for(Player player : playerList) {
				String msg = LangMacro.replaceAmount(Lang.BROADCAST_PENDING_TICKETS, amount);
				Lang.sendMessage(player, msg);
			}
		}
	}
	
	private void cleanup() {
		for(TicketPriority priority : TicketPriority.values())
			playerMap.get(priority).clear();
	}
	
}
