package com.alysaa.geyseradmintools.forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.DatabaseSetup;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportForm {
    public static void reportList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    SimpleForm.builder()
                            .title("Report Tool")
                            .button("Report player")
                            .button("View reports")
                            .button("Delete reports")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    if (player.hasPermission("geyseradmintools.reportplayer")) {
                                        reportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to Use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                        viewReportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to Use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 2) {
                                    if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                        deleteReportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to Use this button!");
                                    }
                                }
                            }));
        }
    }

    private static void deleteReportPlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            String query = "SELECT * FROM " + DatabaseSetup.Reporttable;
            try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    names.add(rs.getString("USERNAME"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title("View reports")
                                    .dropdown("Select Player to view info", playerlist)
                                    .responseHandler((form, responseData) -> {
                                        CustomFormResponse response = form.parseResponse(responseData);
                                        if (!response.isCorrect()) {
                                            return;
                                        }
                                        try {
                                        int clickedIndex = response.getDropdown(0);
                                        String name = names.get(clickedIndex);
                                        Player player1 = Bukkit.getPlayer(name);
                                        //MySQL code
                                            PreparedStatement statement = DatabaseSetup.getConnection()
                                                    .prepareStatement("DELETE FROM " + DatabaseSetup.Reporttable + " WHERE UUID=?");
                                            statement.setString(1, player1.getUniqueId().toString());
                                            statement.execute();
                                            player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Reports from player "+player1.getName() +" has been deleted!");
                                        } catch (SQLException | IndexOutOfBoundsException e) {
                                            player.sendMessage(ChatColor.YELLOW + "[GeyserAdminTools] wrong usage !");
                                        }
                                    }));
                }
            } catch (SQLException | IndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.YELLOW + "[GeyserAdminTools] wrong usage !");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void viewReportPlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            String query = "SELECT * FROM " + DatabaseSetup.Reporttable;
            try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    names.add(rs.getString("USERNAME"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title("View reports")
                                    .dropdown("Select Player to view info", playerlist)
                                    .responseHandler((form, responseData) -> {
                                        CustomFormResponse response = form.parseResponse(responseData);
                                        if (!response.isCorrect()) {
                                            return;
                                        }
                                        int clickedIndex = response.getDropdown(0);
                                        String name = names.get(clickedIndex);
                                        Player player1 = Bukkit.getPlayer(name);
                                        //MySQL code
                                        try {
                                            PreparedStatement statement = DatabaseSetup.getConnection()
                                                    .prepareStatement("SELECT * FROM " + DatabaseSetup.Reporttable + " WHERE UUID=?");
                                            statement.setString(1, player1.getUniqueId().toString());
                                            ResultSet results = statement.executeQuery();
                                            while (results.next()) {
                                                String report = results.getString("REPORT");
                                                String username = results.getString("USERNAME");
                                                player.sendMessage(ChatColor.GREEN + "Player " + username + " has been reported for: " + report);
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }

                                    }));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void reportPlayers(Player player) {
        //done
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            String[] playerlist = names.toArray(new String[0]);
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Report tool")
                                .dropdown("Select Player", playerlist)
                                .input("Report Reason")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    int clickedIndex = response.getDropdown(0);
                                    String report = response.getInput(1);
                                    String name = names.get(clickedIndex);
                                    Player player1 = Bukkit.getPlayer(name);
                                    //database code
                                    try {
                                        String sql = "(UUID,REPORT,USERNAME) VALUES (?,?,?)";
                                        PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Reporttable
                                                + sql);
                                        insert.setString(1, player1.getUniqueId().toString());
                                        insert.setString(2, report);
                                        insert.setString(3, name);
                                        insert.executeUpdate();
                                        // Player inserted now
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                    Gat.logger.info("New report from " + player.getName() + " has been received ");
                                    player.sendMessage(ChatColor.GOLD + "[GeyserAdminTools] Player " + name + " has been reported");
                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
                                        if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                        onlinePlayers.sendMessage(ChatColor.AQUA +"New report from " + player.getName() + " has been received ");
                                        }
                                    }
                                    //end
                                }));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
