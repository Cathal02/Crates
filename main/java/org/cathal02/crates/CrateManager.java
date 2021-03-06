package org.cathal02.crates;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cathal02.Crates;
import org.cathal02.utils.ItemBuilder;
import org.cathal02.utils.Messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrateManager {

    private List<Crate> crates = new ArrayList<>();


    public CrateManager(final Crates plugin) {
        crates = plugin.getCrateDataManager().getCrateData();
    }

    public boolean createCrate(final String name, final ItemStack item) {

        if (crates.stream().anyMatch(crate -> crate.getName().equalsIgnoreCase(name))) {
            return false;
        }
        crates.add(new Crate(name, item));
        return true;
    }

    public void deleteCrate(final Crate crate) {

    }

    public Crate getCrate(final String name) {
        return crates.stream().
                filter(crate -> crate.getName().equalsIgnoreCase(name)).
                findFirst().orElse(null);
    }

    public void giveCrate(final Player player, final Crate crate, final int amount) {
        ItemStack item =
                new ItemBuilder(crate.getCrateItem()).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "" + crate.getName()).toItemStack();
        item.setAmount(amount);
        item = NBTEditor.set(item, crate.getName(), "crate");

        player.getInventory().addItem(item);
        player.sendMessage(Messages.getString("crateReceived", true));
    }

    public Collection<Crate> getCrates() {
        return crates;
    }
}
