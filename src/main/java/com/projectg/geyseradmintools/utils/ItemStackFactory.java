package com.projectg.geyseradmintools.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackFactory {

    private static ItemStack starTool;

    /**
     * Create the star tool. Getter is {@link #getStarTool()}
     */
    public static void createStarTool() {
        starTool = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta starToolMeta = starTool.getItemMeta();
        assert starToolMeta != null;
        starToolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Admin Tools"));
        starTool.setItemMeta(starToolMeta);
    }

    /**
     * @return the ItemStack of the star tool, if it has been generated.
     */
    public static ItemStack getStarTool() {
        return starTool;
    }

}
