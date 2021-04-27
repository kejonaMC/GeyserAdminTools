package com.alysaa.geyseradmintools.commands;

import com.alysaa.geyseradmintools.gui.ReportPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            ReportPlayer.openReportMenu(player);

        }


        return true;
    }
}