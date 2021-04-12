package fr.youki300.giantv2.task;

import fr.youki300.giantv2.Giant;
import fr.youki300.giantv2.GiantManager;
import fr.youki300.giantv2.api.event.GiantStartEvent;
import fr.youki300.giantv2.api.event.GiantUnleashedEvent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.Collection;
import java.util.Random;

public class AliveAttack extends BukkitRunnable {

    private final Giant giant;
    private final GiantManager giantManager;

    public AliveAttack (Giant giant, GiantManager giantManager){
        this.giantManager = giantManager;
        this.giant = giant;
    }

    @Override
    public void run() {

        if (giantManager.isForceAttack()) {

            int random = new Random().nextInt(giantManager.getForceChance());

            if (random == giantManager.getForceChance() / 2) {

                BlockPosition blockPosition = giant.getCustomGiant().getChunkCoordinates();

                Location location = new Location(
                        giant.getCustomGiant().getWorld().getWorld(),
                        blockPosition.getX(),
                        blockPosition.getY(),
                        blockPosition.getZ());

                GiantUnleashedEvent giantUnleashedEvent = new GiantUnleashedEvent("Luck attack", location);
                Bukkit.getPluginManager().callEvent(giantUnleashedEvent);

                Collection<Entity> collection = location.getWorld().getNearbyEntities(location, 3, 3, 3);

                double d1 = giantManager.getForceX();
                double d2 = giantManager.getForceY();
                double d3 = giantManager.getForceZ();

                collection.forEach(paramEntity -> {

                    if (paramEntity instanceof Player) {

                        Vector vector1 = location.toVector();
                        Vector vector2 = paramEntity.getLocation().toVector();

                        Vector vector3 = new Vector(-(vector1.getX() - vector2.getX()), -(vector1.getY() - vector2.getY()), -(vector1.getZ() - vector2.getZ()));
                        vector3.add(new Vector(d1, d2, d3));

                        paramEntity.setVelocity(vector3);

                        if(giantManager.isfHplaySound()) {

                            ((Player) paramEntity).playSound(paramEntity.getLocation(), Sound.ENDERDRAGON_WINGS, 10, 10);

                        }

                    }

                });
            }
        } else {
            this.cancel();
        }
    }
}
