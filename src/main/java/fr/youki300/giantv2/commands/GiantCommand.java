package fr.youki300.giantv2.commands;

import fr.youki300.giantv2.data.Giant;
import fr.youki300.giantv2.manager.GiantManager;
import fr.youki300.giantv2.GiantPlugin;
import fr.youki300.giantv2.api.event.GiantStartEvent;
import fr.youki300.giantv2.api.event.GiantStopEvent;
import fr.youki300.giantv2.config.Config;
import fr.youki300.giantv2.task.Start;
import fr.youki300.giantv2.utils.Logger;
import fr.youki300.giantv2.utils.command.GCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GiantCommand extends GCommand {

    private final GiantPlugin giantPlugin;
    private final Giant giant;
    private final GiantManager giantManager;
    private final String prefix;
    private final Config messageConfig;
    private final Config mainConfig;

    public GiantCommand(GiantPlugin giantPlugin, Giant giant, GiantManager giantManager, String prefix){
        super(giantPlugin, "giant", "GIANTV2.ADMIN", true);
        this.giantPlugin = giantPlugin;
        this.giant = giant;
        this.giantManager = giantManager;
        this.prefix = prefix;
        this.mainConfig = giantPlugin.getMainConfig();
        this.messageConfig = giantPlugin.getMessageConfig();
    }



    @Override
    public boolean execute(CommandSender commandSender, String[] args) {

        if (args.length != 1) {
            commandSender.sendMessage(prefix + "Help:");
            commandSender.sendMessage(prefix + "§e/giant info : §7Informations about the Giant.");
            commandSender.sendMessage(prefix + "§e/giant move : §7Move spawn location of Giant to your location.");
            commandSender.sendMessage(prefix + "§e/giant start : §7Spawn Giant with Delay.");
            commandSender.sendMessage(prefix + "§e/giant now : §7Instant spawn Giant without Delay.");
            commandSender.sendMessage(prefix + "§e/giant stop : §7Stop and Kill Giant.");
            return true;
        }

        final String arg0 = args[0];

        if (arg0.equalsIgnoreCase("info")) {

            infoGiant(commandSender, giant, giantManager);

        } else if (arg0.equalsIgnoreCase("stats")) {

            if (!(giantManager.isAlive())) {

                commandSender.sendMessage(prefix + messageConfig.getString("NOT-ALIVE"));
                Logger.info(messageConfig.getString("NOT-ALIVE"), Logger.LogType.ERROR);

            } else {

                statsGiant(commandSender, giant, giantManager);

            }


        } else if (arg0.equalsIgnoreCase("start")) {

            startGiant(commandSender);

        } else if (arg0.equalsIgnoreCase("now")) {

            if (!(giantManager.isAlive()) && giantManager.isInStart()) {
                giantManager.setTimeBeforeSpawning(3);
            } else if (!giantManager.isInStart()) {

                startGiant(commandSender);

                giantManager.setTimeBeforeSpawning(3);

            }

        } else if (arg0.equalsIgnoreCase("stop")) {

            stopGiant(commandSender);

        } else if (arg0.equalsIgnoreCase("move")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;

                giantPlugin.getLocationConfig().set("SPAWN-LOCATION.WORLD", player.getLocation().getWorld().getName());
                giantPlugin.getLocationConfig().set("SPAWN-LOCATION.X", (int) player.getLocation().getX());
                giantPlugin.getLocationConfig().set("SPAWN-LOCATION.Y", (int) player.getLocation().getY());
                giantPlugin.getLocationConfig().set("SPAWN-LOCATION.Z", (int) player.getLocation().getZ());

                giantPlugin.getLocationConfig().save();

                giant.setLocation(player.getLocation());

                player.sendMessage(giantPlugin.getMessageConfig().getString("PREFIX") + giantPlugin.getMessageConfig().getString("POSITION-SET"));


            }

        } else if (arg0.equalsIgnoreCase("reload")) {

            if (giantManager.isInStart()) {

                commandSender.sendMessage(prefix + (messageConfig.getString("RELOAD-ALIVE")));
                Logger.info(messageConfig.getString("RELOAD-ALIVE"), Logger.LogType.ERROR);

            } else {

                commandSender.sendMessage(prefix + messageConfig.getString("RELOAD-SUCCESS"));
                Logger.info(messageConfig.getString("RELOAD-SUCCESS"), Logger.LogType.ERROR);

                GiantPlugin.getInstance().initGiant(mainConfig);

            }

        }

        return false;
    }

    public void startGiant(CommandSender sender) {

        if (giantManager.isStart()) {

            if (giantManager.isAlive()) {

                sender.sendMessage(prefix + messageConfig.getString("ALREADY-START"));
                Logger.info(messageConfig.getString("ALREADY-START"), Logger.LogType.ERROR);

            } else if (giantManager.isInStart()) {

                sender.sendMessage(prefix + messageConfig.getString("COUNTDOWN-ALREADY-START"));
                Logger.info(messageConfig.getString("COUNTDOWN-ALREADY-START"), Logger.LogType.ERROR);

            }

        } else {

            new Start(giantPlugin).runTaskTimer(giantPlugin, 0L, 20L);

            giantPlugin.reloadGiant();

            GiantStartEvent giantStartEvent = new GiantStartEvent(giant.getLocation());
            Bukkit.getPluginManager().callEvent(giantStartEvent);

            giantManager.setInStart(true);
            giantManager.setStart(true);
            giantManager.setTimeBeforeSpawning(mainConfig.getInt("COUNTDOWN"));

        }
    }

    public void stopGiant(CommandSender sender) {

        if (giantManager.isAlive()) {

            for (Entity entity : Bukkit.getWorld(giant.getLocation().getWorld().getName()).getEntities()) {

                if (entity.getName().equals(giant.getName())) {
                    giantManager.setForceStopped(true);
                    ((org.bukkit.entity.Giant) entity).damage(giant.getHealth() * 4);
                    entity.setLastDamageCause(null);

                }

            }

            Bukkit.broadcastMessage(prefix + (messageConfig.getString("STOP")));
            Logger.info(messageConfig.getString("STOP"), Logger.LogType.SUCCESS);

            giantManager.setAlive(false);
            giantManager.setInStart(false);
            giantManager.setStart(false);

            giantManager.setTimeBeforeSpawning(-1);

            Bukkit.getScheduler().cancelTasks(giantPlugin);

            GiantStopEvent giantStopEvent = new GiantStopEvent("STOPPED BY " + sender.getName());
            Bukkit.getPluginManager().callEvent(giantStopEvent);


        } else if (giantManager.isInStart()) {

            Bukkit.getScheduler().cancelTasks(giantPlugin);

            Bukkit.broadcastMessage(prefix + (messageConfig.getString("STOP")));
            Logger.info((messageConfig.getString("STOP")), Logger.LogType.SUCCESS);

            giantManager.setAlive(false);
            giantManager.setInStart(false);
            giantManager.setStart(false);

            giantManager.setTimeBeforeSpawning(-1);

            Bukkit.getScheduler().cancelTasks(giantPlugin);

        } else if (!giantManager.isInStart()) {

            sender.sendMessage(prefix + (messageConfig.getString("NOT-START")));
            Logger.info((messageConfig.getString("NOT-START")), Logger.LogType.ERROR);
        }
    }

    public void infoGiant(CommandSender sender, Giant giant, GiantManager giantManager) {

        sender.sendMessage(prefix + "Informations:");
        sender.sendMessage(prefix + "§6» §eName : §7" + giant.getName());
        sender.sendMessage(prefix + "§6» §eTime before spawning : §7" + (giantManager.getTimeBeforeSpawning() == -1 ? "Not in start" : giantManager.getTimeBeforeSpawning()));
        sender.sendMessage(prefix + "§6» §eAlive : §7" + (giantManager.isAlive() ? "Alive" : "Not alive"));
        sender.sendMessage(prefix + "§6» §eHealth : §7" + giant.getHealth());
        sender.sendMessage(prefix + "§6» §eHealth actual : §7" + (giantManager.isAlive() ? String.valueOf(giant.getActualHealth()) : "Not alive"));
        sender.sendMessage(prefix + "§6» §eSpawn Location : §7World: "
                + giant.getLocation().getWorld().getName()
                + " X: " + (int) giant.getLocation().getX()
                + " Y: " + (int) giant.getLocation().getY()
                + " Z: " + (int) giant.getLocation().getZ());
        sender.sendMessage(prefix + "§6» §eLocation actual : §7" + (giantManager.isAlive() ? "§7World: "
                + giant.getActualLocation().getWorld().getName()
                + " X: " + (int) giant.getActualLocation().getX()
                + " Y: " + (int) giant.getActualLocation().getY()
                + " Z: " + (int) giant.getActualLocation().getZ() : "Not alive"));

    }


    private void statsGiant(CommandSender sender, Giant giant, GiantManager giantManager) {

        sender.sendMessage(prefix + "§6» §eHealth actual : §7" + (giantManager.isAlive() ? String.valueOf(giant.getActualHealth()) : "Not alive"));
        sender.sendMessage(prefix + "§6» §eLocation actual : §7" + (giantManager.isAlive() ? "§7World: "
                + giant.getActualLocation().getWorld().getName()
                + " X: " + (int) giant.getActualLocation().getX()
                + " Y: " + (int) giant.getActualLocation().getY()
                + " Z: " + (int) giant.getActualLocation().getZ() : "Not alive"));

    }
}