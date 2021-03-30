package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Random;
import java.util.UUID;

public class MobsToolsForm {
    public int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
    public void MTList() {
        FileConfiguration config = Gat.plugin.getConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Player Tools")
                                .dropdown("Spawn Mobs", "Zombie", "Skeleton")
                                .input("Amount of mobs")
                                .responseHandler((form, responseData) -> {
                                            CustomFormResponse response = form.parseResponse(responseData);

                                            if (response.getDropdown(0)== 0) {
                                                int x = getRandom(10, 10);
                                                int y = getRandom(10, 10);
                                                int z = getRandom(10, 10);
                                                Location location = player.getLocation().add(x, y, z);
                                                String s= response.getInput(1);
                                                int in=Integer.parseInt(s);
                                                for (int i = 0; i < in; i++) {
                                                    Bukkit.getWorld("world").spawnEntity(location, EntityType.ZOMBIE);
                                                }
                                            }
                                }));
            } else {
                player.sendMessage("Sorry this is a Bedrock command!");
            }
        }
    }
}