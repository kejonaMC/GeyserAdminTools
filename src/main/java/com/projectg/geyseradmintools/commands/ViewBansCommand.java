package com.projectg.geyseradmintools.commands;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.gui.menu.BanMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ViewBansCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use this command");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("gviewbans") && player.hasPermission("geyseradmintools.viewbans")) {

            new BanMenu(Gat.getPlayerMenuUtility(player)).open();

        }


        return true;
    }
}
