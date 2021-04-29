package com.alysaa.geyseradmintools.gui;

import com.alysaa.geyseradmintools.database.DatabaseSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReportPlayer {
    public static void openReportMenu(Player player) {

        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseSetup.Reporttable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("USERNAME"));
            }
            Inventory reportgui = Bukkit.createInventory(player, 45, ChatColor.BLUE + "Report List");
            for (Player value : convert(list)) {
                PreparedStatement statement = DatabaseSetup.getConnection()
                        .prepareStatement("SELECT * FROM " + DatabaseSetup.Reporttable + " WHERE UUID=?");
                statement.setString(1, value.getUniqueId().toString());
                ResultSet rst = statement.executeQuery();

                if (rst.next()) {
                    String report = rst.getString("REPORT");
                    ItemStack ticket = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = ticket.getItemMeta();
                    meta.setDisplayName(value.getName());
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.DARK_AQUA + "Reporter: " + ChatColor.AQUA + player.getName());
                    lore.add(ChatColor.DARK_AQUA + "Offender: " + ChatColor.AQUA + value.getName());
                    lore.add(ChatColor.DARK_AQUA + "Reason: " + ChatColor.AQUA + report);
                    lore.add(ChatColor.GRAY + "Click on the ticket to remove it");
                    meta.setLore(lore);
                    ticket.setItemMeta(meta);
                    reportgui.addItem(ticket);
                }
                player.openInventory(reportgui);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static List<Player> convert(ArrayList<String> list)
    {
        return list.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

}