package me.rafaskb.ticketmaster.models;

import java.util.Comparator;

public class SimpleTicketComparator implements Comparator<SimpleTicket> {
	
	@Override
	public int compare(SimpleTicket t1, SimpleTicket t2) {
		int compareNull = compareNull(t1, t2);
		if(compareNull != 0)
			return compareNull;
		
		int comparePriority = comparePriority(t1.getPriority(), t2.getPriority());
		if(comparePriority != 0)
			return comparePriority;
		
		int compareStatus = compareStatus(t1.getStatus(), t2.getStatus());
		if(compareStatus != 0)
			return compareStatus;
		
		int compareDate = compareDate(t1.getDate(), t2.getDate());
		return compareDate;
	}
	
	private int compareNull(SimpleTicket t1, SimpleTicket t2) {
		if(t1 != null && t2 == null) return -1;
		if(t1 == null && t2 != null) return 1;
		return 0;
	}
	
	private int comparePriority(TicketPriority p1, TicketPriority p2) {
		return -p1.compareTo(p2);
	}
	
	private int compareStatus(TicketStatus s1, TicketStatus s2) {
		return s1.compareTo(s2);
	}
	
	private int compareDate(long d1, long d2) {
		if(d1 > d2) return -1;
		if(d1 < d2) return 1;
		return 0;
	}
	
}
