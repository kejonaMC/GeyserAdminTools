package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UnbanCommand {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        List<String> names = new ArrayList<>();
        String query = "SELECT * FROM " + BanDatabaseSetup.Bantable;
        try (Statement stmt = BanDatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                names.add(rs.getString("Username"));
            }
            rs.close();
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("gban") && player.hasPermission("geyseradmintools.gban")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer();
            if (target == null) {
                player.sendMessage(ChatColor.RED + "[GeyserAdminTools] Could not find player!");
                player.sendMessage(ChatColor.RED + "[GeyserAdminTools] Perhaps wrong usage ? /gban <username> <amount of days> <reason>");
                return true;
            }
            try {
                PreparedStatement statement = BanDatabaseSetup.getConnection()
                        .prepareStatement("DELETE FROM " + BanDatabaseSetup.Bantable + " WHERE UUID=?");
                statement.setString(1, target.getUniqueId().toString());
                statement.execute();

            } catch (SQLException exe) {
                exe.printStackTrace();
            }
            Gat.logger.info("Player " + player.getName() + " has banned " + target.getName() + " till: " + time + " for reason: " + reason);
            //end
        }
        return true;
    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
