package fr.youki300.giantv2.customs;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.Location;

public class PathfinderGoalWalkToLoc extends PathfinderGoal {

    private double speed;

    private EntityInsentient entity;

    private Location loc;

    private Navigation navigation;

    public PathfinderGoalWalkToLoc(EntityInsentient entity, Location loc, double speed) {
        this.entity = entity;
        this.loc = loc;
        this.navigation = (Navigation) this.entity.getNavigation();
        this.speed = speed;
    }

    public boolean a() {
        return true;
    }

    public void c()
    {
        PathEntity pathEntity = this.navigation.a(loc.getX(), loc.getY(), loc.getZ());

        this.navigation.a(pathEntity, speed);
    }
}