package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.forms.MainForm;
import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import com.projectg.geyseradmintools.utils.ItemStackFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToolCommand implements CommandExecutor {
    private static final ItemStack starTool = ItemStackFactory.getStarTool();

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
                    player.getInventory().addItem(starTool);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + Messages.get("bedrock.command"));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Gat.plugin.getLogger().info(Messages.get("permission.command.error"));
        }
        return true;
    }
}