package org.cathal02.crates;

import org.bukkit.inventory.ItemStack;
import org.cathal02.utils.WeightedItem;

import java.util.ArrayList;
import java.util.List;

public class Crate {

    public Crate(final String name, final ItemStack crateItem) {
        this.name = name;
        this.crateItem = crateItem;
    }

    private List<CrateReward> crateRewards = new ArrayList<>();


    private final String name;
    private final ItemStack crateItem;
    private Integer maxRewards;

    public Crate(final String name, final ItemStack crateItem, final List<CrateReward> rewards) {
        crateRewards = rewards;
        this.crateItem = crateItem;
        this.name = name;

    }

    public Integer getMaxRewards() {
        return maxRewards;
    }

    public String getName() {
        return name;
    }

    public Integer getAmountOfRewards() {
        return crateRewards.size();
    }

    public void addReward(final ItemStack item) {
        crateRewards.add(new CrateReward(item, 10));
    }

    public List<CrateReward> getCrateRewards() {
        return crateRewards;
    }

    public void removeReward(final CrateReward reward) {
        crateRewards.remove(reward);
    }


    public CrateReward getRandomCrateReward() {
        final WeightedItem<CrateReward> rewards = new WeightedItem<CrateReward>();
        for (final CrateReward reward : crateRewards) {
            rewards.addEntry(reward, reward.getChance());
        }
        return rewards.getRandom();
    }


    public ItemStack getCrateItem() {
        return crateItem;
    }
}
