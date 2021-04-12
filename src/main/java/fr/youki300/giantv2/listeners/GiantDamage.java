package fr.youki300.giantv2.listeners;

import fr.youki300.giantv2.Giant;
import fr.youki300.giantv2.GiantManager;
import fr.youki300.giantv2.GiantPlugin;
import fr.youki300.giantv2.api.event.GiantUnleashedEvent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Collection;

public class GiantDamage implements Listener {

    public GiantPlugin giantPlugin;
    public Giant giant;
    public GiantManager giantManager;

    public GiantDamage(GiantPlugin giantPlugin, Giant giant, GiantManager giantManager) {
        this.giantPlugin = giantPlugin;
        this.giant = giant;
        this.giantManager = giantManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!giantManager.isAlive()) {
            return;
        }

        Entity eventEntity = event.getEntity();

        if (eventEntity.getType().equals(EntityType.GIANT)) {
            if (eventEntity instanceof LivingEntity) {
                if (eventEntity.getName() != null && eventEntity.getName().equalsIgnoreCase(giant.getName())) {

                    double damage = event.getDamage();
                    int life = (int) ((LivingEntity) eventEntity).getHealth();
                    Player damager = Bukkit.getPlayerExact(event.getDamager().getName());

                    giant.setActualHealth(life);
                    giant.setActualLocation(eventEntity.getLocation());
                    giant.addDamageFromPlayerToGiant(damager, damage);

                    String message = giantPlugin.getMessageConfig().getString("PREFIX") + giantPlugin.getMessageConfig().getString("CURRENT-LIFE").replace("%life%", String.valueOf(life)).replace("%maxlife%", String.valueOf(giant.getHealth()));

                    for(Player players : Bukkit.getOnlinePlayers()){
                        sendActionBar(players, message);
                    }

                    if(giantManager.isForceWithHealth()){

                        int firstWhenHealth =  giantManager.getForceWhenHealth().get(0);

                        if(firstWhenHealth > life){

                            GiantUnleashedEvent giantUnleashedEvent = new GiantUnleashedEvent("Attack" + firstWhenHealth, giant.getActualLocation());
                            Bukkit.getPluginManager().callEvent(giantUnleashedEvent);

                            giantManager.getForceWhenHealth().remove(0);

                            BlockPosition blockPosition = giant.getCustomGiant().getChunkCoordinates();

                            Location location = new Location(
                                    giant.getCustomGiant().getWorld().getWorld(),
                                    blockPosition.getX(),
                                    blockPosition.getY(),
                                    blockPosition.getZ());

                            Collection<Entity> collection = location.getWorld().getNearbyEntities(location, 3, 3, 3);

                            double d1 = giantManager.getForceWithHealthX();
                            double d2 = giantManager.getForceWithHealthY();
                            double d3 = giantManager.getForceWithHealthZ();



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

                            if(giantManager.isfHdisplayMessage()) {

                                Bukkit.broadcastMessage(giantPlugin.getMessageConfig().getString("PREFIX") + giantPlugin.getMessageConfig().getString("GIANT-UNLEASHED")
                                        .replace("%name%", giant.getName()));

                            }
                        }
                    }
                }
            }

        } else if (eventEntity instanceof Player){

            if(!(giantPlugin.getMainConfig().getBoolean("LOSS-DURABILITY-BY-THE-GIANT"))) {

                if (event.getDamager().getType().equals(EntityType.GIANT)) {
                    if (event.getDamager().getName().equalsIgnoreCase(giant.getName())) {

                        Player damageTaker = (Player) eventEntity;

                        event.setCancelled(true);

                        if (damageTaker.getInventory().getArmorContents().length != 0) {
                            for (ItemStack armor : damageTaker.getInventory().getArmorContents()) {
                                armor.setDurability((armor.getDurability()));
                            }
                            damageTaker.updateInventory();
                        }

                    }
                }
            }
        }
    }
    public static void sendActionBar(Player p, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);

    }
}
