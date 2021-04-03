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

public class AdminToolsForm {
    @SuppressWarnings("deprecation")
    public void ATList() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("Admin Tools")
                                .content("List of Admin Tools")
                                .button("Gamemode Creative")//1
                                .button("Gamemode Survival")//2
                                .button("Gamemode Spectator")//3
                                .button("Vanish")//4
                                .button("God Mode")//5
                                .button("Fly")//6
                                .button("Clear Inventory")//7
                                .button("Heal")//8
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                        player.setGameMode(GameMode.CREATIVE);
                                        player.sendMessage("Gamemode set on Creative");
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        player.setGameMode(GameMode.SURVIVAL);
                                        player.sendMessage("Gamemode set on Survival");
                                    }
                                    if (response.getClickedButtonId() == 2) {
                                        player.setGameMode(GameMode.SPECTATOR);
                                        player.sendMessage("Gamemode set in Spectator");
                                    }
                                    if (response.getClickedButtonId() == 3) {
                                        player.hidePlayer(player);
                                        player.sendMessage("Vanish Enabled");
                                    }
                                    if (response.getClickedButtonId() == 4) {
                                        player.setInvulnerable(true);
                                        player.sendMessage("God mode enabled");
                                    }
                                    if (response.getClickedButtonId() == 5) {
                                        player.setAllowFlight(true);
                                        player.sendMessage("Flying enabled");
                                    }
                                    if (response.getClickedButtonId() == 6) {
                                        player.getInventory().clear();
                                        player.sendMessage("Inventory cleared");
                                    }
                                    if (response.getClickedButtonId() == 7) {
                                        player.setHealth(12);
                                        player.sendMessage("Health regenerated");
                                    }
                                }));
            }
        }
    }
}
