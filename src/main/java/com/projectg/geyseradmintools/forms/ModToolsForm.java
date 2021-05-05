package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class ModToolsForm {
    public void ModList(Player player) {
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
                            .button("Player Teleport")
                            .button("Vanish")//4
                            .button("Fly")//6
                            .button("Clear Inventory")//7
                            .button("Ban/Unban player")
                            .button("Mute/Unmute player")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Gamemode set on Survival");
                                }
                                if (response.getClickedButtonId() == 1) {
                                    player.setGameMode(GameMode.SPECTATOR);
                                    player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Gamemode set in Spectator");
                                }
                                if (response.getClickedButtonId() == 2) {
                                    TeleportForm.tpPlayer(player);
                                }
                                if (response.getClickedButtonId() == 3) {
                                    player.hidePlayer(player);
                                    player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Vanish Enabled");
                                }
                                if (response.getClickedButtonId() == 4) {
                                    player.setAllowFlight(true);
                                    player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Flying enabled");
                                }
                                if (response.getClickedButtonId() == 5) {
                                    player.getInventory().clear();
                                    player.sendMessage(ChatColor.GREEN + "[GeyserAdminTools] Inventory cleared");
                                }
                                if (response.getClickedButtonId() == 6) {
                                    if (player.hasPermission("geyseradmintools.banplayer")) {
                                        BanPlayerForm.banList(player);
                                    }
                                }
                                else {  player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to use this button!");
                                }
                                if (response.getClickedButtonId() == 7) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        MutePlayerForm.MuteList(player);
                                    }
                                    else {  player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 8) {
                                    if (player.hasPermission("geyseradmintools.viewreportplayer")) {
                                    ReportForm.reportList(player);
                                    }
                                }
                            }));
        }
    }
}
