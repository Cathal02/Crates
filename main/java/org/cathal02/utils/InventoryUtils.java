package org.cathal02.utils;


import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtils {


    public static void fill(final Inventory inventory) {
        final ItemStack itemStack = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(" ");
        itemStack.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {

                inventory.setItem(i, itemStack);
            }
            if (inventory.getItem(i).getType() == XMaterial.AIR.parseMaterial()) {
                inventory.setItem(i, itemStack);
            }
        }
    }


    public static int roundUpToInvSize(final int size) {
        if (size <= 9) {
            return 9;
        } else if (size <= 18) {
            return 18;
        } else if (size <= 27) {
            return 27;
        } else if (size <= 36) {
            return 36;
        } else if (size <= 45) {
            return 45;
        } else {
            return 54;
        }
    }
}
