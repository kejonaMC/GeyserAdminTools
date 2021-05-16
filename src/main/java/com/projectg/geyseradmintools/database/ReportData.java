package com.projectg.geyseradmintools.database;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class ReportData {
    public static void addReport(Player mPlayer, String startDate, String report, String username,
                               String reportedBy) {

        UUID uuid = mPlayer.getUniqueId();
        try {
            String sql = "(UUID,REPORT,USERNAME,REPORTEDBY,STARTDATE) VALUES (?,?,?,?,?)";
            PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.reportTable
                    + sql);
            insert.setString(1, uuid.toString());
            insert.setString(2, report);
            insert.setString(3, username);
            insert.setString(4, reportedBy);
            insert.setString(5, startDate);
            insert.executeUpdate();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static void deleteReport(UUID uuid) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("DELETE FROM " + DatabaseSetup.reportTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.execute();

        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

    public static String infoReport(UUID uuid, String column) {
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.reportTable + " WHERE UUID=?");

            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            // ENDDATE, STARTDATE,  REASON, NAME, USERNAME,BANNEDBY
            return results.getString(column);

        } catch (SQLException ignored) {

        }
        return null;

    }
    public static void reportList(List<String> names) {
        String query = "SELECT * FROM " + DatabaseSetup.reportTable;
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
