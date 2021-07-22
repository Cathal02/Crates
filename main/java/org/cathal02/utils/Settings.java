package org.cathal02.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.cathal02.Crates;

public class Settings {

    FileConfiguration config;
    public Settings(Crates plugin){
        config = plugin.getConfig();
    }
    public int getMaxItemsPerCrate(){
        return config.getInt("maxRewardsPerCrate");
    }
}
