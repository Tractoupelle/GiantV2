package fr.youki300.giantv2.listeners;

import fr.youki300.giantv2.GiantPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class GiantBypass implements Listener {

    private final GiantPlugin giantPlugin;

    public GiantBypass(GiantPlugin giantPlugin){
        this.giantPlugin = giantPlugin;

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawnEvent(EntitySpawnEvent event) {

        if(event.getEntity().getName().equalsIgnoreCase(giantPlugin.getGiant().getName()) && event.isCancelled()) {
            event.setCancelled(false);

        }
    }
}