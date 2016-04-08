package me.rafaskb.ticketmaster.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.TicketMaster;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.Perm;

abstract class Command {
	private List<String> requiredPerms;
	
	public Command(String requiredPerms) {
		this(new String[] {requiredPerms});
	}
	
	public Command(String[] requiredPerms) {
		this.requiredPerms = new ArrayList<>(4);
		
		if(requiredPerms != null)
			for(String requiredPerm : requiredPerms)
				if(requiredPerm != null && !requiredPerm.equals(""))
					this.requiredPerms.add(requiredPerm);
	}
	
	public void execute(CommandSender sender, String[] args) {
		if(canRun(sender)) {
			run(sender, args);
		} else {
			Lang.sendErrorMessage(sender);
		}
	}
	
	protected abstract void run(CommandSender sender, String[] args);
	
	public boolean canRun(CommandSender sender) {
		if(requiresPermission()) {
			for(String perm : requiredPerms)
				if(Perm.check(sender, perm))
					return true;
			return false;
		}
		return true;
	}
	
	private boolean requiresPermission() {
		return requiredPerms.size() > 0;
	}
	
	protected TicketMaster getPlugin() {
		return TicketMaster.getInstance();
	}
}
