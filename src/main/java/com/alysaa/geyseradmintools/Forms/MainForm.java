package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class MainForm {
    @SuppressWarnings("deprecation")
    public static void formList(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("AdminTools")
                                .content("List of Tools")
                                .button("Admin Tools")
                                .button("Mod Tools")
                                .button(Gat.plugin.getConfig().getString("CommandsForm.ButtonMainMenu"))
                                .button("Mobs Tools")
                                .button("Server Tools")
                                .button("Remove all gadmin effects")
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                        if (player.hasPermission("geyseradmintools.admintool")) {
                                            if (Gat.plugin.getConfig().getBoolean("Forms.EnableAdminForm")) {
                                                new AdminToolsForm().ATList();
                                            } else {
                                                player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                            }
                                        }
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        if (player.hasPermission("geyseradmintools.modtool")) {
                                            if (Gat.plugin.getConfig().getBoolean("Forms.EnableModForm")) {
                                                new ModToolsForm().ModList();
                                            } else {
                                                player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                            }
                                        }
                                    }
                                    if (response.getClickedButtonId() == 2) {
                                        if (player.hasPermission("geyseradmintools.customcommands")) {
                                            if (Gat.plugin.getConfig().getBoolean("Forms.EnableCustomCommands")) {
                                                new CustomCommandsTool().CustomCommands();

                                            } else {
                                                player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                            }
                                        }
                                    }
                                    if (response.getClickedButtonId() == 3) {
                                        if (player.hasPermission("geyseradmintools.mobtool")) {
                                            if (Gat.plugin.getConfig().getBoolean("Forms.EnableMobForm")) {
                                                new MobsToolsForm().preMTList();
                                            } else {
                                                player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                            }
                                        }
                                    }
                                    if (response.getClickedButtonId() == 4) {
                                        if (player.hasPermission("geyseradmintools.servertool")) {
                                            if (Gat.plugin.getConfig().getBoolean("Forms.EnableServerForm")) {
                                                new ServerToolsForm().STList();
                                            } else {
                                                player.sendMessage("[GeyserAdminTool] You do not have the permission to ise this button!");
                                            }
                                        }
                                    }
                                    if (response.getClickedButtonId() == 5) {
                                        player.setInvulnerable(false);
                                        player.setAllowFlight(false);
                                        player.setGameMode(GameMode.SURVIVAL);
                                        player.showPlayer(player);
                                        player.sendMessage("[GeyserAdminTool] All effects have been cleared");
                                    }
                                }));
            }
        }
    }
}