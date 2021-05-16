package com.projectg.geyseradmintools.database;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MuteData {
    public static void addMute(Player mPlayer, String startDate, String endDate, String reason, String username,
                              String mutedBy) {

        UUID uuid = mPlayer.getUniqueId();
        try {
            String sql = "(UUID,REASON,USERNAME,MUTEDBY,ENDDATE,STARTDATE) VALUES (?,?,?,?,?,?)";
            PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.muteTable
                    + sql);
            insert.setString(1, uuid.toString());
            insert.setString(2, reason);
            insert.setString(3, username);
            insert.setString(4, mutedBy);
            insert.setString(5, endDate);
            insert.setString(6, startDate);
            insert.executeUpdate();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static void deleteMute(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.muteTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.execute();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static String infoMute(UUID uuid, String column) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.muteTable + " WHERE UUID=?");

            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            // ENDDATE, STARTDATE,  REASON, NAME, USERNAME,BANNEDBY
            return results.getString(column);

        } catch (SQLException ignored) {

        }
        return null;

    }
}
