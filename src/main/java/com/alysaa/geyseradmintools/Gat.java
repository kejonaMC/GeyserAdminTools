package com.alysaa.geyseradmintools;

import com.alysaa.geyseradmintools.commands.GatCommand;
import com.alysaa.geyseradmintools.listeners.AdminLockChat;
import com.alysaa.geyseradmintools.listeners.AdminToolOnJoin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Gat extends JavaPlugin {
    public static Gat plugin;
    @Override
    public void onEnable(){
        plugin = this;
        createFiles();
        this.getCommand("gadmin").setExecutor(new GatCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminLockChat(), this);
        getLogger().info("Plugin has been enabled");

    }
    @Override
    public void onDisable(){

    }
    private void createFiles() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
