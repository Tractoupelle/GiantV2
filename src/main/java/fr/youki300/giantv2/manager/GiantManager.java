package fr.youki300.giantv2.manager;

import fr.youki300.giantv2.GiantPlugin;
import fr.youki300.giantv2.config.Config;

import java.util.ArrayList;
import java.util.List;

public class GiantManager {


    private int timeBeforeSpawning = -1;
    private boolean isAlive = false;
    private boolean inStart = false;
    private boolean isStart = false;
    private boolean isForceStopped = false;
    private List<Integer> forceWhenHealth;

    public GiantManager(List<Integer> forceWhenHealth) {
        this.forceWhenHealth = forceWhenHealth;

    }

    public boolean isForceStopped() {
        return isForceStopped;
    }

    public void setForceStopped(boolean forceStopped) {
        isForceStopped = forceStopped;
    }

    public int getTimeBeforeSpawning() {
        return timeBeforeSpawning;
    }

    public void setTimeBeforeSpawning(int timeBeforeSpawning) {
        this.timeBeforeSpawning = timeBeforeSpawning;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isInStart() {
        return inStart;
    }

    public void setInStart(boolean inStart) {
        this.inStart = inStart;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public List<Integer> getForceWhenHealth() {
        return forceWhenHealth;
    }

    public void setForceWhenHealth(List<Integer> forceWhenHealth) {
        this.forceWhenHealth = forceWhenHealth;
    }

    public String getTimeBeforeSpawningFormat () {

        int timeleft = this.timeBeforeSpawning;

        Config messageConfig = GiantPlugin.getInstance().getMessageConfig();
        String formattedTime;

        int remainder = timeleft % 3600, minutes = remainder / 60, seconds = remainder % 60;

        if (minutes == 0) {

            formattedTime = messageConfig.getString("GIANT-SPAWN-COUNTDOWN-TIME-SECOND").replace("%s%", String.valueOf(seconds));

        } else {

            formattedTime = messageConfig.getString("GIANT-SPAWN-COUNTDOWN-TIME-MINUTE").replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds));

        }

        return formattedTime;

    }
}
