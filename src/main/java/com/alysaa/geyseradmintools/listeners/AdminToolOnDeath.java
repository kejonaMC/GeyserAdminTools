package com.alysaa.geyseradmintools.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AdminToolOnDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> list = e.getDrops();
        list.removeIf(item -> item.getType() == Material.NETHER_STAR);
    }

}
