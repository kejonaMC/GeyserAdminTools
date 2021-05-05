package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.forms.MainForm;
import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import com.projectg.geyseradmintools.utils.ItemStackFactory;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;


public class AdminToolInventory  implements Listener {

    private static final FileConfiguration config = Gat.plugin.getConfig();
    private static final ItemStack starTool = ItemStackFactory.getStarTool();
    PreparedStatement reportstatement;
    {
        try {
            reportstatement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.Reporttable + " WHERE UUID=?");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    PreparedStatement banstatement;
    {
        try {
            banstatement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.Bantable + " WHERE UUID=?");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        UUID uuid = e.getWhoClicked().getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            if (config.getBoolean("DisableItemMove")) {
                if (Objects.requireNonNull(e.getCurrentItem()).equals(starTool)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("geyseradmintools.item")) {
            if (player.getInventory().getItemInMainHand().equals(starTool) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                MainForm.formList(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (config.getBoolean("DisableItemDrop")) {
            if (e.getItemDrop().getItemStack().equals(starTool)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onreportMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        try {
        OfflinePlayer whoToReport = Bukkit.getOfflinePlayer((UUID.fromString(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Gat.getPlugin(), "reporteduuid"), PersistentDataType.STRING)))));


            if (e.getView().getTitle().equalsIgnoreCase("View Report Tickets")) {
                if (e.getCurrentItem().getType() == Material.PAPER) {
                    //ReportPlayer.openPlayerMenu(player, whoToReport);
                    reportstatement.setString(1, whoToReport.getUniqueId().toString());
                    reportstatement.execute();
                    reportstatement.close();
                    player.sendMessage(ChatColor.DARK_AQUA + "[GeyserAdminTools] Reports from " + ChatColor.AQUA + whoToReport.getName() + ChatColor.DARK_AQUA +" has been deleted!");
                    e.setCancelled(true);
                    player.closeInventory();
                }
        }
            } catch (SQLException | NullPointerException exception) {
                exception.getSuppressed();
            }
    }
    @EventHandler
    public void onbanMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        try {
            OfflinePlayer whoToReport = Bukkit.getOfflinePlayer((UUID.fromString(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Gat.getPlugin(), "banuuid"), PersistentDataType.STRING)))));


            if (e.getView().getTitle().equalsIgnoreCase("View Banned Players")) {
                if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    //ReportPlayer.openPlayerMenu(player, whoToReport);
                    banstatement.setString(1, whoToReport.getUniqueId().toString());
                    banstatement.execute();
                    banstatement.close();
                    player.sendMessage(ChatColor.DARK_AQUA + "[GeyserAdminTools] Player: " + ChatColor.AQUA + whoToReport.getName() + ChatColor.DARK_AQUA +" has been unbanned");
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        } catch (SQLException | NullPointerException exception) {
            exception.getSuppressed();
        }
    }
}

