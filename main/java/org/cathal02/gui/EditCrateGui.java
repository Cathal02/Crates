package org.cathal02.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;
import org.cathal02.crates.CrateReward;
import org.cathal02.gui.holders.EditCrateGuiHolder;
import org.cathal02.utils.InventoryUtils;
import org.cathal02.utils.ItemBuilder;
import org.cathal02.utils.XMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO: Refactor inventory refreshing
public class EditCrateGui implements Listener {

    private final Crates plugin;
    private Crate crate;
    private final Map<Integer, CrateReward> crateRewardMappings = new HashMap<>();
    private CrateReward currentlySelectedReward = null;
    private Player player;
    private Inventory inventory;

    public EditCrateGui(final Crates plugin) {
        this.plugin = plugin;
    }


    public void open(final Player player, final Crate crate) {
        this.crate = crate;
        this.player = player;
        final Inventory inventory = Bukkit.createInventory(new EditCrateGuiHolder(),
                InventoryUtils.roundUpToInvSize(crate.getAmountOfRewards()), "Edit crate");
        this.inventory = inventory;

        refreshInventory();

        player.openInventory(inventory);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    private void onClick(final InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof EditCrateGuiHolder)) return;
        e.setCancelled(true);

        final Player player = (Player) e.getWhoClicked();
        final ItemStack clickedItem = e.getCurrentItem();

        // Adds an item
        if (e.getClickedInventory() instanceof PlayerInventory) {
            if (clickedItem == null || clickedItem.getType() == XMaterial.AIR.parseMaterial()) return;

            crate.addReward(clickedItem);
            refreshInventory();
            return;
        }

        // Editing an item
        if (clickedItem != null) {

            //Finds the reward we clicked on based off slot
            final CrateReward reward = crateRewardMappings.get(e.getRawSlot());
            if (reward == null) return;

            if (e.getClick() == ClickType.RIGHT) {
                crate.removeReward(reward);
                refreshInventory();
            } else if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                // Set chance
                currentlySelectedReward = reward;
                player.sendMessage(ChatColor.GREEN + "Set the chance you want for " + ChatColor.WHITE + "Item #" + (e.getRawSlot() + 1) + ChatColor.GREEN + ":");
                player.closeInventory();
            }
        }
    }

    @EventHandler
    private void asyncChat(final AsyncPlayerChatEvent e) {
        // Are we trying to change the chance of getting an item?
        if (currentlySelectedReward == null) return;
        e.setCancelled(true);

        try {
            final double chance = Double.parseDouble(e.getMessage());
            currentlySelectedReward.setChance(chance);

            e.getPlayer().sendMessage(ChatColor.GREEN + "Chance set to " + chance);
            currentlySelectedReward = null;
            open(player, crate);
        } catch (final NumberFormatException err) {
            e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RED + "Invalid " +
                    "amount.");
        }

    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof EditCrateGuiHolder)) return;
        if (currentlySelectedReward == null) {
            HandlerList.unregisterAll(this);
        }
    }


    private void refreshInventory() {
        if (crate == null || player == null) return;

        int counter = 0;
        if (crate.getCrateRewards().size() > inventory.getSize()) {
            inventory = Bukkit.createInventory(new EditCrateGuiHolder(),
                    InventoryUtils.roundUpToInvSize(crate.getAmountOfRewards()), "Edit crate");

            player.openInventory(inventory);
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
        inventory.clear();

        for (final CrateReward reward : crate.getCrateRewards()) {
            final int item = counter + 1;
            final List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.YELLOW + "Chance: " + ChatColor.AQUA + reward.getChance() + "%");
            lore.add("");
            lore.add(ChatColor.YELLOW + "RIGHT CLICK " + ChatColor.GRAY + "to " + ChatColor.YELLOW + "REMOVE");
            lore.add(ChatColor.YELLOW + "SHIFT+CLICK " + ChatColor.GRAY + "to " + ChatColor.YELLOW + "SET CHANCE");
            inventory.setItem(counter,
                    new ItemBuilder(reward.getItemStack().getType()).setLore(lore).setName(ChatColor.GREEN + "Item #" + item).toItemStack());
            crateRewardMappings.put(counter, reward);
            counter++;
        }
    }
}
