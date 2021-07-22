package org.cathal02.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;
import org.cathal02.utils.Messages;

public class GiveSubCommand extends CrateSubCommand {
    @Override
    public void handleCrateSubCommand(final Player player, final String[] args,
                                      final Crates plugin) {
        if (!player.hasPermission("crates.admin.give")) {
            player.sendMessage(Messages.getString("noPermission", true));
            return;
        }

        if (args.length < 4) {
            player.sendMessage(Messages.getString("incorrectCrateGiveCommandFormat", true));
            return;
        }

        final Crate crate = plugin.getCrateManager().getCrate(args[2]);
        if (crate == null) {
            player.sendMessage(Messages.getString("crateDoesNotExist", true));
            return;
        }
        final Player receiver = Bukkit.getPlayer(args[1]);
        if (receiver == null) {
            player.sendMessage(Messages.getString("playerIsOffline", true));
            return;
        }


        int amount = 1;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (final NumberFormatException e) {
            player.sendMessage(Messages.getString("notANumber", true));
        }

        plugin.getCrateManager().giveCrate(player, crate, amount);
    }

    @Override
    public String getAction() {
        return "give";
    }
}
