package com.projectg.geyseradmintools.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public abstract class PaginatedMenu extends Menu {

    protected int pageIndex = 0;
    protected int maxItemsPerPage = 28;
    protected boolean lastPage = false;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    /**
     * Add the glass border at the bottom containing the buttons
     */
    public void setMenuBorder() {
        if (pageIndex == 0) {
            inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
        }
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
        if (!lastPage) {
            inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));
        }

        for (int i = 45; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_ITEM);
            }
        }
    }

    /**
     * Remove the contents of the inventory (excluding the border)
     */
    public void removeContents() {
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, null);
        }
    }

    // todo: maybe put these two methods back in Menu.java and have a setPage() method here
    public abstract void setMenuItems(int pageNumber);

    public void open(int pageIndex) {
        if (inventory == null) {
            inventory = Bukkit.createInventory(this, getTotalSlots(), getMenuName());
        }
        this.pageIndex = pageIndex;
        setMenuBorder();
        this.setMenuItems(pageIndex);
        playerMenuUtility.getOwner().openInventory(inventory);
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
    @Override
    public int getTotalSlots() {
        return 54;
    }
}
