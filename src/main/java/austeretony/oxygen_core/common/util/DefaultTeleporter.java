package austeretony.oxygen_core.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DefaultTeleporter extends Teleporter {

    private final WorldServer worldServer;

    private double xPos, yPos, zPos;

    public DefaultTeleporter(WorldServer worldServer, double x, double y, double z) {
        super(worldServer);
        this.worldServer = worldServer;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        this.worldServer.getBlockState(new BlockPos((int) this.xPos, (int) this.yPos, (int) this.zPos)); 
        entity.setPosition(this.xPos, this.yPos, this.zPos);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }
}
