package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class BanPlayerForm {
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
                                .title("Ban/mute tool")
                                .dropdown("Select Player", playerlist)
                                .input("Hours banned")
                                .input("Reason ban")
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
                                    bl.addBan(name, reason, new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hour), null);
                                    Player player1 = Bukkit.getPlayer(name);
                                    player1.kickPlayer(reason);
                                }));
            }
        }
    }
}
