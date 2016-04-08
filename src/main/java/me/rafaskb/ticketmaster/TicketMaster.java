package me.rafaskb.ticketmaster;

import me.rafaskb.ticketmaster.utils.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.rafaskb.ticketmaster.listeners.PlayerJoinListener;
import me.rafaskb.ticketmaster.sql.DatabaseManager;
import me.rafaskb.ticketmaster.tasks.CooldownCleanup;
import me.rafaskb.ticketmaster.tasks.PendingTicketNotifier;
import me.rafaskb.ticketmaster.utils.LangConfig;

public class TicketMaster extends JavaPlugin {
	private static TicketMaster instance;
	
	@Override
	public void onEnable() {
		instance = this;

		// Load lang.yml
		LangConfig.reloadConfig();
        ConfigLoader.reloadConfig();

		// Open database
		DatabaseManager.open();
		DatabaseManager.createTablesIfNotExists();
		DatabaseManager.updateDatabase();
		
		// Commands
		CommandHandler cmdHandler = new CommandHandler();
		getCommand("ticket").setExecutor(cmdHandler);
		getCommand("tickets").setExecutor(cmdHandler);
		
		// Listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerJoinListener(), this);
		
		// Tasks
		new PendingTicketNotifier().register(this);
		new CooldownCleanup().register(this);
	}
	
	@Override
	public void onDisable() {
        ConfigLoader.saveConfig();
		DatabaseManager.close();
		Bukkit.getScheduler().cancelTasks(this);
		instance = null;
	}
	
	public static TicketMaster getInstance() {
		return instance;
	}
}
