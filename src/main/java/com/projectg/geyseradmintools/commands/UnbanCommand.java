package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnbanCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Messages.get("permission.command.error"));
            return true;
        }
        Player player = (Player) sender;
        try {
        if (cmd.getName().equalsIgnoreCase("gunban") && player.hasPermission("geyseradmintools.gunban")) {
            OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[0]);
            if (!offplayer.hasPlayedBefore()) {
                for (OfflinePlayer target : Bukkit.getOfflinePlayers()) {
                    if (args[0].equalsIgnoreCase(target.getName())) {
                        try {
                            PreparedStatement statement = DatabaseSetup.getConnection()
                                    .prepareStatement("DELETE FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
                            statement.setString(1, target.getUniqueId().toString());
                            statement.execute();
                            player.sendMessage(ChatColor.DARK_AQUA + Messages.get("player.player") + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA +  Messages.get("unban.command.player.message1"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        }
        } catch (IllegalArgumentException |ArrayIndexOutOfBoundsException | CommandException e) {
                player.sendMessage(ChatColor.DARK_RED + Messages.get("unban.command.error"));
        }
        return true;
    }
}
