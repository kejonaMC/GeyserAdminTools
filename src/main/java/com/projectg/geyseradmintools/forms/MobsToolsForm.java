package com.projectg.geyseradmintools.forms;

import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
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
    public void preMTList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
            fPlayer.sendForm(
                    SimpleForm.builder()
                            .title(ChatColor.DARK_AQUA + Messages.get("mob.form.title"))
                            .content(ChatColor.DARK_AQUA + Messages.get("mob.form.content"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mob.form.button1"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mob.form.button2"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mob.form.button3"))
                            .button(ChatColor.DARK_AQUA + Messages.get("mob.form.button4"))
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    PAMobs(player);
                                }
                                if (response.getClickedButtonId() == 1) {
                                    AGMobs(player);
                                }
                                if (response.getClickedButtonId() == 2) {
                                    BOMobs(player);
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

    public void AGMobs(Player player) {
        final int[] in = {0};
        try {
            UUID uuid = player.getUniqueId();
            Location target = player.getEyeLocation().add(player.getLocation().getDirection().multiply(4 /* distance in blocks */));
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
                fPlayer.sendForm(
                        CustomForm.builder()
                                .title(ChatColor.DARK_AQUA + Messages.get("agro.mob.form.title"))
                                .dropdown(ChatColor.DARK_AQUA + Messages.get("agro.mob.form.dropdown")
                                        , Messages.get("agro.mob.form.mob1")
                                        , Messages.get("agro.mob.form.mob2")
                                        , Messages.get("agro.mob.form.mob3")
                                        , Messages.get("agro.mob.form.mob4")
                                        , Messages.get("agro.mob.form.mob5")
                                        , Messages.get("agro.mob.form.mob6")
                                        , Messages.get("agro.mob.form.mob7")
                                        , Messages.get("agro.mob.form.mob8")
                                        , Messages.get("agro.mob.form.mob9"))
                                .input(Messages.get("agro.mob.form.input"))
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    String s = response.getInput(1);
                                    try {
                                        in[0] = Integer.parseInt(s);
                                    }catch (NumberFormatException e) {
                                        player.sendMessage(ChatColor.YELLOW + Messages.get("mob.input.error"));
                                    }
                                    if (response.getDropdown(0) == 0) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.ZOMBIE);
                                        }
                                    }
                                    if (response.getDropdown(0) == 1) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 2) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SPIDER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 3) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.CREEPER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 4) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.BLAZE);
                                        }
                                    }
                                    if (response.getDropdown(0) == 5) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.VEX);
                                        }
                                    }
                                    if (response.getDropdown(0) == 6) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.WITHER_SKELETON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 7) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SLIME);
                                        }
                                    }
                                    if (response.getDropdown(0) == 8) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.GHAST);
                                        }
                                    }
                                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PAMobs(Player player) {
        final int[] in = {0};
        try {
            UUID uuid = player.getUniqueId();
            Location target = player.getEyeLocation().add(player.getLocation().getDirection().multiply(4 /* distance in blocks */));
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
                fPlayer.sendForm(
                        CustomForm.builder()
                                .title(ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.title"))
                                .dropdown(ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.dropdown")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob1")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob2")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob3")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob4")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob5")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob6")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob7")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob8")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob9")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob10")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob11")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob12")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob13")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob14")
                                        , ChatColor.DARK_AQUA + Messages.get("nonagro.mob.form.mob15"))
                                .input(Messages.get("nonagro.mob.form.input"))
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    String s = response.getInput(1);
                                    try {
                                        in[0] = Integer.parseInt(s);
                                    }catch (NumberFormatException e) {
                                        player.sendMessage(ChatColor.YELLOW + Messages.get("mob.input.error"));
                                    }
                                    if (response.getDropdown(0) == 0) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.PIG);
                                        }
                                    }
                                    if (response.getDropdown(0) == 1) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.CHICKEN);
                                        }
                                    }
                                    if (response.getDropdown(0) == 2) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.COW);
                                        }
                                    }

                                    if (response.getDropdown(0) == 3) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.HORSE);
                                        }
                                    }
                                    if (response.getDropdown(0) == 4) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SHEEP);
                                        }
                                    }

                                    if (response.getDropdown(0) == 5) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.RABBIT);
                                        }
                                    }
                                    if (response.getDropdown(0) == 6) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.DONKEY);
                                        }
                                    }
                                    if (response.getDropdown(0) == 7) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.BAT);
                                        }
                                    }
                                    if (response.getDropdown(0) == 8) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.WOLF);
                                        }
                                    }
                                    if (response.getDropdown(0) == 9) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SNOWMAN);
                                        }
                                    }
                                    if (response.getDropdown(0) == 10) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.VILLAGER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 11) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.SQUID);
                                        }
                                    }
                                    if (response.getDropdown(0) == 12) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.PARROT);
                                        }
                                    }
                                    if (response.getDropdown(0) == 13) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.POLAR_BEAR);
                                        }
                                    }
                                    if (response.getDropdown(0) == 14) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(target, EntityType.MUSHROOM_COW);
                                        }
                                    }
                                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BOMobs(Player player) {
        final int[] in = {0};
        try {
            UUID uuid = player.getUniqueId();
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(uuid);
                fPlayer.sendForm(
                        CustomForm.builder()
                                .title(ChatColor.DARK_AQUA + Messages.get("boss.mob.form.title"))
                                .dropdown(ChatColor.DARK_AQUA + Messages.get("boss.mob.form.dropdown")
                                        , ChatColor.DARK_AQUA + Messages.get("boss.mob.form.mob1")
                                        , ChatColor.DARK_AQUA + Messages.get("boss.mob.form.mob2")
                                        , ChatColor.DARK_AQUA + Messages.get("boss.mob.form.mob3"))
                                .input(Messages.get("boss.mob.form.input"))
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        // player closed the form or returned invalid info (see FormResponse)
                                        return;
                                    }
                                    int x = getRandom(10, 10);
                                    int y = getRandom(10, 10);
                                    int z = getRandom(10, 10);
                                    Location location = player.getLocation().add(x, y, z);
                                    String s = response.getInput(1);
                                    try {
                                        in[0] = Integer.parseInt(s);
                                    }catch (NumberFormatException e) {
                                        player.sendMessage(ChatColor.YELLOW + Messages.get("mob.input.error"));
                                    }
                                    if (response.getDropdown(0) == 0) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
                                        }
                                    }
                                    if (response.getDropdown(0) == 1) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.WITHER);
                                        }
                                    }
                                    if (response.getDropdown(0) == 2) {
                                        for (int i = 0; i < in[0]; i++) {
                                            player.getWorld().spawnEntity(location, EntityType.GUARDIAN);
                                        }
                                    }
                                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}