package com.projectg.geyseradmintools;

import com.alysaa.geyseradmintools.commands.*;
import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.gui.PlayerMenuUtility;
import com.alysaa.geyseradmintools.listeners.*;
import com.projectg.geyseradmintools.listeners.*;
import com.projectg.geyseradmintools.utils.ItemStackFactory;
import com.projectg.geyseradmintools.utils.bstats.Metrics;
import com.projectg.geyseradmintools.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

public class Gat extends JavaPlugin {

    public static Gat plugin;
    public static Logger logger;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();



    @Override
    public void onEnable(){
        new Metrics(this, 10943);
        plugin = this;
        logger = getLogger();
        new DatabaseSetup().mysqlSetup();
        createFiles();
        checkConfigVer();
        ItemStackFactory.createStarTool();
        Objects.requireNonNull(this.getCommand("gadmin")).setExecutor(new FormCommand());
        Objects.requireNonNull(this.getCommand("gban")).setExecutor(new BanCommand());
        Objects.requireNonNull(this.getCommand("gunban")).setExecutor(new UnbanCommand());
        Objects.requireNonNull(this.getCommand("gmute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(this.getCommand("gunmute")).setExecutor(new UnmuteCommand());
        Objects.requireNonNull(this.getCommand("greport")).setExecutor(new ReportCommand());
        Objects.requireNonNull(this.getCommand("gviewreport")).setExecutor(new ViewReportCommand());
        Objects.requireNonNull(this.getCommand("gviewbans")).setExecutor(new ViewBansCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolChat(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolInventory(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnRespawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminToolOnLogin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getLogger().info("Plugin has been enabled - Provided by ProjectG");

    }
    @Override
    public void onDisable(){

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
    public static Gat getPlugin() {
        return plugin;
    }
}
