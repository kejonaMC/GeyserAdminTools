package com.projectg.geyseradmintools.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_ITEM = makeItem(Material.BLACK_STAINED_GLASS_PANE, "");

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();

    public abstract int getTotalSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void fillNullSlots(){
        for (int i = 0; i < getTotalSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_ITEM);
            }
        }
    }

    public ItemStack makeItem(Material material, String displayName, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(displayName);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);

        return item;
    }

}
