package com.projectg.geyseradmintools.gui.menu;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.gui.PaginatedMenu;
import com.projectg.geyseradmintools.gui.PlayerMenuUtility;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BanMenu extends PaginatedMenu {

    private List<ItemStack> bannedHeads = null;

    public BanMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "View Banned Players";
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
                int totalHeads = bannedHeads.size();

                if (totalHeads > currentCapacity) {
                    // If the total count doesn't exceed the next page, then the next page is the last.
                    lastPage = totalHeads < currentCapacity + getMaxItemsPerPage();
                    super.open(pageIndex + 1);
                }
            }
        }
    }

    @Override
    public void setMenuItems(int pageIndex) {
        // Generate all content if it hasn't yet, and recall open()
        if (bannedHeads == null) {
            generateBanList(true);
            return;
        }

        // Clear any existing heads
        removeContents();

        // Since indexes start at 0 and not 1, and the start index is inclusive, the start index of this page is conveniently the amount of items that can fix it earlier pages.
        int startIndex = pageIndex * getMaxItemsPerPage();
        // The end index is exclusive, so it is simply the start index of the next page
        List<ItemStack> displayedHeads = bannedHeads.subList(startIndex, startIndex + getMaxItemsPerPage());
        inventory.addItem(displayedHeads.toArray(new ItemStack[0]));
    }

    /**
     * Generate the ban list {@link this#bannedHeads}
     * @param openMenu Enable in order to open the menu to the first page once the list has been generated.
     */
    public void generateBanList(boolean openMenu) {

        // set the list to be non null
        bannedHeads = new ArrayList<>(0);
        List<UUID> allUUIDS = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId).collect(Collectors.toCollection(ArrayList::new));

        // Run the database query async
        new BukkitRunnable() {
            @Override
            public void run() {

                Map<ResultSet, UUID> results = new HashMap<>();
                for (UUID uuid : allUUIDS) {
                    try {
                        PreparedStatement statement = DatabaseSetup.getConnection()
                                .prepareStatement("SELECT * FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
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

                                    String report = rst.getString("REASON");
                                    String date = rst.getString("ENDDATE");
                                    ItemStack banned = new ItemStack(Material.PLAYER_HEAD, 1);
                                    SkullMeta sm = (SkullMeta) banned.getItemMeta();
                                    if (sm != null) {
                                        sm.setOwningPlayer(offlinePlayer);
                                        sm.setDisplayName(Messages.get("ban.gui.text1"));
                                        ArrayList<String> lore = new ArrayList<>();
                                        sm.getPersistentDataContainer().set(new NamespacedKey(Gat.getPlugin(), "banuuid"), PersistentDataType.STRING, uuid.toString());
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("ban.gui.text2", offlinePlayer.getName()));
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("ban.gui.text3", report));
                                        lore.add(ChatColor.DARK_AQUA + Messages.get("ban.gui.text4", date));
                                        lore.add(ChatColor.WHITE + Messages.get("ban.gui.text5"));
                                        sm.setLore(lore);
                                        banned.setItemMeta(sm);
                                        bannedHeads.add(banned);
                                    }
                                }
                            } catch (SQLException e ) {
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
