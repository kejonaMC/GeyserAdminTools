package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class BanPlayerForm implements Listener {
   public static String name;
    public static void banList(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("Ban/Mute Tool")
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
                                            banPlayers();
                                        } else {
                                            player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                        }
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        if (player.hasPermission("geyseradmintools.banplayer")) {
                                            unbanPlayers();
                                        } else {
                                            player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                        }
                                    }
                                }));
            }
        }
    }
    public static void banPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            String[] playerlist = names.toArray(new String[0]);
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Ban/unban tool")
                                .dropdown("Select Player", playerlist)
                                .input("Hours banned")
                                .input("Reason ban")
                                .input("Player name to unban")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    if (response.getToggle(0)) {
                                        int clickedIndex = response.getDropdown(0);
                                        String hours = response.getInput(1);
                                        String reason = response.getInput(2);
                                        int hour = Integer.parseInt(hours);
                                        String name = names.get(clickedIndex);
                                        BanList bl = Bukkit.getBanList(BanList.Type.NAME);
                                        bl.addBan(name,"reason for ban: "+reason, new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hour), null);
                                        Player player1 = Bukkit.getPlayer(name);
                                        player1.kickPlayer("you where banned for: "+reason);
                                    }
                                    if (response.getToggle(1)){
                                        Bukkit.getBanList(BanList.Type.NAME).pardon(response.getInput(3));
                                    }
                                }));
            }
        }
    }
    public static void unbanPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Ban/unban tool")
                                .input("Player name to unban")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    if (response.getDropdown(0) == 0) {
                                        String s = response.getInput(1);
                                        Bukkit.getBanList(BanList.Type.NAME).pardon(s);
                                    }
                                }));
            }
        }
    }

}
