package com.projectg.geyseradmintools.database;

import org.bukkit.OfflinePlayer;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BanData {

    public void addBan(OfflinePlayer bPlayer, Date startDate, Date endDate, String reason, String username,
                       String bannedBy){

        UUID uuid = bPlayer.getUniqueId();
        try {
            String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
            PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.banTable
                    + sql);
            insert.setString(1, uuid.toString());
            insert.setString(2, startDate.toString());
            insert.setString(3, endDate.toString());
            insert.setString(4, reason);
            insert.setString(5, username);
            insert.setString(6, bannedBy);
            insert.executeUpdate();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public void deleteBan(String uuid){
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
            statement.setString(1, uuid);
            statement.execute();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }
    public String CheckBan(String uuid, String column){
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.banTable + " WHERE UUID=?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            results.next();
            // START, END, REASON, NAME, USERNAME,BANNEDBY
            return results.getString(column);

        } catch (SQLException exe) {
            exe.printStackTrace();

        }
        return null;

    }
}
