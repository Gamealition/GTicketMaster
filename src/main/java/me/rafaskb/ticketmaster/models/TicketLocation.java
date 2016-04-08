package me.rafaskb.ticketmaster.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class TicketLocation {
	private final String worldName;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	
	public TicketLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public TicketLocation(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	public Location getLocation() {
		return new Location(getWorld(), x, y, z, yaw, pitch);
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	
}
