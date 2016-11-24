**GTicketMaster** is a Bukkit plugin that allows players to file tickets. Staff can then handle
these tickets, keeping track of incidents and helping players get moderation as asked.

GTicketMaster is a downstream fork of @justin393's [TicketMaster][TM], with Slack integration by
@Robrotheram and @RoyCurtis.

# Features

*Originally from the TicketMaster readme*

* Ticket statuses (Pending, On Hold, Claimed and Closed)
* Ticket priorities (Low, Normal, High and Critical)
* Players can create new tickets, comment on them and close them
* Staff can manage tickets by changing their priorities, their status, commenting on them, etc
* Staff can teleport to where the ticket was created, but only if they have permission
* Players are notified about changes made on their tickets, even if they're offline at the time.
* Only two commands: `/ticket` and `/tickets`
* You can edit the plugin messages through the lang.yml file, including the plugin name
* Uses SQLite to store ticket data

## Slack integration

![Example screenshot of ticket notifications in Slack](http://i.imgur.com/ErnMjfB.png)

GTicketMaster can integrate with your [Slack][SLACK] team, using [incoming WebHooks][WEBHOOKS].
This integration works by announcing new tickets to a given channel. It shows the player's head and
name, as well as the ticket ID, description and Dynmap link (if available).

### Setup

1. Go to https://YourTeamName.slack.com/apps/build/custom-integration
1. Select "Incoming WebHooks"
1. Choose Channel or User for the messages to go to
1. Copy the Webhook URL (e.g. `https://hooks.slack.com/services/xxx/yyy/zzz`)
1. Paste the Webhook URL into `webhookURL` in `config.yml`
1. Make sure `enabled` under `slack` is set to `true` in `config.yml`

# Building, debugging and debug logging

For instructions and screenshots on how to. . .

* Compile this plugin from scratch
* Build a JAR of this plugin
* Debug this plugin on a server
* Enable debug logging levels such as `FINE` and `FINER`

. . .[please follow the linked guide on this Google document.][BUILD]

## License
As GTicketMaster is a fork of TicketMaster by RafaSKB and justin393, GTicketMaster is licensed the
under the Apache 2.0 license. Please see `LICENSE` or [this website][LICENSE] for the full license.


[TM]: https://github.com/justin393/ticket-master
[BUILD]: https://docs.google.com/document/d/1TTDXG7IZ9M0D2-rzbILAWg1CKjynHK8fNGxbf3W4wBk/view
[LICENSE]: http://www.apache.org/licenses/LICENSE-2.0
[SLACK]: http://slack.com
[WEBHOOKS]: https://api.slack.com/incoming-webhooks