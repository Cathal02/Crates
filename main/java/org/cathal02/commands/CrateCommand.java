package org.cathal02.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cathal02.Crates;
import org.cathal02.commands.subcommands.*;
import org.cathal02.utils.Messages;

import java.util.ArrayList;
import java.util.List;

public class CrateCommand implements CommandExecutor {

    Crates crates;
    private final List<CrateSubCommand> subCommandList = new ArrayList<>();

    public CrateCommand(final Crates crates) {
        this.crates = crates;
        subCommandList.add(new CreateSubCommand());
        subCommandList.add(new GiveSubCommand());
        subCommandList.add(new DeleteSubCommand());
        subCommandList.add(new EditSubCommand());
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label,
                             final String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getString("mustBeAPlayer", true));
            return true;
        }

        final Player player = (Player) sender;

        if (args.length < 2) {
            Messages.sendHelpMessage(player);
            return true;
        }

        final String action = args[0];

        for (final CrateSubCommand cmd : subCommandList) {
            if (cmd.getAction().equalsIgnoreCase(action)) {
                cmd.handleCrateSubCommand(player, args, crates);
                break;
            }
        }

        return true;

    }


}

