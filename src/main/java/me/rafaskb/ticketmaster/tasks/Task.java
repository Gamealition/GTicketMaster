package me.rafaskb.ticketmaster.tasks;

import org.bukkit.plugin.Plugin;

public abstract class Task implements Runnable {
	private final long firstRunInterval;
	private final long loopInterval;
	
	public Task(long firstRunInterval, long loopInterval) {
		this.firstRunInterval = firstRunInterval;
		this.loopInterval = loopInterval;
	}
	
	public void register(Plugin plugin) {
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, firstRunInterval * 20, loopInterval * 20);
	}
}
