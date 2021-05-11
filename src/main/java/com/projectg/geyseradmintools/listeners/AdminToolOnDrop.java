package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.language.Messages;
import com.projectg.geyseradmintools.utils.ItemStackFactory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class AdminToolOnDrop implements Listener {
    private static final ItemStack starTool = ItemStackFactory.getStarTool();

    @EventHandler
    public void onItemDrop (PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().equals(starTool)) {
            e.getItemDrop().remove();
            Player p = e.getPlayer();
            p.sendMessage(ChatColor.DARK_AQUA + Messages.get("tool.drop"));
        }
    }

}
