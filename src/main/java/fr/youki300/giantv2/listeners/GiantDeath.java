package fr.youki300.giantv2.listeners;

import fr.youki300.giantv2.Giant;
import fr.youki300.giantv2.GiantManager;
import fr.youki300.giantv2.GiantPlugin;
import fr.youki300.giantv2.api.event.GiantWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

public class GiantDeath implements Listener {

    public GiantPlugin giantPlugin;
    public Giant giant;
    public GiantManager giantManager;

    public GiantDeath(GiantPlugin giantPlugin, Giant giant, GiantManager giantManager) {
        this.giantPlugin = giantPlugin;
        this.giant = giant;
        this.giantManager = giantManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if(!(giantManager.isAlive())){ return; }

        if(event.getEntity().getType().equals(EntityType.GIANT) && event.getEntity().getName().equals(giant.getName())){

            Entity giant = event.getEntity();
            Player winner = event.getEntity().getKiller();

            if(giantPlugin.getMainConfig().getBoolean("FIREWORK")) {
                spawnFireworks(giant.getLocation(), giantPlugin.getMainConfig().getInt("AMOUNT"));
            }

            String message = giantPlugin.getMessageConfig().getString("PREFIX")
                    + giantPlugin.getMessageConfig().getString("KILL-WIN")
                    .replace("%winner%", (winner != null ? winner.getName() : "Aucun"))
                    .replace("%name%", giant.getName());

            Bukkit.broadcastMessage(message);

            Bukkit.getScheduler().cancelTasks(giantPlugin);
            giantManager.setAlive(false);
            giantManager.setInStart(false);
            giantManager.setStart(false);
            giantManager.setTimeBeforeSpawning(-1);

            giantManager.getForceWhenHealth().clear();
            giantManager.setForceWhenHealth(giantPlugin.getMainConfig().getIntegerList("FORCE.FORCE-WITH-HEALTH.FORCE-WHEN-HEALTH"));

            giantClassement(winner);

            GiantWinEvent giantWinEvent = new GiantWinEvent(winner, GiantPlugin.getInstance().getGiant().getDamageToGiant());
            Bukkit.getPluginManager().callEvent(giantWinEvent);

        }
    }

    public static void spawnFireworks(Location location, int amount) {
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void giantClassement(Player winner){
        if(giantPlugin.getMainConfig().getBoolean("DAMAGE-CLASSEMENT")) {

            int i = 0;

            Player firstDamager = null;
            double firstDamage = 0;
            Player secondDamager = null;
            double secondDamage = 0;
            Player thirstDamager = null;
            double thirstDamage = 0;

            //entriesSortedByValues(giant.getDamageToGiant());

            for (Map.Entry<Player, Double> str : entriesSortedByValues(giant.getDamageToGiant())) {

                i++;
                if (i > 3){
                    break;
                }

                if(i == 1){

                    firstDamager = str.getKey();
                    firstDamage = str.getValue();

                } else if (i == 2) {

                    secondDamager = str.getKey();
                    secondDamage = str.getValue();

                } else if (i == 3) {

                    thirstDamager = str.getKey();
                    thirstDamage = str.getValue();

                }
            }

            List<String> classementMessage = giantPlugin.getMessageConfig().getStringList("DAMAGER-CLASSEMENT-MESSAGE");
            final String noneDamager = giantPlugin.getMessageConfig().getString("DAMAGER-NONE");
            final String noneDamage = giantPlugin.getMessageConfig().getString("DAMAGE-NONE");
            final String prefix =  giantPlugin.getMessageConfig().getString("PREFIX");

            for(String classement : classementMessage){

                classement = classement.replace("%prefix%", prefix).replace("%firstName%" , (firstDamager == null ? noneDamager : firstDamager.getName())).replace("%firstDamage%", (firstDamager == null ? noneDamage : String.valueOf((int) firstDamage)));
                classement = classement.replace("%prefix%", prefix).replace("%secondName%" , (secondDamager == null ? noneDamager : secondDamager.getName())).replace("%secondtDamage%", (secondDamager == null ? noneDamage : String.valueOf((int) secondDamage)));
                classement = classement.replace("%prefix%", prefix).replace("%thirstName%" , (thirstDamager == null ? noneDamager : thirstDamager.getName())).replace("%thirstDamage%", (thirstDamager == null ? noneDamage : String.valueOf((int) thirstDamage)));

                Bukkit.broadcastMessage(classement);

            }

            if(giantPlugin.getMainConfig().getBoolean("REWARD-DAMAGER-CLASSEMENT")) {
                for (String cmd : giantPlugin.getMainConfig().getStringList("FIRST-DAMAGER-COMMAND")) {
                    if(firstDamager != null) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%firstWinner%", firstDamager.getName()));
                    }
                }

                for (String cmd : giantPlugin.getMainConfig().getStringList("SECOND-DAMAGER-COMMAND")) {
                    if(secondDamager != null){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%secondWinner%", secondDamager.getName()));
                    }
                }

                for (String cmd : giantPlugin.getMainConfig().getStringList("THIRST-DAMAGER-COMMAND")) {
                    if(thirstDamager != null) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%thirdWinner%", thirstDamager.getName()));
                    }
                }

            }

        }



        if(giantPlugin.getMainConfig().getBoolean("REWARD-KILLER")) {

            for (String cmd : giantPlugin.getMainConfig().getStringList("KILLER-COMMAND")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%winner%", winner.getName()));
            }
        }
    }

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        return sortedEntries;

    }

}
