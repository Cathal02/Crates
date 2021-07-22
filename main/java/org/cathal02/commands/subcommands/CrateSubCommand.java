package org.cathal02.commands.subcommands;

import org.bukkit.entity.Player;
import org.cathal02.Crates;

public abstract class CrateSubCommand {
    public abstract void handleCrateSubCommand(Player player, final String[] args,
                                               Crates plugin);

    public abstract String getAction();
}
