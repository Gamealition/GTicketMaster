package me.rafaskb.ticketmaster.commands;

import me.rafaskb.ticketmaster.utils.ConfigLoader;
import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.LangConfig;
import me.rafaskb.ticketmaster.utils.Perm;

public class CommandReload extends Command {
	
	public CommandReload() {
		super(Perm.RELOAD);
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		LangConfig.reloadConfig();
		ConfigLoader.reloadConfig();
		Lang.sendMessage(sender, Lang.RELOAD_MESSAGE);
	}
	
}
