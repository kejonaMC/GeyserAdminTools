package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.utils.ItemStackFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AdminToolOnDeath implements Listener {

    private static final ItemStack starTool = ItemStackFactory.getStarTool();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        // Destroy the star tool if a player dies with it
        List<ItemStack> deathItems = e.getDrops();
        deathItems.removeIf(itemStack -> itemStack.equals(starTool));
    }
}
