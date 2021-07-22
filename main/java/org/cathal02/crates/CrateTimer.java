package org.cathal02.crates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cathal02.Crates;
import org.cathal02.crates.events.CrateOpenEvent;
import org.cathal02.utils.PlayerDataResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class CrateTimer implements Listener {

    private final Map<UUID, LocalDateTime> playerCrateCoolDowns;
    private final Map<UUID, List<LocalDateTime>> playerCrateOpenMap;
    private final int maxDailyCrateOpens;
    private final long crateOpenCooldown;
    private final Crates plugin;

    public CrateTimer(final Crates plugin) {
        playerCrateCoolDowns = new HashMap<>();
        playerCrateOpenMap = new HashMap<>();

        maxDailyCrateOpens = plugin.getConfig().getInt("maxDailyCrateOpens");
        crateOpenCooldown = plugin.getConfig().getLong("crateCooldown");

        this.plugin = plugin;
        loadData();
    }


    public long hasCoolDown(final Player player) {
        if (!playerCrateCoolDowns.containsKey(player.getUniqueId()) || player.hasPermission("admin.bypass")) return -1;

        final LocalDateTime time = playerCrateCoolDowns.get(player.getUniqueId());

        return ((crateOpenCooldown * 1000) - Duration.between(time, LocalDateTime.now()).toMillis()) / 1000;
    }

    public boolean hasHoursRemaining(final Player player) {
        if (!playerCrateOpenMap.containsKey(player.getUniqueId()) || player.hasPermission("admin.bypass")) return false;

        final List<LocalDateTime> timeToRemove = new ArrayList<>();
        int count = 0;

        for (final LocalDateTime time : playerCrateOpenMap.get(player.getUniqueId())) {
            if (Duration.between(time, LocalDateTime.now()).toHours() < 24) {
                count++;
            } else {
                timeToRemove.add(time);
            }
        }

        playerCrateOpenMap.get(player.getUniqueId()).removeAll(timeToRemove);

        return count >= maxDailyCrateOpens;

    }


    @EventHandler
    private void onCrateOpen(final CrateOpenEvent e) {

        if (!playerCrateOpenMap.containsKey(e.getPlayer().getUniqueId())) {
            playerCrateOpenMap.put(e.getPlayer().getUniqueId(), new ArrayList<>());
        }
        playerCrateOpenMap.get(e.getPlayer().getUniqueId()).add(LocalDateTime.now());

        playerCrateCoolDowns.put(e.getPlayer().getUniqueId(), LocalDateTime.now());
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent e) {
        final PlayerDataResponse coolDownTime = plugin.getSQLUtil().getPlayerCrateCooldown(e.getPlayer().getUniqueId());
        if (coolDownTime != null) {
            playerCrateCoolDowns.put(e.getPlayer().getUniqueId(), coolDownTime.getCrateCooldown());
        }

        final List<LocalDateTime> crateOpenTime = coolDownTime.getCrateOpens();
        if (crateOpenTime != null) {
            playerCrateOpenMap.put(e.getPlayer().getUniqueId(), crateOpenTime);
        }
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        plugin.getSQLUtil().update(uuid,
                playerCrateCoolDowns.get(uuid),
                playerCrateOpenMap.get(uuid));
    }

    public void onClose() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final LocalDateTime crateCooldown = playerCrateCoolDowns.get(player.getUniqueId());
            final List<LocalDateTime> localDateTimes = playerCrateOpenMap.get(player.getUniqueId());
            if (crateCooldown != null && localDateTimes != null) {
                plugin.getSQLUtil().update(player.getUniqueId(), crateCooldown, localDateTimes);
            }
        }
    }


    private void loadData() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final PlayerDataResponse response = plugin.getSQLUtil().getPlayerCrateCooldown(player.getUniqueId());
            if (response.getCrateCooldown() != null) {
                playerCrateCoolDowns.put(player.getUniqueId(), response.getCrateCooldown());
            }
            if (response.getCrateOpens().size() > 0) {
                playerCrateOpenMap.put(player.getUniqueId(), response.getCrateOpens());
            }
        }
    }
}
