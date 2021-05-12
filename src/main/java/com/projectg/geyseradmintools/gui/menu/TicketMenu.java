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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class TicketMenu extends PaginatedMenu {

    private List<ItemStack> reportedHeads = null;

    public TicketMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "View Report Tickets";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {
            player.closeInventory();

        } else if (e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
            if (ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Left")) {
                if (pageIndex != 0) {
                    super.open(pageIndex - 1);
                }
            } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                int currentCapacity = (pageIndex + 1) * getMaxItemsPerPage();
                int totalHeads = reportedHeads.size();

                if (totalHeads > currentCapacity) {
                    // If the total count doesn't exceed the next page, then the next page is the last.
                    lastPage = totalHeads <= currentCapacity + getMaxItemsPerPage();
                    super.open(pageIndex + 1);
                }
            }
        }
    }

    @Override
    public void setMenuItems(int pageIndex) {
        // Generate all content if it hasn't yet, and recall open()
        if (reportedHeads == null) {
            generateReportList(true);
            return;
        }
        // Clear any existing heads
        removeContents();

        int fromIndex = pageIndex * getMaxItemsPerPage(); // Inclusive
        // toIndex is the size if the size is smaller than the combined capacity of the current and past pages.
        // If the size is not smaller, then the toIndex is the combined capacity of the current and past pages, which truncates the list if necessary.
        int toIndex = Math.min(reportedHeads.size(), fromIndex + getMaxItemsPerPage()); //Exclusive
        for (ItemStack head : reportedHeads.subList(fromIndex,toIndex)) {
            inventory.addItem(head);
        }
    }

    /**
     * Generate the report list {@link this#reportedHeads}
     * @param openMenu Enable in order to open the menu to the first page once the list has been generated.
     */
    public void generateReportList(boolean openMenu) {

        // set the list to be non null
        reportedHeads = new ArrayList<>(0);
        List<UUID> allUUIDS = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId).collect(Collectors.toCollection(ArrayList::new));

        // Run the database query async
        new BukkitRunnable() {
            @Override
            public void run() {

                Map<ResultSet, UUID> results = new HashMap<>();
                for (UUID uuid : allUUIDS) {
                    try {
                        PreparedStatement statement = DatabaseSetup.getConnection()
                                .prepareStatement("SELECT * FROM " + DatabaseSetup.reportTable + " WHERE UUID=?");
                        statement.setString(1, uuid.toString());
                        ResultSet rst = statement.executeQuery();
                        results.put(rst, uuid);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // resume everything non thread safe
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        for (ResultSet rst : results.keySet()) {
                            try {
                                if (rst.next()) {
                                    UUID uuid = results.get(rst);
                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                                    String report = rst.getString("REPORT");
                                    String reporting = rst.getString("REPORTING");
                                    String date = rst.getString("DATE");
                                    ItemStack ticket = new ItemStack(Material.PAPER, 1);
                                    ItemMeta meta = ticket.getItemMeta();
                                    if (meta != null) {
                                        meta.setDisplayName(Messages.get("report.gui.text1"));
                                        ArrayList<String> lore = new ArrayList<>();
                                        meta.getPersistentDataContainer().set(new NamespacedKey(Gat.getPlugin(), "reporteduuid"), PersistentDataType.STRING, uuid.toString());
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text2", reporting));
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text3", offlinePlayer.getName()));
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text4", report));
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("report.gui.text5", date));
                                        lore.add(ChatColor.WHITE + Messages.get("report.gui.text6"));
                                        meta.setLore(lore);
                                        ticket.setItemMeta(meta);
                                        reportedHeads.add(ticket);
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (openMenu) {
                            open(0);
                        }
                    }
                }.runTask(Gat.getPlugin());
            }
        }.runTaskAsynchronously(Gat.getPlugin());
    }
}