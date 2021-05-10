package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.forms.ReportForm;
import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Messages.get("permission.command.error"));
            return true;
        }
        Player player = (Player) sender;
        try {
        if (!player.hasPermission("geyseradmintools.reportplayer")) {
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
            try {
                Player player1 = Bukkit.getServer().getPlayer(args[0]);
                if (player1 == null) {
                    player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
                    return true;
                }
                try {
                    StringBuilder message = new StringBuilder(args[1]);
                    for (int arg = 2; arg < args.length; arg++) {
                        message.append(" ").append(args[arg]);
                    }
                    String report = message.toString();
                    String sql = "(UUID,REPORT,REPORTED,REPORTING,DATE) VALUES (?,?,?,?,?)";
                    PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Reporttable
                            + sql);
                    insert.setString(1, player1.getUniqueId().toString());
                    insert.setString(2, report);
                    insert.setString(3, player1.getName());
                    insert.setString(4, player.getName());
                    insert.setString(5, LocalDate.now().toString());
                    insert.executeUpdate();
                    // Player inserted now
                    player.sendMessage(ChatColor.GREEN + Messages.get("report.command.player.message1"));
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                            onlinePlayers.sendMessage(ChatColor.DARK_AQUA + Messages.get("report.command.player.message2") + ChatColor.AQUA + player.getName() + ChatColor.DARK_AQUA + Messages.get("report.command.player.message3"));
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
            }
        }
    } catch (IllegalArgumentException |ArrayIndexOutOfBoundsException | CommandException e) {
            player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
    }
        return true;
    }
}

