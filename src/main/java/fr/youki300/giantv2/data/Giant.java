package fr.youki300.giantv2.data;

import fr.youki300.giantv2.customs.CustomGiant;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Giant {

    private String name;
    private int health;
    private Location location;
    private int actualHealth;
    private Location actualLocation;
    private CustomGiant customGiant;
    private HashMap<Player, Double> damageToGiant = new HashMap<>();

    public Giant(String name, int health, Location location) {

        this.name = name;
        this.health = health;
        this.location = location;

    }

    public HashMap<Player, Double> getDamageToGiant() {
        return damageToGiant;
    }

    public void setDamageToGiant(HashMap<Player, Double> damageToGiant) {
        this.damageToGiant = damageToGiant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getActualHealth() {
        return actualHealth;
    }

    public void setActualHealth(int actualHealth) {
        this.actualHealth = actualHealth;
    }

    public Location getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(Location actualLocation) {
        this.actualLocation = actualLocation;
    }


    public Double getDamageFromPlayerToGiant(Player player) {

        if(damageToGiant.containsKey(player)){
            return damageToGiant.get(player);
        }

        return null;

    }

    public void addDamageFromPlayerToGiant(Player player, Double damage) {

        if(damageToGiant.containsKey(player)){

            double newDamage = damageToGiant.get(player) + damage;

            damageToGiant.replace(player, newDamage);

        } else {

            damageToGiant.put(player, damage);

        }

    }

    public void setCustomGiant(CustomGiant customGiant) {
        this.customGiant = customGiant;
    }

    public CustomGiant getCustomGiant() {
        return customGiant;
    }
}
