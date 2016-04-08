package me.rafaskb.ticketmaster.tasks;

import me.rafaskb.ticketmaster.utils.CooldownManager;

public class CooldownCleanup extends Task {
	
	public CooldownCleanup() {
		super(8 * 60, 5 * 60);
	}
	
	@Override
	public void run() {
		CooldownManager.cleanup();
	}
	
}
