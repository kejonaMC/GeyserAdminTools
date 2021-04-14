package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Gat;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AdminToolOnDeath implements Listener {

    private final ItemStack starTool = Gat.getStarTool();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        // Destroy the star tool if a player dies with it
        List<ItemStack> deathItems = e.getDrops();
        deathItems.removeIf(itemStack -> itemStack.equals(starTool));
    }
}
