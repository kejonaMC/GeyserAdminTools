package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.Gat;
import com.projectg.geyseradmintools.language.Messages;
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
            FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
            fPlayer.sendForm(
                    SimpleForm.builder()
                            .title(ChatColor.DARK_AQUA + Messages.get("mod.form.title"))
                            .content(ChatColor.DARK_AQUA + Messages.get("mod.form.content"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button1"))//2
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button2"))//3
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button3"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button4"))//4
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button5"))//6
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button6"))//7
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button7"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button8"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mod.form.button9"))
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("mod.form.gamemode.survival"));
                                } else if (response.getClickedButtonId() == 1) {
                                    player.setGameMode(GameMode.SPECTATOR);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("mod.form.gamemode.spectator"));
                                } else if (response.getClickedButtonId() == 2) {
                                    TeleportForm.tpPlayer(player);
                                } else if (response.getClickedButtonId() == 3) {
                                    player.hidePlayer(Gat.getPlugin(), player);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("mod.form.vanish"));
                                } else if (response.getClickedButtonId() == 4) {
                                    player.setAllowFlight(true);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("mod.form.fly"));
                                } else if (response.getClickedButtonId() == 5) {
                                    player.getInventory().clear();
                                    player.sendMessage(ChatColor.GREEN + Messages.get("mod.form.inv.clear"));
                                } else if (response.getClickedButtonId() == 6) {
                                    if (player.hasPermission("geyseradmintools.banplayer")) {
                                        BanPlayerForm.banList(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                } else if (response.getClickedButtonId() == 7) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        MutePlayerForm.MuteList(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                } else if (response.getClickedButtonId() == 8) {
                                    if (player.hasPermission("geyseradmintools.viewreports")) {
                                        ReportForm.reportList(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + Messages.get("permission.button.error"));
                                    }
                                }
                            }));
        }
    }
}
