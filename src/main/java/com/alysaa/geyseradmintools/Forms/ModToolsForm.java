package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class ModToolsForm {
    public void ModList() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("Mod Tools")
                                .content("List of Mod Tools")
                                .button("Gamemode Survival")//2
                                .button("Gamemode Spectator")//3
                                .button("Vanish")//4
                                .button("Fly")//6
                                .button("Clear Inventory")//7
                                .button("Ban player")
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                        player.setGameMode(GameMode.SURVIVAL);
                                        player.sendMessage("[GeyserAdminTool] Gamemode set on Survival");
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        player.setGameMode(GameMode.SPECTATOR);
                                        player.sendMessage("[GeyserAdminTool] Gamemode set in Spectator");
                                    }
                                    if (response.getClickedButtonId() == 2) {
                                        player.hidePlayer(player);
                                        player.sendMessage("[GeyserAdminTool] Vanish Enabled");
                                    }
                                    if (response.getClickedButtonId() == 3) {
                                        player.setAllowFlight(true);
                                        player.sendMessage("[GeyserAdminTool] Flying enabled");
                                    }
                                    if (response.getClickedButtonId() == 4) {
                                        player.getInventory().clear();
                                        player.sendMessage("[GeyserAdminTool] Inventory cleared");
                                    }
                                    if (response.getClickedButtonId() == 5) {
                                        if (player.hasPermission("geyseradmintools.banplayer")) {
                                            BanPlayerForm.banPlayers();
                                        }
                                    }
                                }));
            }
        }
    }
}
