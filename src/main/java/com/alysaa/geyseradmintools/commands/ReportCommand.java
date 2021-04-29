package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.DatabaseSetup;
import com.alysaa.geyseradmintools.forms.ReportForm;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("geyseradmintools.greport")) {
                return true;
            }

            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                try {
                    ReportForm.reportPlayers(player);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                    Player player1 = Bukkit.getServer().getPlayer(args[0]);
                    if (player1 == null) {
                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] The player you are trying to report is not online or wrong usage of the command. /greport <Username> <What to report>");
                        return true;
                    }
                    String report = args[1];
                try {
                    String sql = "(UUID,REPORT,USERNAME) VALUES (?,?,?)";
                    PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Reporttable
                            + sql);
                    insert.setString(1, player1.getUniqueId().toString());
                    insert.setString(2, report);
                    insert.setString(3, player1.getName());
                    insert.executeUpdate();
                    // Player inserted now
                    player.sendMessage(ChatColor.GREEN + "Report has been delivered");
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                            onlinePlayers.sendMessage(ChatColor.DARK_AQUA + "New report from "  + ChatColor.AQUA + player.getName() +ChatColor.DARK_AQUA + " has been received ");
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Gat.plugin.getLogger().info("This command only works in-game!");
        }
        return true;
    }
}
