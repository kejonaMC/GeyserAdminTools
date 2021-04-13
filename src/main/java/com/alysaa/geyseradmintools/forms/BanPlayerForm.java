package com.alysaa.geyseradmintools.forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.MySql;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BanPlayerForm {
    public static void banList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    SimpleForm.builder()
                            .title("Ban/Unban Tool")
                            .button("Ban Player")
                            .button("Unban Player")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    if (player.hasPermission("geyseradmintools.banplayer")) {
                                        banPlayers(player);
                                    } else {
                                        player.sendMessage("[GeyserAdminTool] You do not have the permission to use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.banplayer")) {
                                        unbanPlayers(player);
                                    } else {
                                        player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                    }
                                }
                            }));
        }
    }

    public static void banPlayers(Player player) {
        UUID uuid = player.getUniqueId();
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        String[] playerlist = names.toArray(new String[0]);
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    CustomForm.builder()
                            .title("Ban tool")
                            .dropdown("Select Player", playerlist)
                            .input("Hours banned")
                            .input("Ban Reason")
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    return;
                                }
                                int clickedIndex = response.getDropdown(0);
                                String hours = response.getInput(1);
                                String reason = response.getInput(2);
                                int hour = Integer.parseInt(hours);
                                String name = names.get(clickedIndex);
                                BanList bl = Bukkit.getBanList(BanList.Type.NAME);
                                bl.addBan(name, "reason for ban: " + reason, new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hour), null);
                                Player player1 = Bukkit.getPlayer(name);
                                player1.kickPlayer("you where banned for: " + reason);
                                player.sendMessage("Player " + name + " is banned");
                                //MySQL code
                                if (Gat.plugin.getConfig().getBoolean("EnableMySQL")) {
                                    try {
                                        PreparedStatement statement = MySql.getConnection()
                                                .prepareStatement("DELETE FROM " + MySql.Bantable + " WHERE UUID='" + "'");
                                        statement.execute();
                                        PreparedStatement insert = MySql.getConnection().prepareStatement("INSERT INTO " + MySql.Bantable
                                                + "(UUID,REASON,USERNAME,HOURS) VALUE (?,?,?,?)");
                                        insert.setString(1, player1.getUniqueId().toString());
                                        insert.setString(2, reason);
                                        insert.setString(3, name);
                                        insert.setString(4, hours);
                                        insert.executeUpdate();

                                        // Player inserted now
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }

                            }));
        }
    }

    public static void unbanPlayers(Player player) {
        UUID uuid = player.getUniqueId();
        List<String> names = Bukkit.getBannedPlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
        String[] playerlist = names.toArray(new String[0]);
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    CustomForm.builder()
                            .title("unban tool")
                            .dropdown("Select Player to unban", playerlist)
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    return;
                                }
                                int clickedIndex = response.getDropdown(0);
                                String name = names.get(clickedIndex);
                                BanList bl = Bukkit.getBanList(BanList.Type.NAME);
                                bl.pardon(name);
                                player.sendMessage("Player " + name + " is unbanned");
                                Player player1 = Bukkit.getPlayer(name);
                                //MySQL code
                                if (Gat.plugin.getConfig().getBoolean("EnableMySQL")) {
                                    try {
                                        PreparedStatement statement = MySql.getConnection()
                                                .prepareStatement("DELETE FROM " + MySql.Bantable + " WHERE UUID=?");
                                        statement.setString(1, player1.getUniqueId().toString());
                                        statement.execute();

                                    } catch (SQLException exe) {
                                        exe.printStackTrace();
                                    }
                                }
                            }));
        }
    }
}