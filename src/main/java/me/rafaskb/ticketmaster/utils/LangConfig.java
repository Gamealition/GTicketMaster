package me.rafaskb.ticketmaster.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.rafaskb.ticketmaster.TicketMaster;

public class LangConfig {
	private static final String FILENAME = "lang.yml";
	private static File file = null;
	private static FileConfiguration config = null;
	
	protected static FileConfiguration getConfig() {
		if(config == null)
			reloadConfig();
		return config;
	}
	
	public static void reloadConfig() {
		saveDefaultConfig();
		
		if(config == null)
			file = new File(getDataFolder(), FILENAME);
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	protected static void saveDefaultConfig() {
		if(file == null)
			file = new File(getDataFolder(), FILENAME);
		if(!file.exists())
			TicketMaster.getInstance().saveResource(FILENAME, false);
	}
	
	protected static void saveDefaultValue(String key) {
		try {
			InputStream is = TicketMaster.getInstance().getResource(FILENAME);
			if(is != null) {
				YamlConfiguration resourceConfig;
				resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is, "UTF-8"));
				String value = resourceConfig.getString(key);
				
				if(value != null) {
					try {
						FileConfiguration realConfig = getConfig();
						realConfig.set(key, value);
						realConfig.save(file);
					} catch (IOException | NullPointerException e) {
						TicketMaster.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + FILENAME, e);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// Should never happen
			e.printStackTrace();
		}
	}
	
	private static File getDataFolder() {
		return TicketMaster.getInstance().getDataFolder();
	}
	
}
