package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BanCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("gban" + (args[0]) + (args[2]) + (args[1])) && player.hasPermission("geyseradmintools.gban")) {
                String playername = args[0];
                String reason = args[1];
                String time = args[2];
                Player p = Bukkit.getPlayer(playername);
                try {
                    String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                    PreparedStatement insert = BanDatabaseSetup.getConnection().prepareStatement("INSERT INTO " + BanDatabaseSetup.Bantable
                            + sql);
                    insert.setString(1, p.getUniqueId().toString());
                    insert.setString(2, reason);
                    insert.setString(3, playername);
                    insert.setString(4, time);
                    insert.executeUpdate();
                    // Player inserted now
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                p.kickPlayer("you where banned for: " + reason);
                Gat.logger.info("Player " + player.getName() + " has banned " + p.getName() + " till: " + time + " for reason: " + reason);
                //end
            }
        }
        return true;
    }
}
