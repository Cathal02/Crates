package org.cathal02.listeners;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;
import org.cathal02.crates.CrateOpener;
import org.cathal02.utils.Messages;

public class CrateOpenListener implements Listener {

    private final CrateOpener crateOpener;
    private final Crates plugin;

    public CrateOpenListener(final Crates plugin) {
        crateOpener = new CrateOpener(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrateOpen(final PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!NBTEditor.contains(e.getItem(), "crate")) return;

        final String crateName = NBTEditor.getString(e.getItem(), "crate");

        final Crate crate = plugin.getCrateManager().getCrate(crateName);
        if (crate == null) return;

        e.setCancelled(true);

        final long coolDown = plugin.getCrateTimer().hasCoolDown(e.getPlayer());
        if (coolDown > 0) {
            e.getPlayer().sendMessage(Messages.getCoolDownMessage(coolDown));
            return;
        }

        if (plugin.getCrateTimer().hasHoursRemaining(e.getPlayer())) {
            e.getPlayer().sendMessage(Messages.getString("dailyLimitReached", true));
            return;
        }
        crateOpener.openCrate(e.getPlayer(), crate);
    }
}
