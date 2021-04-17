package com.alysaa.geyseradmintools;

import com.alysaa.geyseradmintools.commands.GatCommand;
import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import com.alysaa.geyseradmintools.listeners.*;
import com.alysaa.geyseradmintools.utils.ItemStackFactory;
import com.alysaa.geyseradmintools.utils.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Gat extends JavaPlugin {

    public static Gat plugin;
    public static Logger logger;

    @Override
    public void onEnable(){
        new Metrics(this, 10943);
        plugin = this;
        logger = getLogger();
        new BanDatabaseSetup().mysqlSetup();
        new MuteDatabaseSetup().mysqlSetup();
        createFiles();
        checkConfigVer();
        ItemStackFactory.createStarTool();
        this.getCommand("gadmin").setExecutor(new GatCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolChat(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolInventory(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnRespawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnLogin(), this);
        getLogger().info("Plugin has been enabled - Provided by ProjectG");

    }
    @Override
    public void onDisable(){

    }
    public void checkConfigVer(){
        Logger logger = this.getLogger();
        //Change version number only when editing config.yml!
        if (!(getConfig().getInt("version") == 1 )){
            logger.info("Config.yml is outdated. please regenerate a new config.yml!");
        }
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
