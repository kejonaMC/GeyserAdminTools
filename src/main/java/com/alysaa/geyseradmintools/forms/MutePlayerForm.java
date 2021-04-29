package com.alysaa.geyseradmintools.forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.database.DatabaseSetup;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MutePlayerForm {
    public static void MuteList(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    SimpleForm.builder()
                            .title("Mute/Unmute Tool")
                            .button("Mute Player")
                            .button("Unmute Player")
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        MutePlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to use this button!");
                                    }
                                }
                                if (response.getClickedButtonId() == 1) {
                                    if (player.hasPermission("geyseradmintools.muteplayer")) {
                                        unMutePlayers(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "[GeyserAdminTools] You do not have the permission to use this button!");
                                    }
                                }
                            }));
        }
    }
    public static void MutePlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            String[] playerlist = names.toArray(new String[0]);
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
            if (isFloodgatePlayer) {
                FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                fplayer.sendForm(
                        CustomForm.builder()
                                .title("Mute tool")
                                .dropdown("Select Player", playerlist)
                                .input("Day's Muted")
                                .input("Mute Reason")
                                .responseHandler((form, responseData) -> {
                                    CustomFormResponse response = form.parseResponse(responseData);
                                    if (!response.isCorrect()) {
                                        return;
                                    }
                                    int clickedIndex = response.getDropdown(0);
                                    String day = response.getInput(1);
                                    String time;
                                    try {
                                        time = LocalDate.now().plusDays(Long.parseLong(day)).toString();
                                    } catch (NumberFormatException | NullPointerException e) {
                                        player.sendMessage(ChatColor.YELLOW + "[GeyserAdminTools] wrong usage ! <username> <amount of days> <reason>");
                                        return;
                                    }
                                    String reason = response.getInput(2);
                                    String name = names.get(clickedIndex);
                                    Player player1 = Bukkit.getPlayer(name);
                                    //database code
                                    try {
                                        String sql = "(UUID,REASON,USERNAME,ENDDATE) VALUES (?,?,?,?)";
                                        PreparedStatement insert = DatabaseSetup.getConnection().prepareStatement("INSERT INTO " + DatabaseSetup.Mutetable
                                                + sql);
                                        insert.setString(1, player1.getUniqueId().toString());
                                        insert.setString(2, reason);
                                        insert.setString(3, name);
                                        insert.setString(4, time);
                                        insert.executeUpdate();
                                        // Player inserted now
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                    player1.sendMessage(ChatColor.RED + "You are Muted till " + time + " for Reason: " + reason);
                                    Gat.logger.info("Player " + player.getName() + " has muted " + player1.getName() + " till: " + time + " for reason: " + reason);
                                    player.sendMessage(ChatColor.GOLD + "[GeyserAdminTools] Player " + name + " is muted");
                                    //end
                                }));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public static void unMutePlayers(Player player) {
        Runnable runnable = () -> {
            UUID uuid = player.getUniqueId();
            List<String> names = new ArrayList<>();
            String query = "SELECT * FROM " + DatabaseSetup.Mutetable;
            try (Statement stmt = DatabaseSetup.getConnection().createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    names.add(rs.getString("Username"));
                }
                rs.close();
                String[] playerlist = names.toArray(new String[0]);
                boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
                if (isFloodgatePlayer) {
                    FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
                    fplayer.sendForm(
                            CustomForm.builder()
                                    .title("unmute tool")
                                    .dropdown("Select Player to unmute", playerlist)
                                    .responseHandler((form, responseData) -> {
                                        CustomFormResponse response = form.parseResponse(responseData);
                                        if (!response.isCorrect()) {
                                            return;
                                        }
                                        int clickedIndex = response.getDropdown(0);
                                        String name = names.get(clickedIndex);
                                        Player player1 = Bukkit.getPlayer(name);
                                        //MySQL code
                                        try {
                                            PreparedStatement statement = DatabaseSetup.getConnection()
                                                    .prepareStatement("DELETE FROM " + DatabaseSetup.Mutetable + " WHERE UUID=?");
                                            statement.setString(1, player1.getUniqueId().toString());
                                            statement.execute();
                                            player.sendMessage(ChatColor.GOLD + "[GeyserAdminTools] Player " + name + " is unmuted");
                                            player1.sendMessage(ChatColor.GOLD + "Your mute has been lifted by an admin!");

                                        } catch (SQLException exe) {
                                            exe.printStackTrace();
                                        }
                                    }));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
