package com.alysaa.geyseradmintools.forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.MySql;
import com.alysaa.geyseradmintools.listeners.AdminToolMutePlayer;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MutePlayerForm {
    public static void MuteList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    SimpleForm.builder()
                            .title("Mute/UnMute Tool")
                            .button("Mute Player")
                            .button("UnMute Player")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        MutePlayers(player);
                                    } else {
                                        player.sendMessage("[GeyserAdminTool] You do not have the permission to use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        unMutePlayers(player);
                                    } else {
                                        player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                    }
                                }
                            }));
        }
    }

    public static void MutePlayers(Player player) {
        UUID uuid = player.getUniqueId();
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        String[] playerlist = names.toArray(new String[0]);
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    CustomForm.builder()
                            .title("mute tool")
                            .dropdown("Select Player", playerlist)
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    return;
                                }
                                int clickedIndex = response.getDropdown(0);
                                String name = names.get(clickedIndex);
                                Player player1 = Bukkit.getPlayer(name);
                                new AdminToolMutePlayer().bannedFromChat.add(player1);
                                if (Gat.plugin.getConfig().getBoolean("EnableMySQL")) {
                                    try {
                                        PreparedStatement insert = MySql.getConnection()
                                                .prepareStatement("INSERT INTO " + MySql.Mutetable + " (PlayerUUID,PlayerName) VALUES (?,?)");
                                        insert.setString(1, String.valueOf(player1.getUniqueId()));
                                        insert.setString(2, player1.getName());
                                        insert.executeUpdate();
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                            }));
        }
    }

    public static void unMutePlayers(Player player) {
        UUID uuid = player.getUniqueId();
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        String[] playerlist = names.toArray(new String[0]);
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    CustomForm.builder()
                            .title("Unmute tool")
                            .dropdown("Select Player to unmute", playerlist)
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    return;
                                }
                                int clickedIndex = response.getDropdown(0);
                                String name = names.get(clickedIndex);
                                Player player1 = Bukkit.getPlayer(name);
                                new AdminToolMutePlayer().bannedFromChat.remove(player1);
                            }));
        }
    }
}