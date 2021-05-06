package austeretony.oxygen_core.server.pvp;

import net.minecraft.entity.player.EntityPlayerMP;

@FunctionalInterface
public interface PVPValidator {

    boolean canPlayerAttack(EntityPlayerMP attacker, EntityPlayerMP victim);
}
