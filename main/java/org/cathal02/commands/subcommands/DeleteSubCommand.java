package org.cathal02.commands.subcommands;

import org.bukkit.entity.Player;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;
import org.cathal02.gui.ConfirmationGUI;
import org.cathal02.utils.Messages;

public class DeleteSubCommand extends CrateSubCommand {
    @Override
    public void handleCrateSubCommand(final Player player, final String[] args,
                                      final Crates plugin) {
        final Crate crate = plugin.getCrateManager().getCrate(args[1]);

        if (crate == null) {
            player.sendMessage(Messages.getString("crateDoesNotExist", true));
            return;
        }

        if (!player.hasPermission("crates.admin.delete")) {
            player.sendMessage(Messages.getString("noPermission", true));
            return;
        }

        new ConfirmationGUI(player, plugin)
                .onConfirm(p -> {
                    plugin.getCrateManager().deleteCrate(crate);
                    p.sendMessage(Messages.getString("crateDeleted", true));

                }).onDecline(p -> {
            player.sendMessage(Messages.getString("actionCancelled", true));
        });
    }

    @Override
    public String getAction() {
        return "delete";
    }
}
