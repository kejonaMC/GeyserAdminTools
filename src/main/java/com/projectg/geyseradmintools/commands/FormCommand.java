package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.forms.MainForm;
import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FormCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("geyseradmintools.gadmin")) {
                return true;
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
                sender.sendMessage(ChatColor.YELLOW + "[GeyserAdminTool] Sorry, this command only works for bedrock players!");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Gat.plugin.getLogger().info("This command only works in-game!");
        }
        return true;
    }
}