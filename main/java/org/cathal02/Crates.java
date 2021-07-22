package org.cathal02;

import org.bukkit.plugin.java.JavaPlugin;
import org.cathal02.commands.CrateCommand;
import org.cathal02.crates.CrateManager;
import org.cathal02.crates.CrateTimer;
import org.cathal02.data.CrateDataManager;
import org.cathal02.listeners.CancelClickListener;
import org.cathal02.listeners.CrateOpenListener;
import org.cathal02.utils.CrateUtils;
import org.cathal02.utils.Messages;
import org.cathal02.utils.SQLUtil;
import org.cathal02.utils.Settings;

public final class Crates extends JavaPlugin {
    
    private CrateManager crateManager;
    private CrateDataManager crateDataManager;
    private Settings settings;
    private CrateUtils crateUtils;
    private CrateTimer crateTimer;
    private SQLUtil sqlUtil;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        new Messages(this);
        sqlUtil = new SQLUtil(this);
        crateDataManager = new CrateDataManager(this);
        crateManager = new CrateManager(this);
        settings = new Settings(this);
        crateUtils = new CrateUtils();
        crateTimer = new CrateTimer(this);
        getCommand("crate").setExecutor(new CrateCommand(this));


        getServer().getPluginManager().registerEvents(new CrateOpenListener(this), this);
        getServer().getPluginManager().registerEvents(new CancelClickListener(), this);
        getServer().getPluginManager().registerEvents(crateTimer, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        crateDataManager.saveCrates(crateManager.getCrates());
        crateTimer.onClose();
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    public CrateDataManager getCrateDataManager() {
        return crateDataManager;
    }

    public Settings getSettings() {
        return settings;
    }

    public CrateUtils getCrateUtils() {
        return crateUtils;
    }

    public CrateTimer getCrateTimer() {
        return crateTimer;
    }

    public SQLUtil getSQLUtil() {
        return sqlUtil;
    }
}
