package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.utils.Lang;

public class CommandUsageInfo extends Command {
	
	public CommandUsageInfo() {
		super("");
	}
	
	@Override
	protected void run(CommandSender sender, String[] args) {
		Lang.sendMessage(sender, Lang.USAGE_INFORMATION);
	}
	
}
