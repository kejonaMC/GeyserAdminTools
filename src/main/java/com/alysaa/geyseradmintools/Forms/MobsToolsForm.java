package com.alysaa.geyseradmintools.Forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.List;
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
                                .button("Boss Mobs")
                                .button("Kill all Mobs")
                                .responseHandler((form, responseData) -> {
                                    SimpleFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    if (response.getClickedButtonId() == 0) {
                                        if (Gat.plugin.getConfig().getBoolean("Forms.EnableMobForm.EnablePassiveMobs")) {
                                            PAMobs();
                                        } else {
                                            player.sendMessage("This form has been disabled");
                                        }
                                    }
                                    if (response.getClickedButtonId() == 1) {
                                        if (Gat.plugin.getConfig().getBoolean("Forms.EnableMobForm.EnableAggressiveMobs")) {
                                            AGMobs();
                                        } else {
                                            player.sendMessage("This form has been disabled");
                                        }
                                    }
                                    if (response.getClickedButtonId() == 2) {
                                        if (Gat.plugin.getConfig().getBoolean("Forms.EnableMobForm.EnableBossMobs")) {
                                            BOMobs();
                                        } else {
                                            player.sendMessage("This form has been disabled");
                                        }
                                    }
                                    if (response.getClickedButtonId() == 3) {
                                        List<Entity> ents = player.getNearbyEntities(35, player.getWorld().getMaxHeight() * 2, 10);

                                        for (Entity ent : ents) {
                                            if (ent instanceof Monster) {
                                                ((Monster) ent).setHealth(0);
                                            }
                                                if (ent instanceof Creature) {
                                                    ((Creature) ent).setHealth(0);
                                                }
                                            }
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
                                .dropdown("Spawn Mobs", "Zombie", "Skeleton", "Spider", "Creeper", "Blaze", "Vex", "Wither Skeleton")
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
                                            player.getWorld().spawnEntity(location, EntityType.ZOMBIE);
                                        }
                                    }
                                    if (response.getDropdown(0) == 1) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 2) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SPIDER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 3) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.CREEPER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 4) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.BLAZE);
                                        }
                                    }
                                    if (response.getDropdown(0) == 6) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.VEX);
                                        }
                                    }
                                    if (response.getDropdown(0) == 7) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 8) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SLIME);
                                        }
                                    }
                                    if (response.getDropdown(0) == 9) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.GHAST);
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
                                .dropdown("Spawn Mobs", "Pig", "Chicken", "Cow", "Horse", "Sheep", "Rabbit", "Donkey", "Bat", "Wolf", "Snowman", "Villager", "Squid", "Parrot", "Polar Bear", "Mushroom Cow")
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
                                            player.getWorld().spawnEntity(location, EntityType.PIG);
                                        }
                                    }
                                    if (response.getDropdown(0)== 1) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.CHICKEN);
                                        }
                                    }
                                    if (response.getDropdown(0)== 2) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.COW);
                                        }
                                    }
                                    if (response.getDropdown(0)== 3) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.HORSE);
                                        }
                                    }
                                    if (response.getDropdown(0)== 4) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SHEEP);
                                        }
                                    }
                                    if (response.getDropdown(0)== 5) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.RABBIT);
                                        }
                                    }
                                    if (response.getDropdown(0)== 6) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.DONKEY);
                                        }
                                    }
                                    if (response.getDropdown(0)== 7) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.BAT);
                                        }
                                    }
                                    if (response.getDropdown(0)== 8) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.WOLF);
                                        }
                                    }
                                    if (response.getDropdown(0)== 9) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SNOWMAN);
                                        }
                                    }
                                    if (response.getDropdown(0)== 10) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.VILLAGER);
                                        }
                                    }
                                    if (response.getDropdown(0)== 11) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(4);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.SQUID);
                                        }
                                    }
                                    if (response.getDropdown(0)== 12) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.PARROT);
                                        }
                                    }
                                    if (response.getDropdown(0)== 13) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.POLAR_BEAR);
                                        }
                                    }
                                    if (response.getDropdown(0)== 14) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s= response.getInput(1);
                                        int in=Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.MUSHROOM_COW);
                                        }
                                    }
                                }));
            }
        }
    }
    public void BOMobs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Boss Mobs Spawning")
                                .dropdown("Spawn Mobs", "Ender Dragon", "Wither", "Guardians")
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
                                            player.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 1) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.WITHER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 2) {
                                        int x = getRandom(10, 10);
                                        int y = getRandom(10, 10);
                                        int z = getRandom(10, 10);
                                        Location location = player.getLocation().add(x, y, z);
                                        String s = response.getInput(1);
                                        int in = Integer.parseInt(s);
                                        for (int i = 0; i < in; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.GUARDIAN);
                                        }
                                    }
                                }));
            }
        }
    }
}