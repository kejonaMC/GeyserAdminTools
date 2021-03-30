package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.Forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GatCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("gadmin") && player.hasPermission("geyseradmintools.gadmin")) {
                try {
                    MainForm.formList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Gat.plugin.getLogger().info("This command only works in-game!");
        }
        return false;
    }
}