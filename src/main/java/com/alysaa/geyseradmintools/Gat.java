package com.alysaa.geyseradmintools;

import com.alysaa.geyseradmintools.commands.*;
import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import com.alysaa.geyseradmintools.database.ReportDatabaseSetup;
import com.alysaa.geyseradmintools.javamenu.PlayerMenuUtility;
import com.alysaa.geyseradmintools.listeners.*;
import com.alysaa.geyseradmintools.utils.ItemStackFactory;
import com.alysaa.geyseradmintools.utils.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class Gat extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public static Gat plugin;
    public static Logger logger;

    @Override
    public void onEnable(){
        new Metrics(this, 10943);
        plugin = this;
        logger = getLogger();
        new BanDatabaseSetup().mysqlSetup();
        new MuteDatabaseSetup().mysqlSetup();
        new ReportDatabaseSetup().mysqlSetup();
        createFiles();
        checkConfigVer();
        ItemStackFactory.createStarTool();
        this.getCommand("gadmin").setExecutor(new FormCommand());
        this.getCommand("gban").setExecutor(new BanCommand());
        this.getCommand("gunban").setExecutor(new UnbanCommand());
        this.getCommand("gmute").setExecutor(new MuteCommand());
        this.getCommand("gunmute").setExecutor(new UnmuteCommand());
        this.getCommand("greport").setExecutor(new ReportCommand());
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
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
    public static Gat getPlugin() {
        return plugin;
    }
}
