package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class ServerToolsForm {
    public void STList() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("Server Tools")
                                .content("List of server tools")
                                .button("Weather Sun")//1
                                .button("Weather Rain")//2
                                .button("Day")//3
                                .button("Night")//4
                                .button("Shutdown server")//5
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                        player.getWorld().setStorm(false);
                                        player.sendMessage("Weather set on Sunny");
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        player.getWorld().setStorm(true);
                                        player.sendMessage("Weather set on Rain");
                                    }
                                    if (response.getClickedButtonId() == 2) {
                                        player.getWorld().setTime(1000);
                                        player.sendMessage("Time set on Day");
                                    }
                                    if (response.getClickedButtonId() == 3) {
                                        Bukkit.getWorld("world").setTime(12000);
                                        player.sendMessage("Time set on Night");
                                    }
                                    if (response.getClickedButtonId() == 4) {
                                        Bukkit.getServer().shutdown();
                                        player.sendMessage("Server is shutting down");
                                    }
                                }));
            }
        }
    }
}
