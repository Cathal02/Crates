package org.cathal02.crates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.cathal02.Crates;
import org.cathal02.crates.events.CrateOpenEvent;
import org.cathal02.gui.ConfirmationGUI;
import org.cathal02.gui.holders.CrateOpenerHolder;
import org.cathal02.utils.Messages;
import org.cathal02.utils.XMaterial;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class CrateOpener implements Listener {

    final private Crates plugin;
    private final HashMap<UUID, Integer> tasks = new HashMap<>();

    public CrateOpener(final Crates plugin) {
        this.plugin = plugin;
    }

    public void openCrate(final Player player, final Crate crate) {
        new ConfirmationGUI(player, plugin).onConfirm(p -> {
            if (crate.getCrateRewards().size() < 1) {
                player.sendMessage(Messages.getString("crateHasNoItems", true));
                return;
            }
            removeItemFromHand(player);
            openGui(player, crate);
        });
    }

    private void openGui(final Player player, final Crate crate) {
        final Inventory inventory = Bukkit.createInventory(new CrateOpenerHolder(), 45,
                "Open Crate: " + crate.getName());
        player.openInventory(inventory);

        tasks.put(player.getUniqueId(), new BukkitRunnable() {

            int topCounter = 0;
            int minSlot = 22;
            final int rewards = new Random().nextInt((plugin.getSettings().getMaxItemsPerCrate()) + 1) + 1;
            boolean calcComplete = false;


            @Override
            public void run() {

                if (!calcComplete) {
                    if (rewards % 2 == 0) {
                        inventory.setItem(22, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
                        minSlot -= rewards / 2;
                    } else {
                        minSlot -= Math.floorDiv(rewards, 2);
                    }
                    calcComplete = true;
                    player.openInventory(inventory);

                }

                // Crate animation
                crateAnimation();

                // Sets rewards in crate gui
                if (topCounter == minSlot) {
                    addRewardsToInventory();
                    Bukkit.getPluginManager().callEvent(new CrateOpenEvent(crate, player));
                    cancel();
                }

            }

            private void crateAnimation() {
                if (topCounter < minSlot) {
                    inventory.setItem(topCounter, plugin.getCrateUtils().getRandomGlassPane());
                    inventory.setItem(inventory.getSize() - topCounter - 1,
                            plugin.getCrateUtils().getRandomGlassPane());
                    topCounter++;
                }
            }

            private void addRewardsToInventory() {
                final int upperBound = plugin.getCrateUtils().getUpperBound(rewards);

                for (int i = 0; i < upperBound; i++) {
                    //Skips middle divider if present
                    if (i + topCounter == 22 && rewards % 2 == 0) continue;

                    final CrateReward reward = crate.getRandomCrateReward();

                    inventory.setItem(topCounter + i, reward.getItemStack());
                    player.getInventory().addItem(reward.getItemStack());
                    player.sendMessage(Messages.getCrateRewardMessage(reward));

                }
            }

        }.runTaskTimer(plugin, 0L, 2L).getTaskId());
    }

    private void removeItemFromHand(final Player player) {
        final ItemStack item = player.getItemInHand();
        if (item.getAmount() == 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(item.getAmount() - 1);
        }
    }


    @EventHandler
    public void onPlayerLogout(final PlayerQuitEvent e) {
        tasks.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onCrateOpen(final CrateOpenEvent e) {

        //Closes inventory if still open
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (e.getPlayer().getInventory() instanceof CrateOpenerHolder) {
                return;
            }
            e.getPlayer().closeInventory();
        }, 6 * 20L);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof CrateOpenerHolder) {

            if (tasks.containsKey(e.getPlayer().getUniqueId())) {
                Bukkit.getScheduler().cancelTask(tasks.get(e.getPlayer().getUniqueId()));
                tasks.remove(e.getPlayer().getUniqueId());
            }
        }
    }

}
