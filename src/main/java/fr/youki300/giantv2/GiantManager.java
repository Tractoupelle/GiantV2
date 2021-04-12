package fr.youki300.giantv2;

import fr.youki300.giantv2.config.Config;

import java.util.ArrayList;
import java.util.List;

public class GiantManager {

    private final boolean forceWithHealth;
    private final int forceWithHealthX;
    private final int forceWithHealthY;
    private final int forceWithHealthZ;
    private final int forceWithHealthDistance;

    private final boolean fHplaySound;
    private final boolean fHdisplayMessage;

    private List<Integer> forceWhenHealth;
    private int countTime;
    private int timeBeforeSpawning = -1;
    private boolean isAlive = false;
    private boolean inStart = false;
    private boolean isStart = false;
    private boolean force;
    private int forceX;
    private int forceY;
    private int forceZ;
    private int forceDistance;
    private int forceChance;
    private boolean fLplaySound;
    private List<String> countdownDisplays = new ArrayList<>();

    public GiantManager(int countTime,
                        boolean force, int forceX, int forceY, int forceZ, int forceDistance, int forceLuck, boolean fLplaySound,
                        boolean forceWithHealth, int forceWithHealthX, int forceWithHealthY, int forceWithHealthZ, int forceWithHealthDistance, List<Integer> forceWhenHealth,boolean fHdisplayMessage, boolean fHplaySound){
        this.countTime = countTime;

        this.force = force;
        this.forceX = forceX;
        this.forceY = forceY;
        this.forceZ = forceZ;
        this.forceChance = forceLuck;
        this.fLplaySound = fLplaySound;
        this.forceDistance = forceDistance;

        this.forceWithHealth = forceWithHealth;
        this.forceWithHealthX = forceWithHealthX;
        this.forceWithHealthY = forceWithHealthY;
        this.forceWithHealthZ = forceWithHealthZ;
        this.forceWithHealthDistance = forceWithHealthDistance;
        this.forceWhenHealth = forceWhenHealth;
        this.fHdisplayMessage = fHdisplayMessage;
        this.fHplaySound = fHplaySound;

    }

    public boolean isfHplaySound() {
        return fHplaySound;
    }

    public boolean isfHdisplayMessage() {
        return fHdisplayMessage;
    }

    public boolean isfLplaySound() {
        return fLplaySound;
    }

    public void setfLplaySound(boolean fLplaySound) {
        this.fLplaySound = fLplaySound;
    }

    public List<Integer> getForceWhenHealth() {
        return forceWhenHealth;
    }

    public int getForceChance() {
        return forceChance;
    }

    public void setForceChance(int forceChance) {
        this.forceChance = forceChance;
    }

    public boolean isForceWithHealth() {
        return forceWithHealth;
    }

    public int getForceWithHealthX() {
        return forceWithHealthX;
    }

    public int getForceWithHealthY() {
        return forceWithHealthY;
    }

    public int getForceWithHealthZ() {
        return forceWithHealthZ;
    }

    public int getForceWithHealthDistance() {
        return forceWithHealthDistance;
    }

    public boolean isForceAttack() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public int getForceX() {
        return forceX;
    }

    public void setForceX(int forceX) {
        this.forceX = forceX;
    }

    public int getForceY() {
        return forceY;
    }

    public void setForceY(int forceY) {
        this.forceY = forceY;
    }

    public int getForceZ() {
        return forceZ;
    }

    public void setForceZ(int forceZ) {
        this.forceZ = forceZ;
    }

    public int getForceDistance() {
        return forceDistance;
    }

    public void setForceDistance(int forceDistance) {
        this.forceDistance = forceDistance;
    }

    public int getCountTime() { return countTime; }

    public void setCountTime(int countTime) {
        this.countTime = countTime;
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

    public List<String> getCountdownDisplays() {
        return countdownDisplays;
    }

    public void setCountdownDisplays(List<String> countdownDisplays) {
        this.countdownDisplays = countdownDisplays;
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

    public void setForceWhenHealth(List<Integer> integerList) {
        this.forceWhenHealth = integerList;
    }
}
