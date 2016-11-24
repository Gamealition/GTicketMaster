package me.rafaskb.ticketmaster.utils;

import com.google.common.base.Strings;
import me.rafaskb.ticketmaster.TicketMaster;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigLoader
{
    public final  static Slack slack = new Slack();
    public static class  Slack
    {
        public boolean enabled    = true;
        public boolean debug      = true;
        public String  webhookURL = null;
        public String  dynmapURL  = null;
    }

    public static void reloadConfig()
    {
        Plugin tMaster = TicketMaster.getInstance();

        tMaster.saveDefaultConfig();
        tMaster.reloadConfig();

        FileConfiguration config = tMaster.getConfig();

        slack.enabled    = config.getBoolean("slack.enabled", slack.enabled);
        slack.debug      = config.getBoolean("slack.debug", slack.debug);
        slack.webhookURL = config.getString("slack.webhookURL", slack.webhookURL);
        slack.dynmapURL  = config.getString("slack.dynmapURL", slack.dynmapURL);

        if ( Strings.isNullOrEmpty(slack.dynmapURL) )
            slack.dynmapURL = null;
    }
}
