package org.cathal02.crates.events;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cathal02.crates.Crate;

public class CrateOpenEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Crate crate;
    private final Player player;

    public CrateOpenEvent(final Crate crate, final Player player) {
        this.crate = crate;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Crate getCrate() {
        return crate;
    }

    public Player getPlayer() {
        return player;
    }

}
