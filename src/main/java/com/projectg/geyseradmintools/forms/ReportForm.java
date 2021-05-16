package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.database.BanData;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.database.ReportData;
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
                                } else if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.viewreports")) {
                                        viewReportPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                } else if (response.getClickedButtonId() == 2) {
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
            ReportData.reportList(names);
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
                                            Player rPlayer = Bukkit.getPlayer(name);
                                            //MySQL code
                                            ReportData.deleteReport(rPlayer.getUniqueId());
                                            player.sendMessage(ChatColor.GREEN + Messages.get("delete.report.form.player.message1", rPlayer.getName()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }));
                }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void viewReportPlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            ReportData.reportList(names);
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
                                        String startDate = ReportData.infoReport(uuid, "STARTDATE");
                                        String report = ReportData.infoReport(uuid, "REPORT");

                                        player.sendMessage(ChatColor.AQUA + "#--------------------------------------------------#");
                                        assert player1 != null;
                                        player.sendMessage(ChatColor.AQUA + Messages.get("view.report.form.player.message1",player.getName(),player1.getName()));
                                        player.sendMessage(ChatColor.AQUA + Messages.get("view.report.form.player.message2",report));
                                        player.sendMessage(ChatColor.AQUA + Messages.get("view.report.form.player.message3",startDate));
                                        player.sendMessage(ChatColor.AQUA + "#--------------------------------------------------#");

                                    }));
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
                                    Player rPlayer = Bukkit.getPlayer(name);
                                    String startDate = LocalDate.now().toString();
                                    //database code
                                    ReportData.addReport(rPlayer,startDate,report,rPlayer.getName(),player.getName());
                                    player.sendMessage(ChatColor.GOLD + Messages.get("report.report.form.player.message1",name));
                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
                                        if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                        onlinePlayers.sendMessage(ChatColor.AQUA + Messages.get("report.report.form.player.message2",player.getName()));
                                        }
                                    }
                                }));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
