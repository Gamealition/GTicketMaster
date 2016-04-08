package me.rafaskb.ticketmaster.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.rafaskb.ticketmaster.TicketMaster;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.Lang;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLater(TicketMaster.getInstance(), new PendingMessageDelivery(player), 2000 / 20);
	}
	
	class PendingMessageDelivery implements Runnable {
		private Player player;
		private String name;
		
		public PendingMessageDelivery(Player player) {
			this.player = player;
			this.name = player.getName();
		}
		
		@Override
		public void run() {
			if(!player.isOnline())
				return;
			
			if(Controller.countPendingMessages(name) < 1)
				return;
			
			List<String> pendingMessages = Controller.getPendingMessages(name);
			
			for(String pendingMessage : pendingMessages)
				Lang.sendMessage(player, pendingMessage);
			
			Controller.deletePendingMessages(name);
		}
	}
}
