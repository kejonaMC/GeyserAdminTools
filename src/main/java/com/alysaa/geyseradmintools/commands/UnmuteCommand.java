package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnmuteCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("gunmute") && player.hasPermission("geyseradmintools.gunmute")) {
            Player target = Bukkit.getPlayer(args[0]);
                        try {
                            PreparedStatement statement = BanDatabaseSetup.getConnection()
                                    .prepareStatement("DELETE FROM " + MuteDatabaseSetup.Mutetable + " WHERE UUID=?");
                            statement.setString(1, target.getUniqueId().toString());
                            statement.execute();
                            player.sendMessage("[GeyserAdminTools] Player " + target.getName() + " is unmuted");
                            Gat.logger.info("Player " + player.getName() + " has unmuted " + target.getName());
                            target.sendMessage("Your mute has been lifted by an admin!");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
        }
        return false;
    }
}

