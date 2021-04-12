package com.alysaa.geyseradmintools.database;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlGetSet implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayer(player.getUniqueId(), player);
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = MySql.getConnection()
                    .prepareStatement("SELECT * FROM " + MySql.tables + " WHERE PlayerUUID=?");
            statement.setString(1, uuid.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void createPlayer(final UUID uuid, Player player) {
        try {
            PreparedStatement statement = MySql.getConnection()
                    .prepareStatement("SELECT * FROM " + MySql.tables + " WHERE PlayerUUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(uuid)) {
                PreparedStatement insert = MySql.getConnection()
                        .prepareStatement("INSERT INTO " + MySql.tables + " (PlayerUUID) VALUES (?)");
                insert.setString(1, uuid.toString());
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
