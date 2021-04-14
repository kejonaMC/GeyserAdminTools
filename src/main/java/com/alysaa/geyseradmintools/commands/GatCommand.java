package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GatCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("geyseradmintools.gadmin")) {
                return false;
            }

            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                try {
                    MainForm.formList(player);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[GeyserAdminTool] Sorry, this command only works for bedrock players!");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Gat.plugin.getLogger().info("This command only works in-game!");
        }
        return false;
    }
}