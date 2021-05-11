package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class AdminToolsForm {
    @SuppressWarnings("deprecation")
    public void ATList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
            fPlayer.sendForm(
                    SimpleForm.builder()
                            .title(ChatColor.DARK_AQUA + Messages.get("admin.form.title"))
                            .content(ChatColor.ITALIC + Messages.get("admin.form.content"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button2"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button3"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button4"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button5"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button6"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button7"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button8"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button9"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button10"))
                            .button(ChatColor.DARK_AQUA + Messages.get("admin.form.button11"))
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    player.setGameMode(GameMode.CREATIVE);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.gamemode.creative"));
                                } else if (response.getClickedButtonId() == 1) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.gamemode.survival"));
                                } else if (response.getClickedButtonId() == 2) {
                                    player.setGameMode(GameMode.SPECTATOR);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.gamemode.spectator"));
                                } else if (response.getClickedButtonId() == 3) {
                                    TeleportForm.tpPlayer(player);
                                } else if (response.getClickedButtonId() == 4) {
                                    for (Player target : Bukkit.getOnlinePlayers()) {
                                        target.hidePlayer(player);
                                        player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.vanish"));
                                    }
                                } else if (response.getClickedButtonId() == 5) {
                                    player.setInvulnerable(true);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.godmode"));
                                } else if (response.getClickedButtonId() == 6) {
                                    player.setAllowFlight(true);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.fly"));
                                } else if (response.getClickedButtonId() == 7) {
                                    player.getInventory().clear();
                                    player.sendMessage(ChatColor.GREEN + Messages.get("admin.form.inv.clear"));
                                } else if (response.getClickedButtonId() == 8) {
                                    BanPlayerForm.banList(player);
                                } else if (response.getClickedButtonId() == 9) {
                                    MutePlayerForm.MuteList(player);
                                } else if (response.getClickedButtonId() == 10) {
                                    ReportForm.reportList(player);
                                }
                            }));
        }
    }
}

