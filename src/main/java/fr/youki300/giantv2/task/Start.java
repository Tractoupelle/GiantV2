package fr.youki300.giantv2.task;

import fr.youki300.giantv2.data.Giant;
import fr.youki300.giantv2.manager.GiantManager;
import fr.youki300.giantv2.GiantPlugin;
import fr.youki300.giantv2.api.event.GiantSpawnEvent;
import fr.youki300.giantv2.customs.CustomGiant;
import fr.youki300.giantv2.customs.EntityTypes;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Start extends BukkitRunnable {

    private final GiantPlugin giantPlugin;
    private final Giant giant;
    private final GiantManager giantManager;
    private final String countdownMessage;

    public Start (GiantPlugin giantPlugin){
        this.giantPlugin = giantPlugin;
        giant = giantPlugin.getGiant();
        giantManager = giantPlugin.getGiantManager();
        countdownMessage = giantPlugin.getMessageConfig().getString("PREFIX") + giantPlugin.getMessageConfig().getString("GIANT-SPAWN-COUNTDOWN");
    }

    @Override
    public void run() {

        if(giantManager.getTimeBeforeSpawning() <= 0){

            spawnGiant();

            new AliveAttack(giantPlugin, giant, giantManager).runTaskTimerAsynchronously(giantPlugin, 20 , 20);

            GiantSpawnEvent giantSpawnEvent = new GiantSpawnEvent(giant.getLocation());
            Bukkit.getPluginManager().callEvent(giantSpawnEvent);

            this.cancel();

        } else {

            if(giantPlugin.getMainConfig().getStringList("COUNTDOWN-MESSAGE-IN-SECOND").contains(String.valueOf(giantManager.getTimeBeforeSpawning()))){

                String message = (countdownMessage.replace("%cooldown%", String.valueOf(giantManager.getTimeBeforeSpawningFormat()))
                        .replace("%name%", giant.getName()));

                for(Player pls : Bukkit.getOnlinePlayers()) {
                    sendActionBar(pls, message);
                }

                Bukkit.broadcastMessage(message);

            }

            giantManager.setTimeBeforeSpawning(giantManager.getTimeBeforeSpawning() - 1);

        }


    }

    public void spawnGiant(){

        giantManager.setInStart(false);
        giantManager.setAlive(true);
        giantManager.setTimeBeforeSpawning(-1);
        giant.setActualHealth(giant.getHealth());
        giant.setActualLocation(giant.getLocation());
        giant.getDamageToGiant().clear();

        if(!(giant.getLocation().getChunk().isLoaded())){
            giant.getLocation().getChunk().load();
        }

        String fullMessage = giantPlugin.getMessageConfig().getString("PREFIX") + giantPlugin.getMessageConfig().getString("SPAWN")
                .replace("%name%", giant.getName())
                .replace("%z%", String.valueOf((int) giant.getLocation().getZ()))
                .replace("%world%", String.valueOf(giant.getLocation().getWorld().getName()))
                .replace("%x%", String.valueOf((int) giant.getLocation().getX()))
                .replace("%y%", String.valueOf((int) giant.getLocation().getY()));

        for(Player pls : Bukkit.getOnlinePlayers()) {
            sendActionBar(pls, fullMessage);
        }

        Bukkit.broadcastMessage(fullMessage);

        CustomGiant customGiant = new CustomGiant(Bukkit.getWorld(giant.getLocation().getWorld().getName()));
        giant.setCustomGiant(customGiant);

        EntityTypes.spawnEntity(customGiant, giant.getLocation());

    }

    public static void sendActionBar(Player p, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);

    }
}
