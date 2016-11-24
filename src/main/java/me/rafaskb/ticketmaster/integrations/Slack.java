package me.rafaskb.ticketmaster.integrations;

import com.google.gson.Gson;
import me.rafaskb.ticketmaster.TicketMaster;
import me.rafaskb.ticketmaster.models.Ticket;
import me.rafaskb.ticketmaster.models.TicketPriority;
import me.rafaskb.ticketmaster.sql.Controller;
import me.rafaskb.ticketmaster.utils.ConfigLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Handles broadcasting ticket events to configured Slack webhook
 * @author Robrotheram
 * @author RoyCurtis
 */
public class Slack
{
    private static final Gson gson = new Gson();

    private static class SlackMessage
    {
        private String username = "Ticket Master Robot";
        private String icon_url = "";
        private String text     = "";
        SlackMessage() {}

        void setIcon(String who)
        {
            icon_url = "https://minotar.net/avatar/" + who + "/100.png";
        }

        void setText(String[] msg, Object... parts)
        {
            String combined = String.join("\n", msg);

            text = String.format(combined, parts);
        }

        void setText(String msg, Object... parts)
        {
            text = String.format(msg, parts);
        }
    }

    public static void notifyNewTicket(Ticket ticket)
    {
        SlackMessage msg  = new SlackMessage();
        int          id   = ticket.getId();
        String[]     text = new String[]
        {
            "*%s* created ticket *#%d*:",
            "> _%s_",
            "...in world *`%s`* at *`%d %d %d`*" + generateDynmapURL(ticket),
            "",
            "• _To claim:_ `/ticket claim %d`",
            "• _To teleport to:_ `/ticket tp %d`",
            "• _To comment:_ `/ticket comment %d <message>`",
            "• _To close:_ `/ticket close %d <reason>`",
            "• _There are %d open tickets (including this one)_"
        };

        msg.setIcon( ticket.getSubmitter() );
        msg.setText(text,
            ticket.getSubmitter(), id,
            ticket.getMessage(),
            ticket.getTicketLocation().getWorldName(),
            (int) ticket.getTicketLocation().getX(),
            (int) ticket.getTicketLocation().getY(),
            (int) ticket.getTicketLocation().getZ(),
            id, id, id, id,
            Controller.countPendingTicketsWithPriority(TicketPriority.NORMAL)
        );

        sendMessage(msg);
    }

    public static void notifyClaimTicket(int id, String who, String submitter)
    {
        SlackMessage msg = new SlackMessage();

        msg.setIcon(who);

        if ( who.equals(submitter) )
            msg.setText("*%s* claimed their own ticket *#%d*", who, id);
        else
            msg.setText("*%s* claimed *%s's* ticket *#%d*", who, submitter, id);

        sendMessage(msg);
    }

    public static void notifyCloseTicket(int id, String who, String submitter, String reason)
    {
        SlackMessage msg = new SlackMessage();
        msg.setIcon(who);

        String text;
        if ( who.equals(submitter) )
            text = String.format("*%s* closed their own ticket ~*#%d*~", who, id);
        else
            text = String.format("*%s* closed *%s's* ticket ~*#%d*~", who, submitter, id);

        if (reason == null)
            msg.setText(text);
        else
            msg.setText(new String[]
            {
                text + ":",
                "> _%s_"
            }, reason);

        sendMessage(msg);
    }

    public static void notifyCommentTicket(int id, String who, String submitter, String comment)
    {
        SlackMessage msg = new SlackMessage();
        msg.setIcon(who);

        if ( who.equals(submitter) )
            msg.setText(new String[]
            {
                "*%s* commented on their own ticket *#%d*:",
                "> _%s_"
            }, who, id, comment);
        else
            msg.setText(new String[]
            {
                "*%s* commented on *%s's* ticket *#%d*:",
                "> _%s_"
            }, who, submitter, id, comment);

        sendMessage(msg);
    }

    public static void notifyReopenTicket(int id, String who, String submitter)
    {
        SlackMessage msg = new SlackMessage();
        msg.setIcon(who);

        if ( who.equals(submitter) )
            msg.setText("*%s* re-opened their own ticket *#%d*", who, id);
        else
            msg.setText("*%s* re-opened *%s's* ticket *#%d*", who, submitter, id);

        sendMessage(msg);
    }

    private static void sendMessage(SlackMessage msg)
    {
        if (!ConfigLoader.slack.enabled)
            return;

        Logger log = TicketMaster.getInstance().getLogger();

        try
        {
            URL    url   = new URL(ConfigLoader.slack.webhookURL);
            String json  = gson.toJson(msg);
            byte[] bytes = (
                "payload=" + URLEncoder.encode(json, "UTF-8")
            ).getBytes("UTF-8");

            if (ConfigLoader.slack.debug)
                log.info("Outgoing JSON: " + json);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(bytes.length) );
            conn.setDoOutput(true);
            conn.getOutputStream().write(bytes);

            try (
                InputStream       input  = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(input, "UTF-8")
            )
            {
                // Apparently necessary for post to actually go through
                String response = "";
                for ( int c = reader.read(); c != -1; c = reader.read() )
                    response += (char) c;

                if (ConfigLoader.slack.debug)
                    log.info("Slack response: " + response);
            }
        }
        catch (MalformedURLException e)
        {
            log.warning("Slack WebHook URL appears to be wrong!");
            log.warning( e.toString() );
        }
        catch (Exception e)
        {
            log.warning( "Slack error: " + e.toString() );
        }
    }

    private static String generateDynmapURL(Ticket ticket)
    {
        if (ConfigLoader.slack.dynmapURL == null)
            return "";

        String mapUrl = " - [<%s/?worldname=%s&mapname=flat&zoom=6&x=%d&y=%d&z=%d|Dynmap>]";

        return String.format(mapUrl,
            ConfigLoader.slack.dynmapURL,
            ticket.getTicketLocation().getWorldName(),
            (int) ticket.getTicketLocation().getX(),
            (int) ticket.getTicketLocation().getY(),
            (int) ticket.getTicketLocation().getZ()
        );
    }
}