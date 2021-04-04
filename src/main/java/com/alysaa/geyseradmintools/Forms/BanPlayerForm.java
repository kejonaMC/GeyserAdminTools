package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanPlayerForm {
    public static void banPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> players = new ArrayList<String>();
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                players.add(online.getName().replaceAll("[\\[\\](){}]",""));
            }
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Ban/mute tool")
                                .dropdown("Select Player", String.valueOf(players))
                                .input("day's banned/muted!")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getDropdown(0) == 0) {
                                    }
                                    if (response.getDropdown(0) == 1) {
                                    }
                                    if (response.getDropdown(0) == 2) {

                                    }
                                }));
            }
        }
    }
}