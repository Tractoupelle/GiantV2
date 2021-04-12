package fr.youki300.giantv2.config;

import fr.youki300.giantv2.GiantPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConfigManager {

    private GiantPlugin giantPlugin;
    private final FileConfiguration config;


    public ConfigManager(GiantPlugin giantPlugin) {
        this.giantPlugin = giantPlugin;
        this.config = giantPlugin.getConfig();
    }

    public String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', giantPlugin.getConfig().getString(path));
    }


    public List<String> getStringList(String path) {

        List<String> stringList = giantPlugin.getConfig().getStringList(path);
        ArrayList<String> toReturn = new ArrayList<>();

        stringList.forEach(line -> toReturn.add(ChatColor.translateAlternateColorCodes('&', line)));

        return toReturn;
    }

    public List<Integer> getIntegerList(String path) {

        List<String> stringList = giantPlugin.getConfig().getStringList(path);
        ArrayList<Integer> toReturn = new ArrayList<>();


        for(String string : stringList){
            if(isInteger(string)){
                toReturn.add(Integer.valueOf(string));
            }
        }

        toReturn.sort(Collections.reverseOrder());

        return toReturn;

    }

    public void setDouble(String path, double value) {
        config.set(path, value);
    }

    public void setInt(String path, int value) {
        config.set(path, value);
    }

    public void setString(String path, String value) {
        config.set(path, value);
    }



    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) { return config.getBoolean(path); }

    @SuppressWarnings("unused")
    private double getDouble(String path) {
        return config.getDouble(path);
    }

    public double getFloat(String path) {
        return config.getDouble(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void updateConfig() {

        giantPlugin.getConfig().options().copyDefaults(true);
        giantPlugin.saveConfig();
        giantPlugin.reloadConfig();

    }

    public boolean isInteger( String input ) { //Pass in string
        try { //Try to make the input into an integer
            Integer.parseInt( input );
            return true; //Return true if it works
        }
        catch( Exception e ) {
            return false; //If it doesn't work return false
        }
    }
}