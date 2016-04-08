package me.rafaskb.ticketmaster.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.rafaskb.ticketmaster.models.SimpleTicket;
import me.rafaskb.ticketmaster.models.Ticket;
import me.rafaskb.ticketmaster.models.TicketComment;
import me.rafaskb.ticketmaster.models.TicketLocation;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.models.TicketStatus;
import me.rafaskb.ticketmaster.utils.Utils;

public class Controller {
	
	public static boolean ticketExists(int id) {
		if(id <= 0)
			return false;
		
		ResultSet rs = null;
		try {
			rs = DatabaseManager.executeSqlQuery("SELECT * FROM tickets WHERE id = " + id);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static TicketPriority getTicketPriority(int id) {
		ResultSet rs = null;
		try {
			String sql = "SELECT priority FROM tickets_special WHERE id = " + id + ";";
			rs = DatabaseManager.executeSqlQuery(sql);
			if(!rs.next())
				return null;
			String priorityString = rs.getString("priority");
			try {
				return TicketPriority.valueOf(priorityString.toUpperCase());
			} catch (IllegalStateException e) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static boolean setTicketPriority(int id, TicketPriority newPriority) {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OR IGNORE tickets_special SET priority = ");
		sb.append("'").append(newPriority.toString()).append("'");
		sb.append(" WHERE id = ");
		sb.append(id);
		sb.append(" ; ");
		
		return DatabaseManager.executeSqlUpdate(sb.toString());
	}
	
	public static TicketStatus getTicketStatus(int id) {
		ResultSet rs = null;
		try {
			String sql = "SELECT status FROM tickets_special WHERE id = " + id + ";";
			rs = DatabaseManager.executeSqlQuery(sql);
			if(!rs.next())
				return null;
			String statusString = rs.getString("status");
			try {
				return TicketStatus.valueOf(statusString.toUpperCase());
			} catch (IllegalStateException e) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static boolean setTicketStatus(int id, TicketStatus newStatus) {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OR IGNORE tickets_special SET status = ");
		sb.append("'").append(newStatus.toString()).append("'");
		sb.append(" WHERE id = ");
		sb.append(id);
		sb.append(" ; ");
		
		return DatabaseManager.executeSqlUpdate(sb.toString());
	}
	
	public static TicketLocation getTicketLocation(int id) {
		ResultSet rs = null;
		try {
			String sql = "SELECT world,x,y,z,yaw,pitch FROM tickets_location WHERE id = " + id + ";";
			rs = DatabaseManager.executeSqlQuery(sql);
			if(!rs.next())
				return null;
			
			String world = rs.getString("world");
			double x = rs.getDouble("x");
			double y = rs.getDouble("y");
			double z = rs.getDouble("z");
			float yaw = rs.getFloat("yaw");
			float pitch = rs.getFloat("pitch");
			
			TicketLocation loc = new TicketLocation(world, x, y, z, yaw, pitch);
			return loc;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static String getTicketSubmitter(int id) {
		ResultSet rs = null;
		try {
			String sql = "SELECT submitter FROM tickets WHERE id = " + id + ";";
			rs = DatabaseManager.executeSqlQuery(sql);
			if(!rs.next())
				return null;
			String submitter = rs.getString("submitter");
			return submitter;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static String getTicketAssignee(int id) {
		ResultSet rs = null;
		try {
			String sql = "SELECT assignee FROM tickets_special WHERE id = " + id + ";";
			rs = DatabaseManager.executeSqlQuery(sql);
			if(!rs.next())
				return null;
			String assignee = rs.getString("assignee");
			return assignee.equals("") ? null : assignee;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static boolean setTicketAssignee(int id, String assignee) {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OR IGNORE tickets_special SET assignee = ");
		sb.append("'").append(assignee).append("'");
		sb.append(" WHERE id = ");
		sb.append(id);
		sb.append(" ; ");
		
		return DatabaseManager.executeSqlUpdate(sb.toString());
	}
	
	public static boolean pushTicketComment(TicketComment comment) {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO tickets_comments ");
		sb.append(" (id, submitter, date, comment) ");
		sb.append(" VALUES ( ");
		sb.append(comment.getId());
		sb.append(" , ");
		sb.append("'").append(comment.getSubmitterName()).append("'");
		sb.append(" , ");
		sb.append(comment.getDateInMillis());
		sb.append(" , ");
		sb.append("'").append(Utils.prepareForDatabase(comment.getComment())).append("'");
		sb.append(" ); ");
		
		return DatabaseManager.executeSqlUpdate(sb.toString());
	}
	
	public static boolean insertPendingMessage(String ticketSubmitter, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO pending_messages ");
		sb.append(" (submitter, date, message) ");
		sb.append(" VALUES ( ");
		sb.append("'").append(ticketSubmitter).append("'");
		sb.append(" , ");
		sb.append(System.currentTimeMillis());
		sb.append(" , ");
		sb.append("'").append(Utils.prepareForDatabase(message)).append("'");
		sb.append(" ); ");
		
		return DatabaseManager.executeSqlUpdate(sb.toString());
	}
	
	public static int countPendingMessages(String submitter) {
		ResultSet rs = null;
		try {
			String sql = "SELECT COUNT(message) AS count FROM pending_messages WHERE submitter = '" + submitter + "';";
			rs = DatabaseManager.executeSqlQuery(sql);
			
			if(!rs.next())
				return 0;
			
			return rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static int countPendingTicketsWithPriority(TicketPriority priority) {
		ResultSet rs = null;
		try {
			String sql = "SELECT COUNT(id) AS count FROM tickets_special " +
				" WHERE status = '" + TicketStatus.PENDING + "' " +
				" AND priority = '" + priority + "';";
			rs = DatabaseManager.executeSqlQuery(sql);
			
			if(!rs.next())
				return 0;
			
			return rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static List<String> getPendingMessages(String submitter) {
		ResultSet rs = null;
		try {
			String sql = "SELECT message FROM pending_messages WHERE submitter = '" + submitter + "' ORDER BY date ASC;";
			rs = DatabaseManager.executeSqlQuery(sql);
			
			List<String> messageList = new ArrayList<String>();
			for(int i = 0; i < 100; i++) {
				if(!rs.next()) break;
				
				String message = rs.getString("message");
				if(message != null)
					messageList.add(message);
			}
			
			return messageList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static boolean deletePendingMessages(String submitter) {
		return DatabaseManager.executeSqlUpdate("DELETE FROM pending_messages WHERE submitter = '" + submitter + "';");
	}
	
	public static boolean insertOrUpdateTicket(Ticket ticket) {
		// If ticket's ID is 0, generate a new one
		if(ticket.getId() == 0)
			ticket.setId(getNextAvailableId());
		
		// Prepare variables
		int id = ticket.getId();
		long date = ticket.getDate();
		String submitter = ticket.getSubmitter();
		String message = Utils.prepareForDatabase(ticket.getMessage());
		String status = ticket.getStatus().toString();
		String priority = ticket.getPriority().toString();
		String assignee = ticket.getAssignee() == null ? "" : ticket.getAssignee();
		String world = Utils.prepareForDatabase(ticket.getTicketLocation().getWorldName());
		double x = ticket.getTicketLocation().getX();
		double y = ticket.getTicketLocation().getY();
		double z = ticket.getTicketLocation().getZ();
		float yaw = ticket.getTicketLocation().getYaw();
		float pitch = ticket.getTicketLocation().getPitch();
		
		// Construct SQL statements
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT OR IGNORE INTO tickets ('id','submitter','message','date') VALUES (")
			.append(id).append(",")
			.append("'").append(submitter).append("'").append(",")
			.append("'").append(message).append("'").append(",")
			.append(date)
			.append("); ");
		
		sb.append(" UPDATE tickets_special SET ")
			.append(" 	status = '").append(status).append("', ")
			.append(" 	priority = '").append(priority).append("', ")
			.append(" 	assignee = '").append(assignee).append("' ")
			.append(" WHERE id = ").append(id).append("; ");
		
		sb.append(" UPDATE tickets_location SET ")
			.append(" 	world = '").append(world).append("', ")
			.append(" 	x = ").append(x).append(", ")
			.append(" 	y = ").append(y).append(", ")
			.append(" 	z = ").append(z).append(", ")
			.append(" 	yaw = ").append(yaw).append(", ")
			.append(" 	pitch = ").append(pitch).append(" ")
			.append(" WHERE id = ").append(id).append("; ");
		
		String sql = sb.toString();
		boolean success = DatabaseManager.executeSqlUpdate(sql);
		
		// Return if process worked without failures
		return success;
	}
	
	public static boolean deleteTicket(int id) {
		return DatabaseManager.executeSqlUpdate("DELETE FROM tickets WHERE id = " + id + ";");
	}
	
	private static int getNextAvailableId() {
		ResultSet rs = null;
		try {
			String sql = "SELECT MAX(id) AS max FROM tickets;";
			rs = DatabaseManager.executeSqlQuery(sql);
			
			if(!rs.next())
				return 0;
			
			return rs.getInt("max") + 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static List<Integer> getTicketIDsFromSender(String submitter) {
		List<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT a.id AS id,submitter,status ");
			sb.append(" FROM tickets a,tickets_special b ");
			sb.append(" WHERE a.id = b.id AND submitter = '").append(submitter).append("' ");
			sb.append(" AND status <> '").append(TicketStatus.CLOSED).append("' ");
			sb.append(" ; ");
			
			String sql = sb.toString();
			rs = DatabaseManager.executeSqlQuery(sql);
			
			while(rs.next()) {
				ids.add(rs.getInt("id"));
			}
			
			return ids;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(rs != null) DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static List<Integer> getTicketIDsUnderSpecials(TicketStatus[] statuses, TicketPriority[] priorities) {
		List<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT a.id AS id,status,priority ");
			sb.append(" FROM tickets a,tickets_special b ");
			sb.append(" WHERE a.id = b.id AND ( ");
			for(int i = 0; i < statuses.length; i++) {
				sb.append(" status = '").append(statuses[i]).append("' ");
				if((i + 1) < statuses.length)
					sb.append(" OR ");
			}
			sb.append(" ) AND ( ");
			for(int j = 0; j < priorities.length; j++) {
				sb.append(" priority = '").append(priorities[j]).append("' ");
				if((j + 1) < priorities.length)
					sb.append(" OR ");
			}
			sb.append(" ); ");
			sb.append(" ; ");
			
			String sql = sb.toString();
			rs = DatabaseManager.executeSqlQuery(sql);
			
			while(rs.next()) {
				ids.add(rs.getInt("id"));
			}
			
			return ids;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(rs != null) DatabaseManager.closeResultSetAndStatement(rs);
		}
	}
	
	public static Ticket getTicket(int id) {
		// ResultSet resources
		ResultSet rsMain = null;
		ResultSet rsSpecial = null;
		ResultSet rsLocation = null;
		ResultSet rsComments = null;
		
		// Ticket to be built
		Ticket ticket = Ticket.empty();
		
		try {
			/* Main */
			String sqlMain = "SELECT submitter,message,date FROM tickets WHERE id = " + id + ";";
			rsMain = DatabaseManager.executeSqlQuery(sqlMain);
			if(!rsMain.next()) return null;
			
			ticket.setId(id);
			ticket.setSubmitter(rsMain.getString("submitter"));
			ticket.setMessage(rsMain.getString("message"));
			ticket.setDate(rsMain.getLong("date"));
			DatabaseManager.closeResultSetAndStatement(rsMain);
			
			/* Special */
			String sqlSpecial = "SELECT status,priority,assignee FROM tickets_special WHERE id = " + id + ";";
			rsSpecial = DatabaseManager.executeSqlQuery(sqlSpecial);
			if(!rsSpecial.next()) return null;
			try {
				ticket.setStatus(TicketStatus.valueOf(rsSpecial.getString("status").toUpperCase()));
				ticket.setPriority(TicketPriority.valueOf(rsSpecial.getString("priority").toUpperCase()));
				ticket.setAssignee(rsSpecial.getString("assignee"));
				DatabaseManager.closeResultSetAndStatement(rsSpecial);
			} catch (IllegalStateException e) {
				return null;
			}
			
			/* Location */
			String sqlLocation = "SELECT world,x,y,z,yaw,pitch FROM tickets_location WHERE id = " + id + ";";
			rsLocation = DatabaseManager.executeSqlQuery(sqlLocation);
			if(!rsLocation.next()) return null;
			
			String world = rsLocation.getString("world");
			double x = rsLocation.getDouble("x");
			double y = rsLocation.getDouble("y");
			double z = rsLocation.getDouble("z");
			float yaw = rsLocation.getFloat("yaw");
			float pitch = rsLocation.getFloat("pitch");
			
			DatabaseManager.closeResultSetAndStatement(rsLocation);
			ticket.setLocation(new TicketLocation(world, x, y, z, yaw, pitch));
			
			/* Comments */
			String sqlComments = "SELECT submitter,date,comment FROM tickets_comments WHERE id = " + id + ";";
			rsComments = DatabaseManager.executeSqlQuery(sqlComments);
			while(rsComments.next()) {
				String submitter = rsComments.getString("submitter");
				String comment = rsComments.getString("comment");
				long date = rsComments.getLong("date");
				
				TicketComment ticketComment = new TicketComment(id, submitter, date, comment);
				ticket.addComment(ticketComment);
			}
			DatabaseManager.closeResultSetAndStatement(rsComments);
			
			// Return ticket
			return ticket;
			
		} catch (Exception e) {
			// Return null if any exception is thrown
			e.printStackTrace();
			return null;
			
		} finally {
			// Close resources
			if(rsMain != null) DatabaseManager.closeResultSetAndStatement(rsMain);
			if(rsSpecial != null) DatabaseManager.closeResultSetAndStatement(rsSpecial);
			if(rsLocation != null) DatabaseManager.closeResultSetAndStatement(rsLocation);
			if(rsComments != null) DatabaseManager.closeResultSetAndStatement(rsComments);
		}
	}
	
	public static SimpleTicket getSimpleTicket(int id) {
		// ResultSet resources
		ResultSet rsMain = null;
		ResultSet rsSpecial = null;
		
		// Ticket to be built
		SimpleTicket ticket = SimpleTicket.empty();
		
		try {
			/* Main */
			String sqlMain = "SELECT submitter,message,date FROM tickets WHERE id = " + id + ";";
			rsMain = DatabaseManager.executeSqlQuery(sqlMain);
			if(!rsMain.next()) return null;
			
			ticket.setId(id);
			ticket.setSubmitter(rsMain.getString("submitter"));
			ticket.setMessage(rsMain.getString("message"));
			ticket.setDate(rsMain.getLong("date"));
			DatabaseManager.closeResultSetAndStatement(rsMain);
			
			/* Special */
			String sqlSpecial = "SELECT status,priority FROM tickets_special WHERE id = " + id + ";";
			rsSpecial = DatabaseManager.executeSqlQuery(sqlSpecial);
			if(!rsSpecial.next()) return null;
			try {
				ticket.setStatus(TicketStatus.valueOf(rsSpecial.getString("status").toUpperCase()));
				ticket.setPriority(TicketPriority.valueOf(rsSpecial.getString("priority").toUpperCase()));
				DatabaseManager.closeResultSetAndStatement(rsSpecial);
			} catch (IllegalStateException e) {
				return null;
			}
						
			// Return ticket
			return ticket;
			
		} catch (Exception e) {
			// Return null if any exception is thrown
			e.printStackTrace();
			return null;
			
		} finally {
			// Close resources
			if(rsMain != null) DatabaseManager.closeResultSetAndStatement(rsMain);
			if(rsSpecial != null) DatabaseManager.closeResultSetAndStatement(rsSpecial);
		}
	}
}
