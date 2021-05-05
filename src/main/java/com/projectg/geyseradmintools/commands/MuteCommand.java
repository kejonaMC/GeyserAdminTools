package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class MuteCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        try {
        if (cmd.getName().equalsIgnoreCase("gmute") && player.hasPermission("geyseradmintools.muteplayer")) {
            try {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ChatColor.DARK_RED + "[GeyserAdminTools] Could not find player! Perhaps wrong usage ? /gmute <username> <days> <reason>");
                    return true;
                }
                String day = args[1];
                String time = LocalDate.now().plusDays(Long.parseLong(day)).toString();
                String reason = args[2];
                String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Mutetable
                        + sql);
                insert.setString(1, target.getUniqueId().toString());
                insert.setString(2, reason);
                insert.setString(3, target.getName());
                insert.setString(4, time);
                insert.executeUpdate();
                target.sendMessage(ChatColor.GOLD + "You where muted till: " + ChatColor.WHITE + time + ChatColor.GOLD + "for: " + ChatColor.GOLD + reason);
                player.sendMessage(ChatColor.DARK_AQUA + "[GeyserAdminTools] Player " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + " is muted");
                Gat.logger.info("Player " + player.getName() + " has muted " + target.getName() + " till: " + time + " for reason: " + reason);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        } catch (IllegalArgumentException |ArrayIndexOutOfBoundsException | CommandException e) {
            player.sendMessage(ChatColor.DARK_RED + "[GeyserAdminTools] Perhaps wrong usage ? /gmute <username> <amount of days> <reason>");
        }
        return true;
    }

}