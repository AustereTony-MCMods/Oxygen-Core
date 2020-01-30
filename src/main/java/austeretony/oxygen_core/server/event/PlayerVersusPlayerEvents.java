package austeretony.oxygen_core.server.event;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.server.OxygenManagerServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerVersusPlayerEvents {

    @SubscribeEvent
    public void onPlayerAttackedPlayer(AttackEntityEvent event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP 
                && event.getTarget() instanceof EntityPlayerMP) {
            event.setCanceled(!OxygenManagerServer.instance().getValidatorsManager().canPlayerAttack(
                    CommonReference.getPersistentUUID(event.getEntityPlayer()), 
                    CommonReference.getPersistentUUID(event.getTarget())));
        }
    }

    @SubscribeEvent
    public void onPlayerAttackedByPlayer(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP 
                && event.getSource().getTrueSource() != null 
                && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            event.setCanceled(!OxygenManagerServer.instance().getValidatorsManager().canPlayerAttack(
                    CommonReference.getPersistentUUID(event.getSource().getTrueSource()), 
                    CommonReference.getPersistentUUID(event.getEntityLiving())));
        }
    }
}
