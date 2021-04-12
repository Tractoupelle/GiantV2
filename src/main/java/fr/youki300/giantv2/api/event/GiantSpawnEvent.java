package fr.youki300.giantv2.api.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiantSpawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location location;

    public GiantSpawnEvent(Location location) {
        this.location = location;
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

    public Location getLocation() {
        return location;
    }
}
