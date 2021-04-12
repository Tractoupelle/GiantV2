package fr.youki300.giantv2;

import fr.youki300.giantv2.api.GiantExpension;
import fr.youki300.giantv2.commands.GiantCommand;
import fr.youki300.giantv2.config.Config;
import fr.youki300.giantv2.listeners.GiantBypass;
import fr.youki300.giantv2.listeners.GiantDamage;
import fr.youki300.giantv2.listeners.GiantDeath;
import fr.youki300.giantv2.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class GiantPlugin extends JavaPlugin {

    private static GiantPlugin instance;
    private Config mainConfig;
    private Config locationConfig;
    private Config messageConfig;
    private Giant giant;
    private GiantManager giantManager;
    private final Logger log = new Logger(this.getDescription().getFullName());

    @Override
    public void onEnable() {

        instance = this;

        init();

    }

    @Override
    public void onDisable() {

        if (giantManager.isAlive()) {

            for (Entity entity : Bukkit.getWorld(giant.getLocation().getWorld().getName()).getEntities()) {

                if (entity.getName().equals(giant.getName())) {
                    ((org.bukkit.entity.Giant) entity).damage(giant.getHealth() * 4);
                    entity.setLastDamageCause(null);
                }

            }

            Bukkit.broadcastMessage(messageConfig.getString("PREFIX") + messageConfig.getString("STOP"));
            Logger.info(messageConfig.getString("STOP"), Logger.LogType.SUCCESS);

        }

    }

    public void init() {

        registerManager();

        initGiant(mainConfig);

        registerCommands();
        registerListeners();

        new GiantExpension(this).register();

        log.info("=======================================", Logger.LogType.SUCCESS);
        log.info(" Plugin initialization in progress...", Logger.LogType.SUCCESS);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            log.info(" Added PlaceHolderAPI dependency", Logger.LogType.SUCCESS);
        }

        log.info(" Author: Tractopelle#4020", Logger.LogType.SUCCESS);
        log.info("=======================================", Logger.LogType.SUCCESS);

    }

    public static GiantPlugin getInstance() {
        return instance;
    }

    public Giant getGiant() {
        return this.giant;
    }

    public Config getLocationConfig() { return this.locationConfig; }

    public Config getMessageConfig() { return this.messageConfig; }

    public Config getMainConfig() { return this.mainConfig; }

    public GiantManager getGiantManager() { return this.giantManager; }

    private void registerCommands() {
        new GiantCommand(this, giant, giantManager, messageConfig.getString("PREFIX"));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GiantDamage(this, giant, giantManager), this);
        pm.registerEvents(new GiantDeath(this, giant, giantManager), this);
        pm.registerEvents(new GiantBypass(this), this);
    }

    private void registerManager() {
        this.mainConfig = new Config(this, "config");
        this.locationConfig = new Config(this, "location");
        this.messageConfig = new Config(this, "message");
    }

    public void initGiant(Config mainConfig) {

        this.giantManager = new GiantManager(
                mainConfig.getInt("COUNTDOWN"),
                mainConfig.getBoolean("FORCE.FORCE-LUCK.USE-FORCE"),
                mainConfig.getInt("FORCE.FORCE-LUCK.FORCE-X"),
                mainConfig.getInt("FORCE.FORCE-LUCK.FORCE-Y"),
                mainConfig.getInt("FORCE.FORCE-LUCK.FORCE-Z"),
                mainConfig.getInt("FORCE.FORCE-LUCK.FORCE-DISTANCE"),
                mainConfig.getInt("FORCE.FORCE-LUCK.FORCE-LUCK"),
                mainConfig.getBoolean("FORCE.FORCE-LUCK.PLAY-SOUND"),

                mainConfig.getBoolean("FORCE.FORCE-WITH-HEALTH.USE-FORCE"),
                mainConfig.getInt("FORCE.FORCE-WITH-HEALTH.FORCE-X"),
                mainConfig.getInt("FORCE.FORCE-WITH-HEALTH.FORCE-Y"),
                mainConfig.getInt("FORCE.FORCE-WITH-HEALTH.FORCE-Z"),
                mainConfig.getInt("FORCE.FORCE-WITH-HEALTH.FORCE-DISTANCE"),
                mainConfig.getIntegerList("FORCE.FORCE-WITH-HEALTH.FORCE-WHEN-HEALTH"),
                mainConfig.getBoolean("FORCE.FORCE-WITH-HEALTH.FORCE-GIANT-UNLEASHED-MESSAGE"),
                mainConfig.getBoolean("FORCE.FORCE-WITH-HEALTH.PLAY-SOUND"));


        this.giant = new Giant(

                mainConfig.getString("NAME"),
                mainConfig.getInt("HEALTH"),
                new Location(Bukkit.getWorld(locationConfig.getString("SPAWN-LOCATION.WORLD")),
                        locationConfig.getInt("SPAWN-LOCATION.X"),
                        locationConfig.getInt("SPAWN-LOCATION.Y"),
                        locationConfig.getInt("SPAWN-LOCATION.Z"))
        );

        giantManager.setCountdownDisplays(mainConfig.getStringList("COUNTDOWN-MESSAGE-IN-SECOND"));
        giantManager.getForceWhenHealth().sort(Collections.reverseOrder());


    }

}
