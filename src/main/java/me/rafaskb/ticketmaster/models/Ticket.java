package me.rafaskb.ticketmaster.models;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.rafaskb.ticketmaster.models.TicketComment.TicketCommentDateComparator;
import me.rafaskb.ticketmaster.utils.Utils;

public class Ticket {
	private int id;
	private String submitter;
	private long date;
	private String message;
	private String assignee;
	private TicketStatus status;
	private TicketPriority priority;
	private TicketLocation ticketLocation;
	private Set<TicketComment> comments;
	
	public Ticket(Player player, String message) {
		this.setSubmitter(player.getName());
		this.setDate(System.currentTimeMillis());
		this.setLocation(player.getLocation());
		this.setMessage(message);
		this.setId(0);
		this.setStatus(TicketStatus.PENDING);
		this.setPriority(TicketPriority.NORMAL);
		this.comments = new TreeSet<TicketComment>(new TicketCommentDateComparator());
	}
	
	private Ticket() {
		this.comments = new TreeSet<TicketComment>(new TicketCommentDateComparator());
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
	
	public String getAssignee() {
		return assignee;
	}
	
	public void setAssignee(String assignee) {
		if(assignee == null) assignee = "";
		this.assignee = assignee;
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
	
	public Location getLocation() {
		return ticketLocation.getLocation();
	}
	
	public TicketLocation getTicketLocation() {
		return ticketLocation;
	}
	
	public void setLocation(Location location) {
		this.ticketLocation = new TicketLocation(location);
	}
	
	public void setLocation(TicketLocation location) {
		this.ticketLocation = location;
	}
	
	public Set<TicketComment> getComments() {
		return comments;
	}
	
	public void setComments(Collection<TicketComment> comments) {
		this.comments.addAll(comments);
	}
	
	public void addComment(TicketComment comment) {
		this.comments.add(comment);
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		
		// Header
		// --- [Ticket #182, created 5 days ago by SomeoneCool] ---
		sb.append(ChatColor.GRAY).append("--- [")
			.append(ChatColor.GOLD).append("Ticket #").append(getId())
			.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(", created ")
			.append(ChatColor.GOLD).append(ChatColor.ITALIC).append(Utils.getFriendlyElapsedTime(getDate()))
			.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(" by ")
			.append(ChatColor.YELLOW).append(getSubmitter())
			.append(ChatColor.GRAY).append("] ---");
		
		// Specials
		// Priority: High                    Status: Claimed by MisterRaptor
		sb.append(ChatColor.RESET).append("\n")
			.append(ChatColor.WHITE).append("Priority: ")
			.append(getPriority().getColor()).append(getPriority().getScreenName())
			.append(ChatColor.WHITE).append("          ")
			.append("Status: ").append(getStatus().getColor()).append(getStatus().getScreenName());
		
		if(getStatus().equals(TicketStatus.CLAIMED))
			sb.append(ChatColor.GRAY).append(" by ").append(getAssignee());
		
		// Location
		// Location: x, y, z @ world
		sb.append(ChatColor.RESET).append("\n")
			.append(ChatColor.WHITE).append("Location: ")
			.append(ChatColor.GRAY).append((int) ticketLocation.getX())
			.append(ChatColor.DARK_GRAY).append(", ")
			.append(ChatColor.GRAY).append((int) ticketLocation.getY())
			.append(ChatColor.DARK_GRAY).append(", ")
			.append(ChatColor.GRAY).append((int) ticketLocation.getZ())
			.append(ChatColor.DARK_GRAY).append(" @ ")
			.append(ChatColor.GRAY).append(ticketLocation.getWorldName());
		
		// Request message
		// Request: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
		sb.append(ChatColor.RESET).append("\n")
			.append(ChatColor.WHITE).append("Request: ")
			.append(ChatColor.GREEN).append(getMessage());
		
		// Comments
		// Comments:
		//  7 secs ago by SomeoneCool: Still not fixed, fix it!
		//  10 mins ago by MisterRaptor: [Ticket claimed]
		sb.append(ChatColor.RESET).append("\n")
			.append(ChatColor.WHITE).append("Comments:");
		for(TicketComment comment : getComments())
			sb.append("\n").append(comment.toString());
		
		sb.append(ChatColor.RESET);
		return sb.toString();
	}
	
	public static Ticket empty() {
		return new Ticket();
	}
}
