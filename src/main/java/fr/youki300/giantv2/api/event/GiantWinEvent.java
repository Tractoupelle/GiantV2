package fr.youki300.giantv2.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GiantWinEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player killer;
    private final HashMap<Player, Double> damageToGiant;

    public GiantWinEvent(Player killer, HashMap<Player, Double> damageToGiant) {
        this.killer = killer;
        this.damageToGiant = damageToGiant;
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public Player getKiller() {
        return killer;
    }

    public HashMap<Player, Double> getDamageToGiant() {
        return damageToGiant;
    }

    public static HandlerList getHandlerList() { return HANDLERS; }

}