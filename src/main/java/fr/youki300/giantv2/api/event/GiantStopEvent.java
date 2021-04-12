package fr.youki300.giantv2.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiantStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public String getReason() {
        return reason;
    }

    private final String reason;

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

    public GiantStopEvent(String reason){
        this.reason = reason;
    }

}
