package com.projectg.geyseradmintools.database;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class BanData {

    public static void addBan(Player bPlayer, String startDate, String endDate, String reason, String username,
                       String bannedBy) {

        UUID uuid = bPlayer.getUniqueId();
        try {
            String sql = "(UUID,REASON,USERNAME,BANNEDBY,ENDDATE,STARTDATE) VALUES (?,?,?,?,?,?)";
            PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.banTable
                    + sql);
            insert.setString(1, uuid.toString());
            insert.setString(2, reason);
            insert.setString(3, username);
            insert.setString(4, bannedBy);
            insert.setString(5, endDate);
            insert.setString(6, startDate);
            insert.executeUpdate();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static void deleteBan(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.execute();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static String infoBan(UUID uuid, String column) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.banTable + " WHERE UUID=?");

            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            // ENDDATE, STARTDATE,  REASON, NAME, USERNAME,BANNEDBY
            return results.getString(column);

        } catch (SQLException exe) {
            exe.printStackTrace();

        }
        return null;

    }

    public static void checkBan(List<String> names) {
        String query = "SELECT * FROM " + DatabaseSetup.banTable;
        try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                names.add(rs.getString("Username"));
            }
            rs.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
