package fr.youki300.giantv2.api.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiantUnleashedEvent  extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private String reason;

    private final Location location;

    public GiantUnleashedEvent(String reason, Location location) {
        this.reason = reason;
        this.location = location;
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

    public String getReason() {
        return reason;
    }

    public Location getLocation() { return location; }

}