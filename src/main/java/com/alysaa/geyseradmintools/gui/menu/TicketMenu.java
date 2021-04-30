package com.alysaa.geyseradmintools.gui.menu;

import com.alysaa.geyseradmintools.database.DatabaseSetup;
import com.alysaa.geyseradmintools.gui.PaginatedMenu;
import com.alysaa.geyseradmintools.gui.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
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

public class TicketMenu extends PaginatedMenu {

    public TicketMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "View Report Tickets";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ArrayList<String> list = new ArrayList<>();

        Player p = (Player) e.getWhoClicked();
        String query = "SELECT * FROM " + DatabaseSetup.Reporttable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("USERNAME"));
            }

            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {

                p.closeInventory();

            } else if (e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
                if (ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Left")) {
                    if (page == 0) {
                        p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                    } else {
                        page = page - 1;
                        super.open();
                    }
                } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                    if (!((index + 1) >= list.size())) {
                        page = page + 1;
                        super.open();
                    } else {
                        p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setMenuItems() {

        addMenuBorder();

        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseSetup.Reporttable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("USERNAME"));
            }

            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= list.size()) break;
                if (list.get(index) != null) {

                    for (Player value : convert(list)) {
                        PreparedStatement statement = DatabaseSetup.getConnection()
                                .prepareStatement("SELECT * FROM " + DatabaseSetup.Reporttable + " WHERE UUID=?");
                        statement.setString(1, value.getUniqueId().toString());
                        ResultSet rst = statement.executeQuery();

                        if (rst.next()) {
                            String report = rst.getString("REPORT");
                            ItemStack ticket = new ItemStack(Material.PAPER, 1);
                            ItemMeta meta = ticket.getItemMeta();
                            assert meta != null;
                            meta.setDisplayName(value.getName());
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.DARK_AQUA + "Offender: " + ChatColor.AQUA + value.getName());
                            lore.add(ChatColor.DARK_AQUA + "Reason: " + ChatColor.AQUA + report);
                            lore.add(ChatColor.GRAY + "Click on the ticket to remove it");
                            meta.setLore(lore);
                            ticket.setItemMeta(meta);
                            inventory.addItem(ticket);
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    public static List<Player> convert(ArrayList<String> list)
    {
        return list.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }
}