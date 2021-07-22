package org.cathal02.commands.subcommands;

import org.bukkit.entity.Player;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;
import org.cathal02.gui.EditCrateGui;
import org.cathal02.utils.Messages;

public class EditSubCommand extends CrateSubCommand {

    @Override
    public void handleCrateSubCommand(final Player player, final String[] args,
                                      final Crates plugin) {
        final Crate crate = plugin.getCrateManager().getCrate(args[1]);

        if (crate == null) {
            player.sendMessage(Messages.getString("crateDoesNotExist", true));
            return;
        }

        if (!player.hasPermission("crates.admin.edit")) {
            player.sendMessage(Messages.getString("noPermission", true));
            return;
        }
        // Edit crate
        new EditCrateGui(plugin).open(player, crate);
    }

    @Override
    public String getAction() {
        return "edit";
    }
}
