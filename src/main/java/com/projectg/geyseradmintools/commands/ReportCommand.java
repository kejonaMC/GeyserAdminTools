package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.database.ReportData;
import com.projectg.geyseradmintools.forms.ReportForm;
import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
                    Player rPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (rPlayer == null) {
                        player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
                        return true;
                    }
                    try {
                        StringBuilder message = new StringBuilder(args[1]);
                        for (int arg = 2; arg < args.length; arg++) {
                            message.append(" ").append(args[arg]);
                        }
                        String startDate = LocalDate.now().toString();
                        String report = message.toString();
                        ReportData.addReport(rPlayer, startDate, report, rPlayer.getName(), player.getName());
                        player.sendMessage(ChatColor.GREEN + Messages.get("report.command.player.message1"));
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                onlinePlayers.sendMessage(ChatColor.DARK_AQUA + Messages.get("report.command.player.message2", player.getName()));
                            }
                        }
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
                    }
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | CommandException e) {
                    player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
                }
            }
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED + Messages.get("report.command.error"));
        }
        return true;
    }
}

