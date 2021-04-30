package com.alysaa.geyseradmintools.forms;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class CustomCommandsForm {
    public void CustomCommands(Player player) {
        FileConfiguration config = Gat.plugin.getConfig();
        UUID uuid = player.getUniqueId();
        boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(uuid);
        if (isFloodgatePlayer) {
            FloodgatePlayer fplayer = FloodgateApi.getInstance().getPlayer(uuid);
            fplayer.sendForm(
                    SimpleForm.builder()
                            .title(config.getString("CommandsForm.Title"))
                            .content(config.getString("CommandsForm.Content"))
                            .button(config.getString("CommandsForm.Button1"))
                            .button(config.getString("CommandsForm.Button2"))
                            .button(config.getString("CommandsForm.Button3"))
                            .button(config.getString("CommandsForm.Button4"))
                            .button(config.getString("CommandsForm.Button5"))
                            .button(config.getString("CommandsForm.Button6"))
                            .button(config.getString("CommandsForm.Button7"))
                            .button(config.getString("CommandsForm.Button8"))
                            .button(config.getString("CommandsForm.Button9"))
                            .button(config.getString("CommandsForm.Button10"))
                            .responseHandler((form, responseData) -> {
                                SimpleFormResponse response = form.parseResponse(responseData);
                                if (!response.isCorrect()) {
                                    // player closed the form or returned invalid info (see FormResponse)
                                    return;
                                }
                                if (response.getClickedButtonId() == 0) {
                                    String server1 = config.getString("CommandsForm.SetCommand1");
                                    assert server1 != null;
                                    player.performCommand(server1);
                                }
                                if (response.getClickedButtonId() == 1) {
                                    String server2 = config.getString("CommandsForm.SetCommand2");
                                    assert server2 != null;
                                    player.performCommand(server2);
                                }
                                if (response.getClickedButtonId() == 2) {
                                    String server3 = config.getString("CommandsForm.SetCommand3");
                                    assert server3 != null;
                                    player.performCommand(server3);
                                }
                                if (response.getClickedButtonId() == 3) {
                                    String server4 = config.getString("CommandsForm.SetCommand4");
                                    assert server4 != null;
                                    player.performCommand(server4);
                                }
                                if (response.getClickedButtonId() == 4) {
                                    String server5 = config.getString("CommandsForm.SetCommand5");
                                    assert server5 != null;
                                    player.performCommand(server5);
                                }
                                if (response.getClickedButtonId() == 5) {
                                    String server6 = config.getString("CommandsForm.SetCommand6");
                                    assert server6 != null;
                                    player.performCommand(server6);
                                }
                                if (response.getClickedButtonId() == 6) {
                                    String server7 = config.getString("CommandsForm.SetCommand7");
                                    assert server7 != null;
                                    player.performCommand(server7);
                                }
                                if (response.getClickedButtonId() == 7) {
                                    String server8 = config.getString("CommandsForm.SetCommand8");
                                    assert server8 != null;
                                    player.performCommand(server8);
                                }
                                if (response.getClickedButtonId() == 8) {
                                    String server9 = config.getString("CommandsForm.SetCommand9");
                                    assert server9 != null;
                                    player.performCommand(server9);
                                }
                                if (response.getClickedButtonId() == 9) {
                                    String server10 = config.getString("CommandsForm.SetCommand10");
                                    assert server10 != null;
                                    player.performCommand(server10);
                                }
                            }));
        }
    }
}

