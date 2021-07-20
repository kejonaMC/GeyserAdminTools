package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.database.BanData;
import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BanPlayerForm {
    public static void banList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
            fPlayer.sendForm(
                    SimpleForm.builder()
                            .title(ChatColor.DARK_AQUA + Messages.get("main.ban.form.title"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.ban.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("main.ban.form.button2"))
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
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.banplayer")) {
                                        unbanPlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                            }));
        }
    }

    public static void banPlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            String[] playerList = names.toArray(new String[0]);
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
                fPlayer.sendForm(
                        CustomForm.builder()
                                .title(ChatColor.DARK_AQUA + Messages.get("ban.ban.form.title"))
                                .dropdown(ChatColor.DARK_AQUA + Messages.get("ban.ban.form.dropdown"), playerList)
                                .input(ChatColor.DARK_AQUA + Messages.get("ban.ban.form.input1"))
                                .input(ChatColor.DARK_AQUA + Messages.get("ban.ban.form.input2"))
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    int clickedIndex = response.getDropdown(0);
                                    String dayInput = response.getInput(1);
                                    String endDate;
                                    try {
                                        endDate = LocalDate.now().plusDays(Long.parseLong(dayInput)).toString();
                                    } catch (NumberFormatException | NullPointerException  e) {
                                        player.sendMessage(ChatColor.YELLOW + Messages.get("ban.input.error"));
                                   return;
                                    }
                                    String reason = response.getInput(2);
                                    String name = names.get(clickedIndex);
                                    Player bPlayer = Bukkit.getPlayer(name);
                                    String startDate = LocalDate.now().toString();
                                    //database code
                                    assert bPlayer != null;
                                    BanData.addBan(bPlayer,startDate,endDate,reason, bPlayer.getName(), player.getName());
                                    bPlayer.kickPlayer(Messages.get("ban.ban.form.player.message1",reason,endDate));
                                    player.sendMessage(ChatColor.GOLD + Messages.get("ban.ban.form.player.message2",name));
                                    //end
                                }));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void unbanPlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            BanData.banList(names);
                String[] playerList = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fPlayer.sendForm(
                            CustomForm.builder()
                                    .title(ChatColor.DARK_AQUA + Messages.get("unban.ban.form.title"))
                                    .dropdown(ChatColor.DARK_AQUA + Messages.get("unban.ban.form.dropdown"), playerList)
                                    .responseHandler((form, responseData) -> {
                                        CustomFormResponse response = form.parseResponse(responseData);
                                        if (!response.isCorrect()) {
                                            return;
                                        }
                                        int clickedIndex = response.getDropdown(0);
                                        String name = names.get(clickedIndex);
                                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(name);
                                        BanData.deleteBan(player1.getUniqueId());
                                            player.sendMessage(ChatColor.GREEN +Messages.get("unban.ban.form.player.message1",name));
                                    }));
                }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}