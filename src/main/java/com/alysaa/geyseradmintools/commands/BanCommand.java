package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.DatabaseSetup;
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

public class BanCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("gban") && player.hasPermission("geyseradmintools.gban")) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "[GeyserAdminTools] Perhaps wrong usage ? /gban <username> <amount of days> <reason>");
                return true;
            }
            String day = args[1];
            String time = LocalDate.now().plusDays(Long.parseLong(day)).toString();
            String reason = args[2];
            try {
                String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Bantable
                        + sql);
                insert.setString(1, target.getUniqueId().toString());
                insert.setString(2, reason);
                insert.setString(3, target.getName());
                insert.setString(4, time);
                insert.executeUpdate();
                target.kickPlayer("you where banned for: " + reason);
                player.sendMessage("[GeyserAdminTools] Player " + target.getName() + " is banned");
                Gat.logger.info("Player " + player.getName() + " has banned " + target.getName() + " till: " + time + " for reason: " + reason);
                //end
            } catch (IllegalArgumentException | CommandException e) {
                player.sendMessage(ChatColor.YELLOW + "[GeyserAdminTools] Perhaps wrong usage ? /gban <username> <amount of days> <reason>");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return true;
    }
}
