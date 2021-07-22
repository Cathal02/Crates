package org.cathal02.gui;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.cathal02.Crates;
import org.cathal02.gui.holders.ConfirmationHolder;
import org.cathal02.utils.InventoryUtils;
import org.cathal02.utils.ItemBuilder;
import org.cathal02.utils.XMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfirmationGUI implements Listener {

    private Consumer<Player> confirmConsumer;
    private Consumer<Player> declineConsumer;
    private final Player player;
    private final Crates plugin;

    public ConfirmationGUI(final Player player, final Crates plugin) {
        this.player = player;
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        player.closeInventory();
        open();
    }

    private void open() {
        final Inventory inventory = Bukkit.createInventory(new ConfirmationHolder(), 27, "Are you sure?");

        final List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Be careful, this action is irreversible!" +
                "");
        inventory.setItem(12,
                new ItemBuilder(XMaterial.EMERALD_BLOCK.parseMaterial()).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRM").setLore(lore).toItemStack());
        inventory.setItem(14,
                new ItemBuilder(XMaterial.REDSTONE_BLOCK.parseMaterial()).setName(ChatColor.RED + "" + ChatColor.BOLD + "CANCEL").toItemStack());
        InventoryUtils.fill(inventory);
        player.openInventory(inventory);
    }

    public ConfirmationGUI onConfirm(final Consumer<Player> onConfirm) {
        confirmConsumer = onConfirm;
        return this;
    }

    public ConfirmationGUI onDecline(final Consumer<Player> onDecline) {
        declineConsumer = onDecline;
        return this;

    }


    @EventHandler
    private void onConfirmClick(final InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof ConfirmationHolder)) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        final Player player = (Player) e.getWhoClicked();

        if (e.getRawSlot() == 12) { // Clicked confirm
            if (confirmConsumer == null) {
                player.closeInventory();
                return;
            }

            confirmConsumer.accept(player);
        } else if (e.getRawSlot() == 14) { // Clicked decline
            if (declineConsumer == null) {
                player.closeInventory();
                return;
            }

            declineConsumer.accept(player);
        }

        player.closeInventory();

    }

    @EventHandler
    private void onInventoryClose(final InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof ConfirmationHolder)) return;
        HandlerList.unregisterAll(this);
    }
}
