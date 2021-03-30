package com.alysaa.geyseradmintools.utils;

import com.alysaa.geyseradmintools.Gat;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class CheckJavaOrFloodPlayer {
    /**
     * Determines if a player is from Bedrock
     * @param uuid the UUID to determine
     * @return true if the player is from Bedrock
     */
    public static boolean isFloodgatePlayer(UUID uuid) {
        if (!Gat.plugin.getConfig().getBoolean("EnableAdminTools")){
            return GeyserConnector.getInstance().getPlayerByUuid(uuid) != null;
        } else {
            return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
        }
    }
}