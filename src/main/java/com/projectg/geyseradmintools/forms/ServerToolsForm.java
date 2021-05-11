package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Objects;
import java.util.UUID;

import static com.projectg.geyseradmintools.listeners.AdminToolChat.isMuted;

public class ServerToolsForm {
    public void STList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
            fPlayer.sendForm(
                    SimpleForm.builder()
                            .title(ChatColor.DARK_AQUA + Messages.get("server.form.title"))
                            .content(ChatColor.DARK_AQUA + Messages.get("server.form.content"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button2"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button3"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button4"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button5"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button6"))
                            .button(ChatColor.DARK_AQUA + Messages.get("server.form.button7"))
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    player.getWorld().setStorm(false);
                                    player.getWorld().setWeatherDuration(18000);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("server.form.weather.sun"));
                                } else if (response.getClickedButtonId() == 1) {
                                    player.getWorld().setStorm(true);
                                    player.getWorld().setWeatherDuration(18000);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("server.form.weather.rain"));
                                } else if (response.getClickedButtonId() == 2) {
                                    player.getWorld().setTime(1000);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("server.form.day"));
                                } else if (response.getClickedButtonId() == 3) {
                                    Objects.requireNonNull(Bukkit.getWorld("world")).setTime(14000);
                                    player.sendMessage(ChatColor.GREEN + Messages.get("server.form.night"));
                                } else if (response.getClickedButtonId() == 4) {
                                    int i;
                                    for (i = 0; i < 25; i++) {
                                        Bukkit.getServer().broadcastMessage(" ");
                                    }
                                    Bukkit.getServer().broadcastMessage(ChatColor.GOLD + Messages.get("server.form.clear.chat"));
                                } else if (response.getClickedButtonId() == 5) {
                                    if (player.hasPermission("geyseradmintools.gadmin")) {
                                        if (!isMuted) {
                                            // Disables chat
                                            isMuted = true;
                                            Bukkit.broadcastMessage(ChatColor.DARK_RED + Messages.get("server.form.lock.chat"));
                                        }
                                    }
                                } else if (response.getClickedButtonId() == 6) {
                                    if (player.hasPermission("geyseradmintools.gadmin")) {
                                        if (isMuted) {
                                            // Enables chat
                                            isMuted = false;
                                            Bukkit.broadcastMessage(ChatColor.GREEN + Messages.get("server.form.unlock.chat"));
                                        }
                                    }
                                }
                            }));
        }
    }
}