package me.rafaskb.ticketmaster.utils;

import me.rafaskb.ticketmaster.TicketMaster;

public class ConfigLoader {

    private static String slackwebhookurl;
    private static String dynmapurl;

    public static boolean isSlackEnable() {
        return slackEnable;
    }

    public static void setSlackEnable(boolean slackEnable) {
        ConfigLoader.slackEnable = slackEnable;
    }

    public static String getDynmapurl() {
        return dynmapurl;
    }

    public static void setDynmapurl(String dynmapurl) {
        ConfigLoader.dynmapurl = dynmapurl;
    }

    public static String getSlackwebhookurl() {
        return slackwebhookurl;
    }

    public static void setSlackwebhookurl(String slackwebhookurl) {
        ConfigLoader.slackwebhookurl = slackwebhookurl;
    }

    private static boolean slackEnable;




    public static void reloadConfig(){
        //See "Creating you're defaults"
        TicketMaster.getInstance().getConfig().options().copyDefaults(true); // NOTE: You do not have to use "plugin." if the class extends the java plugin
        saveConfig();
        //Get data
        setDynmapurl(TicketMaster.getInstance().getConfig().getString("dynmapurl"));
        setSlackwebhookurl(TicketMaster.getInstance().getConfig().getString("slackwebhookURL"));
        setSlackEnable(TicketMaster.getInstance().getConfig().getBoolean("enableSlackintergration"));


    }

    public static void saveConfig(){
        //Save the config whenever you manipulate it
        TicketMaster.getInstance().saveConfig();
    }







}
