package fr.youki300.giantv2.api;

import fr.youki300.giantv2.data.Giant;
import fr.youki300.giantv2.manager.GiantManager;
import fr.youki300.giantv2.GiantPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class GiantExpension extends PlaceholderExpansion {

    private final GiantPlugin giantPlugin;

    public GiantExpension(GiantPlugin giantPlugin) {
        this.giantPlugin = giantPlugin;
    }

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    //@Override
    //public boolean canRegister() {
    //    return true;
    //}

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "Tractopelle";
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "giantv2";
    }

    /**
     * This is the version of this expansion
     */

    @Override
    public String getVersion() {
        return "1.5";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {

        Giant giant = giantPlugin.getGiant();
        GiantManager giantManager = giantPlugin.getGiantManager();

        switch (identifier) {

            case "timer":

                if (giantManager.isStart() && !giantManager.isAlive()) {
                    return String.valueOf(giantManager.getTimeBeforeSpawningFormat());
                }

                break;

            case "actualhealth":

                if (giantManager.isStart() && giantManager.isAlive()) {
                    return String.valueOf(giant.getActualHealth());

                }

                break;
            case "maxhealth":

                if (giantManager.isStart() && giantManager.isAlive()) {
                    return String.valueOf(giant.getHealth());

                }

                break;
            case "actuallocx":

                if (giantManager.isStart() && giantManager.isAlive()) {
                    return String.valueOf((int) giant.getLocation().getX());

                }

                break;
            case "actuallocy":

                if (giantManager.isStart() && giantManager.isAlive()) {
                    return String.valueOf((int) giant.getLocation().getY());

                }

                break;
            case "actuallocz":

                if (giantManager.isStart() && giantManager.isAlive()) {
                    return String.valueOf((int) giant.getLocation().getZ());

                }

            case "firstdamager":

                if (giantManager.isStart() && giantManager.isAlive()) {

                    int i = 0;

                    if (giant.getDamageToGiant().isEmpty()) {
                        return "null";
                    }

                    for (Map.Entry<Player, Double> str : entriesSortedByValues(giant.getDamageToGiant())) {

                        i++;
                        if (i > 1) {
                            break;
                        }

                        if (i == 1) {

                            return str.getKey().getName();

                        }

                    }
                }

                break;
            case "firstdamage":

                if (giantManager.isStart() && giantManager.isAlive()) {

                    if (giant.getDamageToGiant().isEmpty()) {
                        return "null";
                    }

                    int ii = 0;

                    for (Map.Entry<Player, Double> str : entriesSortedByValues(giant.getDamageToGiant())) {

                        ii++;
                        if (ii > 1) {
                            break;
                        }

                        if (ii == 1) {

                            double damage = str.getValue();
                            DecimalFormat df = new DecimalFormat("#.#");

                            return String.valueOf(df.format(damage));

                        }
                    }
                }

                break;
        }


        return null;
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