package com.alysaa.geyseradmintools.javamenu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    private Player playerToKill;

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    public Player getPlayerToKill() {
        return playerToKill;
    }

    public void SetReport(Player playerToKill) {
        this.playerToKill = playerToKill;
    }
}
