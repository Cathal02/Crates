package org.cathal02.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.cathal02.gui.holders.CancelClickHolder;

public class CancelClickListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof CancelClickHolder){
            e.setCancelled(true);
        }
    }
}
