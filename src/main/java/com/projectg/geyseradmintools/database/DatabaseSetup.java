package com.projectg.geyseradmintools.database;

import com.projectg.geyseradmintools.Gat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DatabaseSetup {

    private static Connection connection;
    public String host;
    public String database;
    public String username;
    public String password;
    public static String banTable;
    public static String muteTable;
    public static String reportTable;
    public int port;

    public boolean mysqlSetup() {
        host = Gat.plugin.getConfig().getString("host");
        port = Gat.plugin.getConfig().getInt("port");
        database = Gat.plugin.getConfig().getString("database");
        username = Gat.plugin.getConfig().getString("username");
        password = Gat.plugin.getConfig().getString("password");
        banTable = "Ban_list";
        muteTable = "Mute_list";
        reportTable = "Report_list";
        if (Gat.plugin.getConfig().getBoolean("EnableMySQL")) {
            try {
                synchronized (this) {
                    if (getConnection() != null && !getConnection().isClosed()) {
                        return false;
                    }

                    Class.forName("com.mysql.jdbc.Driver");
                    setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                            + this.port + "/" + this.database, this.username, this.password));
                    createTable();

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[GeyserAdminTools] MYSQL Connected");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File dataFolder = new File(Gat.plugin.getDataFolder(), "PlayerData.db");
                if (!dataFolder.exists()) {
                    try {
                        dataFolder.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Class.forName("org.sqlite.JDBC");
                    setConnection(DriverManager.getConnection("jdbc:sqlite:" + dataFolder));
                    PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.banTable + " (UUID char(36), Reason varchar(500), Username varchar(16),BannedBy varchar(16), EndDate varchar(500),  StartDate varchar(500))");
                    PreparedStatement stmt2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.muteTable + " (UUID char(36), Reason varchar(500), Username varchar(16),BannedBy varchar(16), EndDate varchar(500),  StartDate varchar(500))");
                    PreparedStatement stmt3 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.reportTable + " (UUID char(36), Report varchar(500), Reported varchar(16), Reporting varchar(16), Date varchar(500))");
                    stmt.execute();
                    stmt2.execute();
                    stmt3.execute();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[GeyserAdminTools] SQLite Connected");
                } catch (Exception e) {
                    System.out.println("SQLite Error");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.muteTable + " (UUID char(36), Reason varchar(500), Username varchar(16),BannedBy varchar(16), EndDate varchar(500),  StartDate varchar(500))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.banTable + " (UUID char(36), Reason varchar(500), Username varchar(16), EndDate varchar(500))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + DatabaseSetup.reportTable + " (UUID char(36), Report varchar(500), Reported varchar(16), Reporting varchar(16), Date varchar(500))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        DatabaseSetup.connection = connection;
    }
}
