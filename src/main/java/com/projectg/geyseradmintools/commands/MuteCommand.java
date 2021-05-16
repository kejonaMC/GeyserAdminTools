package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.database.MuteData;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class MuteCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Messages.get("permission.command.error"));
            return true;
        }
        Player player = (Player) sender;
        try {
            if (cmd.getName().equalsIgnoreCase("gmute") && player.hasPermission("geyseradmintools.muteplayer")) {
                try {
                    Player mPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (mPlayer == null) {
                        player.sendMessage(ChatColor.DARK_RED + Messages.get("mute.command.error"));
                        return true;
                    }
                    String day = args[1];
                    String endDate = LocalDate.now().plusDays(Long.parseLong(day)).toString();
                    String reason = args[2];
                    String startDate = LocalDate.now().toString();
                    MuteData.addMute(mPlayer, startDate, endDate, reason, mPlayer.getName(), player.getName());
                    mPlayer.sendMessage(ChatColor.GOLD + Messages.get("mute.command.player.message1", endDate, reason));
                    player.sendMessage(ChatColor.DARK_AQUA + Messages.get("mute.command.player.message2", mPlayer.getName()));
                    Gat.plugin.getLogger().info("Player " + player.getName() + " has muted " + mPlayer.getName() + " till: " + endDate + " for reason: " + reason);
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | CommandException e) {
                    player.sendMessage(ChatColor.DARK_RED + Messages.get("mute.input.error"));
                }
            }
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED + Messages.get("mute.command.error"));
        }
        return true;
    }
}