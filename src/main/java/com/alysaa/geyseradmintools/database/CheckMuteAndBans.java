package com.alysaa.geyseradmintools.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CheckMuteAndBans {

    public void loopBansMutes() {
        String query = "SELECT * FROM " + BanDatabaseSetup.Bantable;
        List<String> dates = new ArrayList<>();
        try (
                Statement stmt = BanDatabaseSetup.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dates.add(rs.getString("EndDate"));
            }
            rs.close();
            String[] checkcDates = dates.toArray(new String[0]);




        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
