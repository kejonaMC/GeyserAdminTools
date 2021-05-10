package com.projectg.geyseradmintools.gui.menu;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.gui.PaginatedMenu;
import com.projectg.geyseradmintools.gui.PlayerMenuUtility;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
        String query = "SELECT * FROM " + DatabaseSetup.reportTable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("REPORTED"));
            }

            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {

                p.closeInventory();

            } else if (e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
                if (ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Left")) {
                    if (page == 0) {
                        p.sendMessage(ChatColor.GRAY + Messages.get("gui.player.message1"));
                    } else {
                        page = page - 1;
                        super.open();
                    }
                } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                    if (!((index + 1) >= list.size())) {
                        page = page + 1;
                        super.open();
                    } else {
                        p.sendMessage(ChatColor.GRAY + Messages.get("gui.player.message2"));
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setMenuItems() {
        Runnable runnable = () -> {
        addMenuBorder();

        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseSetup.reportTable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("REPORTED"));
            }

            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= list.size()) break;
                if (list.get(index) != null) {


                    for (@NotNull OfflinePlayer op : Bukkit.getServer().getOfflinePlayers()) {
                        PreparedStatement statement = DatabaseSetup.getConnection()
                                .prepareStatement("SELECT * FROM " + DatabaseSetup.reportTable + " WHERE UUID=?");
                        statement.setString(1, op.getUniqueId().toString());
                        ResultSet rst = statement.executeQuery();

                        if (rst.next()) {
                            String report = rst.getString("REPORT");
                            String reporting = rst.getString("REPORTING");
                            String date = rst.getString("DATE");
                            ItemStack ticket = new ItemStack(Material.PAPER, 1);
                            ItemMeta meta = ticket.getItemMeta();
                            assert meta != null;
                            meta.setDisplayName(Messages.get("report.gui.text1"));
                            ArrayList<String> lore = new ArrayList<>();
                            meta.getPersistentDataContainer().set(new NamespacedKey(Gat.getPlugin(), "reporteduuid"), PersistentDataType.STRING, op.getUniqueId().toString());
                            lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text2") + ChatColor.AQUA + reporting );
                            lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text3") + ChatColor.AQUA + op.getName());
                            lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text4") + ChatColor.AQUA + report);
                            lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text5") + ChatColor.AQUA + date);
                            lore.add(ChatColor.WHITE + Messages.get("report.gui.text6"));
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
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}