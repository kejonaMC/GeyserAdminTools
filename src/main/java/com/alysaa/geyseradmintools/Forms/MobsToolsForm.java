package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
    public void preMTList(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        SimpleForm.builder()
                                .title("Mobs Tools")
                                .content("List of Mobs")
                                .button("Passive Mobs")
                                .button("Aggressive Mobs")
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                       AGMobs();
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        PAMobs();
                                    }
                                }));
            }
        }
    }
    public void AGMobs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Aggressive Mobs Spawning")
                                .dropdown("Spawn Mobs", "Zombie", "Skeleton", "Spider", "Creeper")
                                .input("Amount of mobs")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);

                                    if (response.getDropdown(0) == 0) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.ZOMBIE);
                                        }
                                    }
                                    if (response.getDropdown(1) == 1) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(2);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(2) == 2) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(3);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.SPIDER);
                                        }
                                    }
                                    if (response.getDropdown(3) == 3) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(4);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.CREEPER);
                                        }
                                    }
                                }));
            }
        }
    }
    public void PAMobs(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Passive Mobs Spawning")
                                .dropdown("Spawn Mobs", "Zombie", "Skeleton", "Spider", "Creeper")
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
                                    if (response.getDropdown(1)== 1) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(2);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(2)== 2) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(3);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.SPIDER);
                                        }
                                    }
                                    if (response.getDropdown(3)== 3) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(4);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            Bukkit.getWorld("world").spawnEntity(location, EntityType.CREEPER);
                                        }
                                    }
                                }));
            } else {
                player.sendMessage("Sorry this is a Bedrock command!");
            }
        }
    }
}