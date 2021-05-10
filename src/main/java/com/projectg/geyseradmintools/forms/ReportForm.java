package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
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
import java.time.LocalDate;
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
                            .title(ChatColor.DARK_AQUA + Messages.get("main.report.form.title"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.report.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.report.form.button2"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.report.form.button3"))
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
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.viewreports")) {
                                        viewReportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                                if (response.getClickedButtonId() == 2) {
                                    if (player.hasPermission("geyseradmintools.viewreports")) {
                                        deleteReportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
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
                    names.add(rs.getString("REPORTED"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title(ChatColor.DARK_AQUA + Messages.get("delete.report.form.title"))
                                    .dropdown(ChatColor.DARK_AQUA + Messages.get("delete.report.form.dropdown"), playerlist)
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
                                            assert player1 != null;
                                            statement.setString(1, player1.getUniqueId().toString());
                                            statement.execute();
                                            player.sendMessage(ChatColor.GREEN + Messages.get("delete.report.form.player.message1") + player1.getName()
                                            );
                                        } catch (SQLException | IndexOutOfBoundsException e) {
                                            player.sendMessage(ChatColor.YELLOW + Messages.get("report.input.error"));
                                        }
                                    }));
                }
            } catch (SQLException | IndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.YELLOW + Messages.get("report.input.error"));
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
                    names.add(rs.getString("REPORTED"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title(ChatColor.DARK_AQUA + Messages.get("view.report.form.title"))
                                    .dropdown(ChatColor.DARK_AQUA + Messages.get("view.report.form.dropdown"), playerlist)
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
                                            assert player1 != null;
                                            statement.setString(1, player1.getUniqueId().toString());
                                            ResultSet results = statement.executeQuery();
                                            while (results.next()) {
                                                String report = results.getString("REPORT");
                                                String username = results.getString("REPORTED");
                                                String reporting = results.getString("REPORTING");
                                                String date = results.getString("DATE");
                                                player.sendMessage(ChatColor.AQUA + "#--------------------------------------------------#");
                                                player.sendMessage(ChatColor.DARK_AQUA + reporting + ChatColor.AQUA + Messages.get("view.report.form.player.message1") + ChatColor.DARK_AQUA+ username);
                                                player.sendMessage(ChatColor.AQUA + Messages.get("view.report.form.player.message2") + ChatColor.DARK_AQUA + report);
                                                player.sendMessage(ChatColor.AQUA + Messages.get("view.report.form.player.message3") + ChatColor.DARK_AQUA + date);
                                                player.sendMessage(ChatColor.AQUA + "#--------------------------------------------------#");
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
                                .title(Messages.get("report.report.form.title"))
                                .dropdown(Messages.get("report.report.form.dropdown"), playerlist)
                                .input(Messages.get("report.report.form.input"))
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
                                        String sql = "(UUID,REPORT,REPORTED,REPORTING,DATE) VALUES (?,?,?,?,?)";
                                        PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Reporttable
                                                + sql);
                                        assert player1 != null;
                                        insert.setString(1, player1.getUniqueId().toString());
                                        insert.setString(2, report);
                                        insert.setString(3, name);
                                        insert.setString(4, player.getName());
                                        insert.setString(5, LocalDate.now().toString());
                                        insert.executeUpdate();
                                        // Player inserted now
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                    player.sendMessage(ChatColor.GOLD + "[GeyserAdminTools] " + name + Messages.get("report.report.form.player.message1"));
                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
                                        if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                        onlinePlayers.sendMessage(ChatColor.AQUA + Messages.get("report.report.form.player.message2") + player.getName());
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
