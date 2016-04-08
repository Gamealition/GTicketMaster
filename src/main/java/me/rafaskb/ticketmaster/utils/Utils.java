package me.rafaskb.ticketmaster.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
	static Pattern pattern_single_quotes = Pattern.compile("(['])");
	
	public static String prepareForDatabase(String string) {
		Matcher m = pattern_single_quotes.matcher(string);
		if(m.find())
			string = m.replaceAll("''");
		return string;
	}
	
	/**
	 * Converts an array of arguments (args) or any other array of strings into a plain string.
	 * <p/>
	 * <b>Example:</b><br>
	 * Converting <i>{"Hello", "world", "foo", "bar!!"</i>} from the index 2 will return "<i>foo bar!!</i>"
	 */
	public static String convertArgumentsToString(String[] args, Integer starterIndex) {
		try {
			StringBuilder builder = new StringBuilder();
			for(int c = starterIndex; c < args.length; c++) {
				String fragment = args[c];
				builder.append(fragment);
				if(c + 1 < args.length)
					builder.append(" ");
			}
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFriendlyElapsedTime(long since) {
		long diff = System.currentTimeMillis() - since;
		
		long secondInMillis = 1000;
		long minuteInMillis = secondInMillis * 60;
		long hourInMillis = minuteInMillis * 60;
		long dayInMillis = hourInMillis * 24;
		
		long elapsedDays = diff / dayInMillis;
		diff = diff % dayInMillis;
		long elapsedHours = diff / hourInMillis;
		diff = diff % hourInMillis;
		long elapsedMinutes = diff / minuteInMillis;
		diff = diff % minuteInMillis;
		long elapsedSeconds = diff / secondInMillis;
		
		if(elapsedDays != 0)
			return elapsedDays + " day" + (elapsedDays == 1 ? "" : "s") + " ago";
		if(elapsedHours != 0)
			return elapsedHours + " hour" + (elapsedHours == 1 ? "" : "s") + " ago";
		if(elapsedMinutes != 0)
			return elapsedMinutes + " min" + (elapsedMinutes == 1 ? "" : "s") + " ago";
		if(elapsedSeconds != 0)
			return elapsedSeconds + " sec" + (elapsedSeconds == 1 ? "" : "s") + " ago";
		return "Just now";
	}
	
	@SuppressWarnings("deprecation")
	public static boolean sendActionMessage(String ticketSubmitter, String message) {
		Player player = Bukkit.getPlayerExact(ticketSubmitter);
		if(player != null && player.isOnline()) {
			Lang.sendMessage(player, message);
			return true;
		}
		return false;
	}
	
	public static String[] increaseArgs(String[] args) {
		String[] newArgs = new String[args.length + 1];
		newArgs[0] = "";
		
		for(int i = 0; i < args.length; i++) {
			newArgs[i + 1] = args[i];
		}
		
		return newArgs;
	}
	
	@SuppressWarnings("unchecked")
	// FIXME Stop using reflection
	public static Player[] getOnlinePlayers() {
		try {
			Method method = Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]);
			if(method.getReturnType() == Collection.class) {
				return ((Collection<Player>) method.invoke(null, new Object[0])).toArray(new Player[0]);
			} else {
				return ((Player[]) method.invoke(null, new Object[0]));
			}
		} catch (Exception ex) {
			return new Player[0];
		}
	}
	
}
