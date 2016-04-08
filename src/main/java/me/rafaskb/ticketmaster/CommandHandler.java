package me.rafaskb.ticketmaster;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.rafaskb.ticketmaster.commands.CommandCheck;
import me.rafaskb.ticketmaster.commands.CommandClaim;
import me.rafaskb.ticketmaster.commands.CommandClose;
import me.rafaskb.ticketmaster.commands.CommandComment;
import me.rafaskb.ticketmaster.commands.CommandDelete;
import me.rafaskb.ticketmaster.commands.CommandElevate;
import me.rafaskb.ticketmaster.commands.CommandHelp;
import me.rafaskb.ticketmaster.commands.CommandList;
import me.rafaskb.ticketmaster.commands.CommandLower;
import me.rafaskb.ticketmaster.commands.CommandNew;
import me.rafaskb.ticketmaster.commands.CommandPriority;
import me.rafaskb.ticketmaster.commands.CommandReload;
import me.rafaskb.ticketmaster.commands.CommandReopen;
import me.rafaskb.ticketmaster.commands.CommandStatus;
import me.rafaskb.ticketmaster.commands.CommandTeleport;
import me.rafaskb.ticketmaster.commands.CommandUsageInfo;
import me.rafaskb.ticketmaster.utils.Lang;
import me.rafaskb.ticketmaster.utils.Utils;

public class CommandHandler implements CommandExecutor {
	private final CommandUsageInfo cmdUsageInfo;
	public final CommandCheck cmdCheck;
	public final CommandClose cmdClose;
	public final CommandComment cmdComment;
	public final CommandDelete cmdDelete;
	public final CommandElevate cmdElevate;
	public final CommandHelp cmdHelp;
	public final CommandList cmdList;
	public final CommandLower cmdLower;
	public final CommandNew cmdNew;
	public final CommandPriority cmdPriority;
	public final CommandReload cmdReload;
	public final CommandReopen cmdReopen;
	public final CommandClaim cmdClaim;
	public final CommandStatus cmdStatus;
	public final CommandTeleport cmdTeleport;
	
	public CommandHandler() {
		cmdUsageInfo = new CommandUsageInfo();
		cmdCheck = new CommandCheck();
		cmdClose = new CommandClose();
		cmdComment = new CommandComment();
		cmdDelete = new CommandDelete();
		cmdElevate = new CommandElevate();
		cmdHelp = new CommandHelp(this);
		cmdList = new CommandList();
		cmdLower = new CommandLower();
		cmdNew = new CommandNew();
		cmdPriority = new CommandPriority();
		cmdReload = new CommandReload();
		cmdReopen = new CommandReopen();
		cmdClaim = new CommandClaim();
		cmdStatus = new CommandStatus();
		cmdTeleport = new CommandTeleport();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Command: "/tickets [?...]" | Works as "/ticket list [???]"
		if(cmd.getName().equalsIgnoreCase("tickets")) {
			cmdList.execute(sender, Utils.increaseArgs(args));
			return true;
		}
		
		// Command: "/ticket [?...]" | Check arguments
		if(cmd.getName().equalsIgnoreCase("ticket")) {
			
			// No arguments, show usage information
			if(args.length == 0) {
				cmdUsageInfo.execute(sender, args);
				return true;
			}
			
			// Iterate through valid arguments
			String arg1 = args[0].toLowerCase();
			switch(arg1) {
				case "help": {
					cmdHelp.execute(sender, args);
					break;
				}
				case "check": {
					cmdCheck.execute(sender, args);
					break;
				}
				case "list": {
					cmdList.execute(sender, args);
					break;
				}
				case "new": {
					cmdNew.execute(sender, args);
					break;
				}
				case "close": {
					cmdClose.execute(sender, args);
					break;
				}
				case "reopen": {
					cmdReopen.execute(sender, args);
					break;
				}
				case "claim": {
					cmdClaim.execute(sender, args);
					break;
				}
				case "delete": {
					cmdDelete.execute(sender, args);
					break;
				}
				case "comment": {
					cmdComment.execute(sender, args);
					break;
				}
				case "tp": {
					cmdTeleport.execute(sender, args);
					break;
				}
				case "status": {
					cmdStatus.execute(sender, args);
					break;
				}
				case "priority": {
					cmdPriority.execute(sender, args);
					break;
				}
				case "elevate": {
					cmdElevate.execute(sender, args);
					break;
				}
				case "lower": {
					cmdLower.execute(sender, args);
					break;
				}
				case "reload": {
					cmdReload.execute(sender, args);
					break;
				}
				default: {
					Lang.sendErrorMessage(sender, Lang.UNKNOWN_COMMAND);
					break;
				}
			}
		}
		return true;
	}
	
}
