package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import com.alysaa.geyseradmintools.database.ReportDatabaseSetup;
import com.alysaa.geyseradmintools.forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.gui.ReportPlayer;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import com.alysaa.geyseradmintools.utils.ItemStackFactory;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;


public class AdminToolInventory  implements Listener {

    private static final FileConfiguration config = Gat.plugin.getConfig();
    private static final ItemStack starTool = ItemStackFactory.getStarTool();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        UUID uuid = e.getWhoClicked().getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
        if (config.getBoolean("DisableItemMove")) {
            if (e.getCurrentItem().equals(starTool)) {
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
        public void onMenuClick(InventoryClickEvent e){
            Player player = (Player) e.getWhoClicked();
            Player whoToReport = Gat.plugin.getServer().getPlayer((e.getCurrentItem().getItemMeta().getDisplayName()));
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Report List")){
                if (e.getCurrentItem().getType() == Material.PAPER){
                    //ReportPlayer.openPlayerMenu(player, whoToReport);
                    try {
                        PreparedStatement statement = ReportDatabaseSetup.getConnection()
                                .prepareStatement("DELETE FROM " + ReportDatabaseSetup.Reporttable + " WHERE UUID=?");
                        statement.setString(1, whoToReport.getUniqueId().toString());
                        statement.execute();
                        player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Reports from player " + whoToReport.getName() + " has been deleted!");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }else if(e.getView().getTitle().equalsIgnoreCase("Player Info")){
                switch(e.getCurrentItem().getType()){
                    case BARRIER:
                         ReportPlayer.openReportMenu(player);
                        break;
                    case BOOK:
                        player.sendMessage(ChatColor.AQUA + "#-------------------------------------------------#");
                        player.sendMessage(ChatColor.AQUA + "   PlayerName: " + ChatColor.WHITE + whoToReport.getName());
                        player.sendMessage(ChatColor.AQUA + "   PlayerUUID: " + ChatColor.WHITE + whoToReport.getUniqueId());
                        player.sendMessage(ChatColor.AQUA + "   HomeLocation: " + ChatColor.WHITE + whoToReport.getBedSpawnLocation());
                        player.sendMessage(ChatColor.AQUA + "   PlayerLevel: " + ChatColor.WHITE + whoToReport.getLevel());
                        player.sendMessage(ChatColor.AQUA + "#-------------------------------------------------#");

                        break;
                    case ANVIL:
                        try {
                        String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                        PreparedStatement insert = BanDatabaseSetup.getConnection().prepareStatement("INSERT INTO " + BanDatabaseSetup.Bantable
                                + sql);
                        insert.setString(1, whoToReport.getUniqueId().toString());
                        insert.setString(2, "Temp ban from ticket");
                        insert.setString(3, whoToReport.getName());
                        insert.setString(4, LocalDate.now().plusDays(1).toString());
                        insert.executeUpdate();
                            whoToReport.kickPlayer("You where banned for: " + ChatColor.DARK_RED + "Temp ban from report.");
                            player.sendMessage("[GeyserAdminTools] " +ChatColor.AQUA + "Player " + whoToReport.getName() + " is banned");
                            Gat.logger.info("Player " + player.getName() + " has banned " + whoToReport.getName() + " till tomorrow " + " for reason: temp ban from report");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;

                    case NOTE_BLOCK:
                        try {
                            String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                            PreparedStatement insert = MuteDatabaseSetup.getConnection().prepareStatement("INSERT INTO " + MuteDatabaseSetup.Mutetable
                                    + sql);
                            insert.setString(1, whoToReport.getUniqueId().toString());
                            insert.setString(2, "Temp mute from ticket");
                            insert.setString(3, whoToReport.getName());
                            insert.setString(4, LocalDate.now().plusDays(1).toString());
                            insert.executeUpdate();
                            whoToReport.sendMessage("You where muted for: " + ChatColor.DARK_RED + "Temp mute from report.");
                            player.sendMessage("[GeyserAdminTools] " +ChatColor.AQUA + "Player " + whoToReport.getName() + " is muted");
                            Gat.logger.info("Player " + player.getName() + " has muted " + whoToReport.getName() + " till tomorrow " + " for reason: temp mute from report");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                }
            }
            e.setCancelled(true);
        }

    }
