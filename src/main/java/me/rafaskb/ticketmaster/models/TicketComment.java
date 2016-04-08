package me.rafaskb.ticketmaster.models;

import java.util.Comparator;

import org.bukkit.ChatColor;

import me.rafaskb.ticketmaster.utils.Utils;

public class TicketComment {
	private int id;
	private String submitter;
	private long date;
	private String comment;
	
	public TicketComment(int id, String submitter, String message) {
		this(id, submitter, System.currentTimeMillis(), message);
	}
	
	public TicketComment(int id, String submitter, long date, String comment) {
		this.setId(id);
		this.setSubmitterName(submitter);
		this.setDateInMillis(date);
		this.setComment(comment);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSubmitterName() {
		return submitter;
	}
	
	public void setSubmitterName(String submitter) {
		this.submitter = submitter;
	}
	
	public long getDateInMillis() {
		return date;
	}
	
	public void setDateInMillis(long date) {
		this.date = date;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(ChatColor.GRAY).append("  ")
			.append(Utils.getFriendlyElapsedTime(getDateInMillis()))
			.append(ChatColor.DARK_GRAY).append(" by ")
			.append(ChatColor.GOLD).append(getSubmitterName())
			.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(": ")
			.append(getComment()).append(ChatColor.RESET);
		
		return sb.toString();
	}
	
	protected static class TicketCommentDateComparator implements Comparator<TicketComment> {
		@Override
		public int compare(TicketComment t1, TicketComment t2) {
			// Newer
			if(t1.getDateInMillis() < t2.getDateInMillis()) return -1;
			
			// Older
			if(t1.getDateInMillis() > t2.getDateInMillis()) return 1;
			
			// Equal
			return 0;
		}
	}
	
}
