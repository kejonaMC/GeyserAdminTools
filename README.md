[![Build Status](https://ci.alysaa.net/job/GeyserAdminTools/job/master/badge/icon)](https://ci.alysaa.net/job/GeyserAdminTools/job/master)
[![License](https://img.shields.io/badge/License-GPL-orange)](https://github.com/ProjectG-Plugins/GeyserAdminTools/blob/master/LICENSE)
[![bStats Spigot](https://img.shields.io/bstats/servers/10943?color=yellow&label=Spigot%20servers)](https://bstats.org/plugin/bukkit/GeyserAdminTools/10943)
[![Discord](https://img.shields.io/discord/806179549498966058?color=7289da&label=discord&logo=discord&logoColor=white)](https://discord.gg/xXzzdAXa2b)

# GeyserAdminTools
### GeyserAdmintools is a plugin that makes server management easier on a bedrock client!

[Here](https://discord.gg/xXzzdAXa2b) is our Discord link, you can come to get support, get GitHub feeds, or just hang out. Don't ping anyone that doesn't have the pingable role!

Note: This is NOT an official GeyserMC plugin. It is made to work with GeyserMC but it is not maintained or produced by GeyserMC. If you need support with this plugin, do not ask Geyser devs, and instead, go to our Discord server linked above.

## Downloading

you can download the plugin [our CI](https://ci.alysaa.net/job/GeyserAdminTools/job/master/).

## Installation

GeyserAdmintools is a spigot only plugin, just put the GeyserAdminTools jar in your servers plugins folder and restart the server. Admintools will generate a config.yml and a database file. please do not remove the db file!

## Usage

| Commands | Permission |
| --- | --- |
| `/gadmin` | `geyseradmintools.gadmin` |
| `/gban <playername> <days> <reason>` | `geyseradmintools.banplayer` |
| `/gunban <playername>` | `geyseradmintools.gunban` |
| `/gmute <playername> <days> <reason>` | `geyseradmintools.muteplayer` |
| `/gunmute <playername>` | `geyseradmintools.gunmute` |
| `/greport` | `geyseradmintools.geyseradmintools.reportplayer` |
| `/gviewban` | `geyseradmintools.viewreports` |
| `/gviewreport` | `geyseradmintools.viewbans` |

## Permission

| Permission | Description |
| --- | --- |
| `geyseradmintools.admintool` | `Allows to open admin form` |
| `geyseradmintools.modtool` | `Allows to open mod form` |
| `geyseradmintools.mobtool` | `Allows to open mob form` |
| `geyseradmintools.servertool` | `Allows to open server form` |
| `geyseradmintools.item` | `gives player admin item on join` |
| `geyseradmintools.customcommands` | `Allows to open Custom Commands form` |
| `geyseradmintools.banplayer` | `Allows to open ban form & use gban command` |
| `geyseradmintools.muteplayer` | `Allows to open mute form & use gmute command` |
| `geyseradmintools.reportplayer` | `Allows to use report player java command & report form` |
| `geyseradmintools.viewreports` | `Allows to use view button/ticket gui` |
| `geyseradmintools.viewbans` | `Allows to use view bans gui` |




## bStats
[Spigot stats](https://bstats.org/plugin/bukkit/GeyserAdminTools/10943)

## Release History
* 1.2.0
  Added ban view and report view gui's for java players.
* 1.1.0
  Official beta testing release. Added Mysql/SQLite support for ban/mute 

## Meta

The project is owned by:
- [ProjectG](https://github.com/ProjectG-Plugins)
</br>
