package com.alysaa.geyseradmintools.gui;

import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.ReportDatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportPlayer {
    public static void openReportMenu(Player player){

        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT * FROM " + ReportDatabaseSetup.Reporttable;
        try (Statement stmt = BanDatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("USERNAME"));
            }
            Inventory reportgui = Bukkit.createInventory(player, 45, ChatColor.BLUE + "Report List");
            for (Player value : convert(list)) {
                ItemStack ticket = new ItemStack(Material.PAPER, 1);
                ItemMeta meta = ticket.getItemMeta();
                meta.setDisplayName(value.getName());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.AQUA +"has reported player: " +ChatColor.GOLD + value.getName());
                lore.add(ChatColor.AQUA + "Click for more information " );
                meta.setLore(lore);
                ticket.setItemMeta(meta);
                reportgui.addItem(ticket);
            }
            player.openInventory(reportgui);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void openPlayerMenu(Player player1, Player whoToReport){

        Inventory reportPlayerMenu = Bukkit.createInventory(player1, 9, "Player Info");

        ItemStack report = new ItemStack(Material.BOOK, 1);
        ItemMeta report_meta = report.getItemMeta();
        report_meta.setDisplayName(whoToReport.getName());
        report.setItemMeta(report_meta);
        reportPlayerMenu.setItem(0, report);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Click for more information about the player " );
        report_meta.setLore(lore);
        report.setItemMeta(report_meta);

        ItemStack ticket = new ItemStack(Material.PAPER, 1);
        ItemMeta player_meta = ticket.getItemMeta();
        player_meta.setDisplayName(whoToReport.getPlayer().getDisplayName());
        ticket.setItemMeta(player_meta);
        reportPlayerMenu.setItem(4, ticket);

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta cancel_meta = cancel.getItemMeta();
        cancel_meta.setDisplayName(ChatColor.RED + "Go Back!");
        cancel.setItemMeta(cancel_meta);
        reportPlayerMenu.setItem(8, cancel);

        player1.openInventory(reportPlayerMenu);
    }
    public static List<Player> convert(ArrayList<String> list)
    {
        return list.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

}