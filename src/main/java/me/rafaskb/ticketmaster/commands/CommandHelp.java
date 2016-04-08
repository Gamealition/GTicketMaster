package me.rafaskb.ticketmaster.commands;

import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.CommandHandler;
import me.rafaskb.ticketmaster.utils.Lang;

public class CommandHelp extends Command {
	private CommandHandler handler;
	
	public CommandHelp(CommandHandler handler) {
		super("");
		this.handler = handler;
	}
	
	// @formatter:off
	@Override
	protected void run(CommandSender sender, String[] args) {
		Lang.sendMessage(sender, Lang.HELP_COMMAND_HEADER);
		
		if(handler.cmdNew.canRun(sender)) 		Lang.sendMessage(sender, Lang.NEW_COMMAND_DESCRIPTION, false);
		if(handler.cmdList.canRun(sender)) 		Lang.sendMessage(sender, Lang.LIST_COMMAND_DESCRIPTION, false);
		if(handler.cmdCheck.canRun(sender)) 	Lang.sendMessage(sender, Lang.CHECK_COMMAND_DESCRIPTION, false);
		
		if(handler.cmdClose.canRun(sender)) 	Lang.sendMessage(sender, Lang.CLOSE_COMMAND_DESCRIPTION, false);
		if(handler.cmdReopen.canRun(sender)) 	Lang.sendMessage(sender, Lang.REOPEN_COMMAND_DESCRIPTION, false);
		if(handler.cmdDelete.canRun(sender)) 	Lang.sendMessage(sender, Lang.DELETE_COMMAND_DESCRIPTION, false);
		if(handler.cmdClaim.canRun(sender)) 	Lang.sendMessage(sender, Lang.CLAIM_COMMAND_DESCRIPTION, false);
		
		if(handler.cmdComment.canRun(sender)) 	Lang.sendMessage(sender, Lang.COMMENT_COMMAND_DESCRIPTION, false);
		if(handler.cmdTeleport.canRun(sender)) 	Lang.sendMessage(sender, Lang.TELEPORT_COMMAND_DESCRIPTION, false);
		if(handler.cmdStatus.canRun(sender)) 	Lang.sendMessage(sender, Lang.STATUS_COMMAND_DESCRIPTION, false);
		if(handler.cmdPriority.canRun(sender)) 	Lang.sendMessage(sender, Lang.PRIORITY_COMMAND_DESCRIPTION, false);
		if(handler.cmdElevate.canRun(sender)) 	Lang.sendMessage(sender, Lang.ELEVATE_COMMAND_DESCRIPTION, false);
		if(handler.cmdLower.canRun(sender)) 	Lang.sendMessage(sender, Lang.LOWER_COMMAND_DESCRIPTION, false);
		
		if(handler.cmdHelp.canRun(sender)) 		Lang.sendMessage(sender, Lang.HELP_COMMAND_DESCRIPTION, false);
		if(handler.cmdReload.canRun(sender)) 	Lang.sendMessage(sender, Lang.RELOAD_COMMAND_DESCRIPTION, false);
	}
	// @formatter:on
	
}
