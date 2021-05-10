package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.language.Messages;
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

public class UnmuteCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Messages.get("permission.command.error"));
            return true;
        }
        Player player = (Player) sender;
        try {
        if (cmd.getName().equalsIgnoreCase("gunmute") && player.hasPermission("geyseradmintools.gunmute")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.DARK_RED + Messages.get("unmute.command.error"));
                return true;
            }
            try {
                            PreparedStatement statement = DatabaseSetup.getConnection()
                                    .prepareStatement("DELETE FROM " + DatabaseSetup.Mutetable + " WHERE UUID=?");
                            statement.setString(1, target.getUniqueId().toString());
                            statement.execute();
                            player.sendMessage(ChatColor.DARK_AQUA + Messages.get("player.player") + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + Messages.get("unmute.command.player.message1"));
                            target.sendMessage(ChatColor.GOLD + Messages.get("unmute.command.player.message2"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
        }
    } catch (IllegalArgumentException |ArrayIndexOutOfBoundsException | CommandException e) {
        player.sendMessage(ChatColor.DARK_RED + Messages.get("unmute.command.error"));
    }
        return true;
    }
}
