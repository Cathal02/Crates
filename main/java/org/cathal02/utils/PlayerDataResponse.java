package org.cathal02.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataResponse {
    private final LocalDateTime crateCooldown;
    private final List<LocalDateTime> crateOpens;

    public PlayerDataResponse(final LocalDateTime crateCooldown, final List<LocalDateTime> crateOpens) {
        this.crateCooldown = crateCooldown;
        this.crateOpens = crateOpens;
    }

    public PlayerDataResponse() {
        crateCooldown = null;
        crateOpens = new ArrayList<>();
    }

    public LocalDateTime getCrateCooldown() {
        return crateCooldown;
    }

    public List<LocalDateTime> getCrateOpens() {
        return crateOpens;
    }
}
