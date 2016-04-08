package me.rafaskb.ticketmaster.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

import me.rafaskb.ticketmaster.TicketMaster;

public class DatabaseManager {
	private static final String FILENAME = "tickets.db";
	private static final int VERSION = 2;
	
	private static Connection conn;
	
	public static void open() {
		if(conn != null)
			return;
		
		try {
			touchDataFolder();
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(getDatabaseUrl());
			conn.setAutoCommit(true);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		if(conn != null) try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTablesIfNotExists() {
		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS [config] ( ");
		sb.append(" 	[key] TEXT NOT NULL ON CONFLICT ABORT UNIQUE ON CONFLICT REPLACE, ");
		sb.append(" 	[value] TEXT NOT NULL ON CONFLICT ABORT ");
		sb.append(" ); ");
		
		sb.append(" CREATE TABLE IF NOT EXISTS [tickets] ( ");
		sb.append(" 	[id] INTEGER NOT NULL ON CONFLICT ABORT UNIQUE ON CONFLICT REPLACE, ");
		sb.append(" 	[submitter] TEXT, ");
		sb.append(" 	[message] TEXT, ");
		sb.append(" 	[date] INTEGER NOT NULL DEFAULT 0 ");
		sb.append(" ); ");
		
		sb.append(" CREATE TABLE IF NOT EXISTS [tickets_special] ( ");
		sb.append(" 	[id] INTEGER NOT NULL ON CONFLICT ABORT UNIQUE ON CONFLICT REPLACE REFERENCES [tickets](id), ");
		sb.append(" 	[status] TEXT NOT NULL DEFAULT 'PENDING', ");
		sb.append(" 	[priority] TEXT NOT NULL DEFAULT 'NORMAL', ");
		sb.append(" 	[assignee] TEXT ");
		sb.append(" ); ");
		
		sb.append(" CREATE TABLE IF NOT EXISTS [tickets_location] ( ");
		sb.append(" 	[id] INTEGER NOT NULL ON CONFLICT ABORT UNIQUE ON CONFLICT REPLACE REFERENCES [tickets](id), ");
		sb.append(" 	[world] TEXT NOT NULL DEFAULT 'world', ");
		sb.append(" 	[x] REAL NOT NULL DEFAULT 0, ");
		sb.append(" 	[y] REAL NOT NULL DEFAULT 0, ");
		sb.append(" 	[z] REAL NOT NULL DEFAULT 0, ");
		sb.append(" 	[yaw] REAL NOT NULL DEFAULT 0, ");
		sb.append(" 	[pitch] REAL NOT NULL DEFAULT 0 ");
		sb.append(" ); ");
		
		sb.append(" CREATE TABLE IF NOT EXISTS [tickets_comments] ( ");
		sb.append(" 	[id] INTEGER NOT NULL ON CONFLICT ABORT, ");
		sb.append(" 	[submitter] TEXT, ");
		sb.append(" 	[date] INTEGER NOT NULL DEFAULT 0, ");
		sb.append(" 	[comment] TEXT ");
		sb.append(" ); ");
		
		sb.append(" CREATE TABLE IF NOT EXISTS [pending_messages] ( ");
		sb.append(" 	[submitter] TEXT, ");
		sb.append(" 	[date] INTEGER NOT NULL DEFAULT 0, ");
		sb.append(" 	[message] TEXT ");
		sb.append(" ); ");
		
		sb.append(" CREATE TRIGGER IF NOT EXISTS [ondelete] BEFORE DELETE ON [tickets] BEGIN ");
		sb.append(" 	DELETE FROM tickets_special WHERE [id] = OLD.[id]; ");
		sb.append(" 	DELETE FROM tickets_location WHERE [id] = OLD.[id]; ");
		sb.append(" 	DELETE FROM tickets_comments WHERE [id] = OLD.[id]; ");
		sb.append(" END; ");
		
		sb.append(" CREATE TRIGGER IF NOT EXISTS [oncreate] AFTER INSERT ON [tickets] BEGIN ");
		sb.append(" 	INSERT INTO tickets_special (id) VALUES (NEW.id); ");
		sb.append(" 	INSERT INTO tickets_location (id) VALUES (NEW.id); ");
		sb.append(" END; ");
		
		String createTableSql = sb.toString();
		String insertVersionSql = "INSERT INTO config VALUES ('version', " + VERSION + ");";
		
		executeSqlUpdate(createTableSql);
		executeSqlUpdate(insertVersionSql);
	}
	
	static boolean executeSqlUpdate(String sql) {
		// Make sure the database is open
		open();
		
		Statement stmt = null;
		try {
			// Create a new statement and execute it
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			return true;
		} catch (SQLException e) {
			// Catches errors
			e.printStackTrace();
			return false;
		} finally {
			try {
				// Tries to close the statement.
				if(stmt != null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	static ResultSet executeSqlQuery(String sql) {
		// Make sure the database is open
		open();
		
		Statement stmt = null;
		try {
			// Create a new statement and execute it
			stmt = conn.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			// Catches errors
			e.printStackTrace();
			
			// And tries to close the statement
			if(stmt != null) try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
	static void closeResultSetAndStatement(ResultSet rs) {
		try {
			if(rs == null) return;
			rs.close();
			rs.getStatement().close();
		} catch (SQLException e) {
		}
	}
	
	private static String getDatabaseUrl() {
		String dataFolder = TicketMaster.getInstance().getDataFolder().toString();
		return "jdbc:sqlite:" + dataFolder + File.separatorChar + FILENAME;
	}
	
	private static void touchDataFolder() {
		File dataFolder = TicketMaster.getInstance().getDataFolder();
		if(!dataFolder.exists())
			dataFolder.mkdir();
	}
	
	private static int getDatabaseVersion() {
		try (ResultSet rs = executeSqlQuery("SELECT [value] FROM [config] WHERE [key] = 'version';)")) {
			if(rs.next())
				return rs.getInt("value");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void updateDatabase() {
		int dbVersion = getDatabaseVersion();
		if(dbVersion == 0)
			return;
		
		while(dbVersion != VERSION) {
			// v1 -> v2
			if(dbVersion == 1) {
				// Delete old trigger (bugged)
				executeSqlUpdate(" DROP TRIGGER IF EXISTS ondelete; ");
				
				// Create correct trigger
				StringBuilder sbNew = new StringBuilder();
				sbNew.append(" CREATE TRIGGER IF NOT EXISTS [ondelete] BEFORE DELETE ON [tickets] BEGIN ");
				sbNew.append(" 	DELETE FROM tickets_special WHERE [id] = OLD.[id]; ");
				sbNew.append(" 	DELETE FROM tickets_location WHERE [id] = OLD.[id]; ");
				sbNew.append(" 	DELETE FROM tickets_comments WHERE [id] = OLD.[id]; ");
				sbNew.append(" END; ");
				executeSqlUpdate(sbNew.toString());
				
				// Update version
				executeSqlUpdate(" UPDATE OR IGNORE [config] SET [value] = 2 WHERE [key] = 'version'; ");
				dbVersion = 2;
				
				// Obtain valid IDs
				TreeSet<Integer> validIds = new TreeSet<>();
				try (ResultSet rs = executeSqlQuery(" SELECT id FROM [tickets]; ")) {
					while(rs.next())
						validIds.add(rs.getInt("id"));
				} catch (SQLException e) {
				}
				
				// Obtain all IDs
				TreeSet<Integer> allIds = new TreeSet<>();
				StringBuilder sbAll = new StringBuilder();
				sbAll.append(" SELECT id FROM [tickets_special] ");
				sbAll.append(" UNION ");
				sbAll.append(" SELECT id FROM [tickets_location] ");
				sbAll.append(" UNION ");
				sbAll.append(" SELECT id FROM [tickets_comments]; ");
				try (ResultSet rs = executeSqlQuery(sbAll.toString())) {
					while(rs.next())
						allIds.add(rs.getInt("id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				// Delete invalid IDs
				for(int id : allIds)
					if(!validIds.contains(id))
						executeSqlUpdate(" DELETE FROM [tickets] WHERE [id] = " + id + ";");
			}
		}
	}
}
