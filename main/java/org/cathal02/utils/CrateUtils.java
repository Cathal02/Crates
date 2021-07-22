package org.cathal02.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CrateUtils {

    private final Random random;
    private final Map<Integer, ItemStack> glassPanes = new HashMap<>();
    private final Map<Integer, ChatColor> colorMap = new HashMap<>();

    public CrateUtils() {
        random = new Random();
        glassPanes.put(1, XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(2, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(3, XMaterial.GREEN_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(4, XMaterial.MAGENTA_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(5, XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(6, XMaterial.LIME_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(7, XMaterial.PINK_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(8, XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(9, XMaterial.BLUE_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(10, XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(11, XMaterial.BROWN_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(12, XMaterial.GREEN_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(13, XMaterial.RED_STAINED_GLASS_PANE.parseItem());
        glassPanes.put(14, XMaterial.CYAN_STAINED_GLASS_PANE.parseItem());

        colorMap.put(10, ChatColor.GREEN);
        colorMap.put(11, ChatColor.AQUA);
        colorMap.put(12, ChatColor.RED);
        colorMap.put(13, ChatColor.LIGHT_PURPLE);
    }


    public ItemStack getRandomGlassPane() {
        final int glassRandomNum = random.nextInt(13) + 1;
        final int colorRandomNum = random.nextInt(13);
        final String title;
        if (colorRandomNum > 9) {
            title = colorMap.get(colorRandomNum) + "...";
        } else {
            title = ChatColor.translateAlternateColorCodes('&', "&" + colorRandomNum + "...");
        }
        return new ItemBuilder(glassPanes.get(glassRandomNum)).setName(title).toItemStack();
    }

    public Integer getUpperBound(final int rewards) {
        return (rewards % 2 == 0) ? rewards + 1 : rewards;
    }
}


