package org.cathal02.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cathal02.Crates;
import org.cathal02.crates.CrateReward;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Messages {
    private File customConfigFile;
    private static FileConfiguration customConfig;
    private static Crates plugin;

    private static String prefix;
    private final static List<String> helpCommand = new ArrayList<>();

    public Messages(final Crates _plugin) {
        plugin = _plugin;
        createCustomConfig();

        prefix = customConfig.getString("prefix");

        for (final String string : customConfig.getStringList("helpMessage")) {
            helpCommand.add(ChatColor.translateAlternateColorCodes('&', string));
        }
    }

    public static String getCrateRewardMessage(final CrateReward reward) {

        String msg = getString("rewardMessage", true);
        final String name;
        if (reward.getItemStack().hasItemMeta() && reward.getItemStack().getItemMeta().getDisplayName() != null) {
            name = reward.getItemStack().getItemMeta().getDisplayName();
        } else {
            name = reward.getItemStack().getType().name();
        }

        msg = msg.replaceAll("%item_name%", name);
        msg = msg.replaceAll("%item_chance%", String.valueOf(reward.getChance()));

        return msg;
    }

    public static String getCoolDownMessage(final long coolDown) {

        String msg = getString("hasCoolDown", true);
        msg = msg.replaceAll("%cooldown%", String.valueOf(coolDown));

        return msg;
    }

    public static void sendHelpMessage(final Player player) {
        for (final String string : helpCommand) {
            player.sendMessage(string);
        }
    }


    private void createCustomConfig() {
        customConfigFile = new File(plugin.getDataFolder().getPath() + File.separator + "lang.yml");
        if (!customConfigFile.exists()) plugin.saveResource(customConfigFile.getName(), false);


        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (final IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getString(final String message, final boolean translate) {
        String s = customConfig.getString(message);
        if (s == null) {
            return ChatColor.RED + "Message not found";
        }

        s = s.replaceAll("%prefix%", prefix);

        return translate ? ChatColor.translateAlternateColorCodes('&', s) : s;
    }

    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
