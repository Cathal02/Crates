package org.cathal02.crates;

import org.bukkit.inventory.ItemStack;

public class CrateReward {
    private ItemStack itemStack;
    private Double chance;

    public CrateReward(ItemStack item, double chance) {
        this.itemStack =item;
        this.chance  =Math.max(chance,0);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance  =Math.max(chance,0);

    }
}
