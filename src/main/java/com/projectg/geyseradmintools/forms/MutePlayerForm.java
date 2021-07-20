package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.database.MuteData;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
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
                            .title(ChatColor.DARK_AQUA + Messages.get("main.mute.form.title"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.mute.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.mute.form.button2"))
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
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                } else if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        unMutePlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                            }));
        }
    }
    public static void MutePlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            String[] playerList = names.toArray(new String[0]);
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title(ChatColor.DARK_AQUA + Messages.get("mute.mute.form.title"))
                                .dropdown(ChatColor.DARK_AQUA + Messages.get("mute.mute.form.dropdown"), playerList)
                                .input(Messages.get("mute.mute.form.input1"))
                                .input(Messages.get("mute.mute.form.input2"))
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    int clickedIndex = response.getDropdown(0);
                                    String day = response.getInput(1);
                                    String endDate;
                                    try {
                                        endDate = LocalDate.now().plusDays(Long.parseLong(day)).toString();
                                    } catch (NumberFormatException | NullPointerException e) {
                                        player.sendMessage(ChatColor.YELLOW + Messages.get("mute.input.error"));
                                        return;
                                    }
                                    String reason = response.getInput(2);
                                    String name = names.get(clickedIndex);
                                    Player mPlayer = Bukkit.getPlayer(name);
                                    String startDate = LocalDate.now().toString();
                                    //database code
                                    assert mPlayer != null;
                                    MuteData.addMute(mPlayer,startDate,endDate,reason,mPlayer.getName(),player.getName());
                                    mPlayer.sendMessage(ChatColor.RED + Messages.get("mute.mute.form.player.message1",endDate,reason));
                                    player.sendMessage(ChatColor.GOLD + Messages.get("mute.mute.form.player.message3",name));
                                    //end
                                }));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public static void unMutePlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            String query = "SELECT * FROM " + DatabaseSetup.muteTable;
            try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    names.add(rs.getString("Username"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title(ChatColor.DARK_AQUA + Messages.get("Unmute.mute.form.title"))
                                    .dropdown(ChatColor.DARK_AQUA + Messages.get("Unmute.mute.form.dropdown"), playerlist)
                                    .responseHandler((form, responseData) -> {
                                        CustomFormResponse response = form.parseResponse(responseData);
                                        if (!response.isCorrect()) {
                                            return;
                                        }
                                        int clickedIndex = response.getDropdown(0);
                                        String name = names.get(clickedIndex);
                                        Player mPlayer = Bukkit.getPlayer(name);
                                        //MySQL code
                                        MuteData.deleteMute(mPlayer.getUniqueId());
                                            player.sendMessage(ChatColor.GOLD + Messages.get("Unmute.mute.form.player.message1",name));
                                            mPlayer.sendMessage(ChatColor.GOLD + Messages.get("Unmute.mute.form.player.message2"));
                                    }));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
