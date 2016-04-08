package me.rafaskb.ticketmaster.models;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.rafaskb.ticketmaster.utils.Utils;

public class SimpleTicket {
	private int id;
	private String submitter;
	private long date;
	private String message;
	private TicketStatus status;
	private TicketPriority priority;
	
	public SimpleTicket(Player player, String message) {
		this.setSubmitter(player.getName());
		this.setDate(System.currentTimeMillis());
		this.setMessage(message);
		this.setId(0);
		this.setStatus(TicketStatus.PENDING);
		this.setPriority(TicketPriority.NORMAL);
	}
	
	private SimpleTicket() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSubmitter() {
		return submitter;
	}
	
	public void setSubmitter(String submitter) {
		if(submitter == null) submitter = "";
		this.submitter = submitter;
	}
	
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		if(message == null) message = "";
		this.message = message;
	}
	
	public TicketStatus getStatus() {
		return status;
	}
	
	public void setStatus(TicketStatus status) {
		if(status == null) status = TicketStatus.PENDING;
		this.status = status;
	}
	
	public TicketPriority getPriority() {
		return priority;
	}
	
	public void setPriority(TicketPriority priority) {
		if(priority == null) priority = TicketPriority.NORMAL;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// Status Color + ID
		sb.append(getStatus().getColor()).append('#').append(getId());
		
		// Priority Tag
		sb.append(' ').append(getPriority().getColoredTag());
		
		// Separator
		sb.append(ChatColor.DARK_GRAY).append(" | ");
		
		// Elapsed Time
		sb.append(ChatColor.GOLD).append(ChatColor.ITALIC).append(Utils.getFriendlyElapsedTime(getDate()));
		
		// In-context separator
		sb.append(ChatColor.DARK_GRAY).append(ChatColor.ITALIC).append(" by ");
		
		// Submitter
		sb.append(ChatColor.DARK_GREEN).append(ChatColor.ITALIC).append(getSubmitter());
		
		// Separator
		sb.append(ChatColor.DARK_GRAY).append(" | ");
		
		// Abbreviated message
		sb.append(ChatColor.WHITE).append(StringUtils.abbreviate(getMessage(), 25));
		
		sb.append(ChatColor.RESET);
		return sb.toString();
	}
	
	public static SimpleTicket empty() {
		return new SimpleTicket();
	}
	
}
