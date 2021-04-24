package com.alysaa.geyseradmintools.javamenu.menus;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.javamenu.PaginatedMenu;
import com.alysaa.geyseradmintools.javamenu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ReportMenu extends PaginatedMenu {


        public ReportMenu(PlayerMenuUtility playerMenuUtility) {
                super(playerMenuUtility);
        }

        @Override
        public String getMenuName() {
                return "Choose a a report";
        }

        @Override
        public int getSlots() {
                return 54;
        }

        @Override
        public void handleMenu(InventoryClickEvent e) {
                Player p = (Player) e.getWhoClicked();

                ArrayList<Player> players = new ArrayList<Player>(getServer().getOnlinePlayers());

                if (e.getCurrentItem().getType().equals(Material.PAPER)) {

                        PlayerMenuUtility playerMenuUtility = Gat.getPlayerMenuUtility(p);
                        playerMenuUtility.SetReport(Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Gat.getPlugin(), "uuid"), PersistentDataType.STRING))));

                        //query and add player reports to the lore

                } else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {

                        p.closeInventory();

                } else if (e.getCurrentItem().getType().equals(Material.WOOD_BUTTON)) {
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                                if (page == 0) {
                                        p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                                } else {
                                        page = page - 1;
                                        super.open();
                                }
                        } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                                if (!((index + 1) >= players.size())) {
                                        page = page + 1;
                                        super.open();
                                } else {
                                        p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                                }
                        }
                }
        }

        @Override
        public void setMenuItems() {

                addMenuBorder();

                //The thing you will be looping through to place items
                ArrayList<Player> players = new ArrayList<Player>(getServer().getOnlinePlayers());

                if (players != null && !players.isEmpty()) {
                        for (int i = 0; i < getMaxItemsPerPage(); i++) {
                                index = getMaxItemsPerPage() * page + i;
                                if (index >= players.size()) break;
                                if (players.get(index) != null) {

                                        ItemStack playerItem = new ItemStack(Material.PAPER, 1);
                                        ItemMeta playerMeta = playerItem.getItemMeta();
                                        playerMeta.setDisplayName(ChatColor.GREEN + players.get(index).getDisplayName());

                                        playerMeta.getPersistentDataContainer().set(new NamespacedKey(Gat.getPlugin(), "uuid"), PersistentDataType.STRING, players.get(index).getUniqueId().toString());
                                        playerItem.setItemMeta(playerMeta);

                                        inventory.addItem(playerItem);
                                }
                        }
                }
        }
}
