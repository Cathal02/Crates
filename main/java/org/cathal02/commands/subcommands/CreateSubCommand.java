package org.cathal02.commands.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cathal02.Crates;
import org.cathal02.gui.ConfirmationGUI;
import org.cathal02.utils.Messages;
import org.cathal02.utils.XMaterial;

public class CreateSubCommand extends CrateSubCommand {

    private Crates crates;

    @Override
    public void handleCrateSubCommand(final Player player, final String[] args,
                                      final Crates plugin) {

        crates = plugin;
        if (!player.hasPermission("crates.admin.create")) {
            player.sendMessage(Messages.getString("noPermission", true));
            return;
        }

        // Create a crate.
        createCrate(player, args[1]);
    }

    @Override
    public String getAction() {
        return "create";
    }

    private void createCrate(final Player player, final String name) {
        final ItemStack item = player.getItemInHand();

        if (item.getType() == XMaterial.AIR.parseMaterial()) {
            player.sendMessage(Messages.getString("noItemInHand", true));
            return;
        }

        new ConfirmationGUI(player, crates)
                .onConfirm(p -> {
                    if (crates.getCrateManager().createCrate(name, item)) {
                        p.sendMessage(Messages.getString("crateCreated", true));
                    } else {
                        p.sendMessage(Messages.getString("crateCouldNotBeCreated", true));
                    }

                }).onDecline(p -> {
            player.sendMessage(Messages.getString("actionCancelled", true));
        });
    }
}
