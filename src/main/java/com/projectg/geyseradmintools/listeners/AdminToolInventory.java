package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.database.BanData;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.database.ReportData;
import com.projectg.geyseradmintools.forms.MainForm;
import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.language.Messages;
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
    public void onReportMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        try {
            OfflinePlayer whoToReport = Bukkit.getOfflinePlayer((UUID.fromString(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Gat.getPlugin(), "reporteduuid"), PersistentDataType.STRING)))));

            if (e.getView().getTitle().equalsIgnoreCase("View Report Tickets")) {
                if (e.getCurrentItem().getType() == Material.PAPER) {
                    //ReportPlayer.openPlayerMenu(player, whoToReport);
                    ReportData.deleteReport(whoToReport.getUniqueId());
                    player.sendMessage(ChatColor.DARK_AQUA + Messages.get("remove.ticket.event", whoToReport.getName()));
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        } catch (NullPointerException exception) {
            exception.getSuppressed();
        }
    }

    @EventHandler
    public void onBanMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        try {
            OfflinePlayer whoToReport = Bukkit.getOfflinePlayer((UUID.fromString(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Gat.getPlugin(), "banuuid"), PersistentDataType.STRING)))));


            if (e.getView().getTitle().equalsIgnoreCase("View Banned Players")) {
                if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    //ReportPlayer.openPlayerMenu(player, whoToReport);
                    BanData.deleteBan(whoToReport.getUniqueId());
                    player.sendMessage(ChatColor.DARK_AQUA + Messages.get("unban.join.event", whoToReport.getName()));
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        } catch (NullPointerException exception) {
            exception.getSuppressed();
        }
    }
}

