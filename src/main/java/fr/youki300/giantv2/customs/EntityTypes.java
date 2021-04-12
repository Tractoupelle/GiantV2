package fr.youki300.giantv2.customs;

import fr.youki300.giantv2.GiantPlugin;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

import static fr.youki300.giantv2.customs.CustomGiant.getPrivateField;

public enum EntityTypes {

    CUSTOM_GIANT(GiantPlugin.getInstance().getGiant().getName(), 53, fr.youki300.giantv2.customs.CustomGiant.class);

    private EntityTypes(String name, int id, Class<? extends Entity> custom)
    {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {

        Giant craftGiant = (Giant) entity.getBukkitEntity();

        ((LivingEntity) craftGiant).setRemoveWhenFarAway(false);


        entity.setCustomName(GiantPlugin.getInstance().getGiant().getName());
        craftGiant.setMaxHealth(GiantPlugin.getInstance().getGiant().getHealth());
        craftGiant.setHealth(GiantPlugin.getInstance().getGiant().getHealth());
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    private static void addToMaps(Class clazz, String name, int id) {

        ((Map)getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));

    }

}
