package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.DatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnbanCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("gunban") && player.hasPermission("geyseradmintools.gunban")) {
            OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[0]);
            if (!offplayer.hasPlayedBefore()) {
                for (OfflinePlayer target : Bukkit.getOfflinePlayers()) {
                    if (args[0].equalsIgnoreCase(target.getName())) {
                        try {
                            PreparedStatement statement = DatabaseSetup.getConnection()
                                    .prepareStatement("DELETE FROM " + DatabaseSetup.Bantable + " WHERE UUID=?");
                            statement.setString(1, target.getUniqueId().toString());
                            statement.execute();
                            player.sendMessage(ChatColor.DARK_AQUA + "[GeyserAdminTools] Player " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA +  " is unbanned");
                            Gat.logger.info("Player " + player.getName() + " has unbanned " + target.getName());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        }
        return true;
    }
}

