package austeretony.oxygen_core.server.event;

import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OxygenEventsServer {

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            OxygenManagerServer.instance().serverTick();
        }
    }

    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        OxygenManagerServer.instance().playerLoggedIn((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        OxygenManagerServer.instance().playerLoggedOut((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        OxygenServer.updateSharedValue(MinecraftCommon.getEntityUUID(event.player), OxygenMain.SHARED_DIMENSION, event.toDim);
    }

    @SubscribeEvent
    public void onPlayerAttackedPlayer(AttackEntityEvent event) {
        if (!CoreConfig.ENABLE_PVP_MANAGER.asBoolean()) return;
        if (event.getEntityPlayer() instanceof EntityPlayerMP && event.getTarget() instanceof EntityPlayerMP) {
            EntityPlayerMP attacker = (EntityPlayerMP) event.getEntityPlayer();
            EntityPlayerMP victim = (EntityPlayerMP) event.getTarget();
            if (!OxygenManagerServer.instance().getPVPManager().canPlayerAttack(attacker, victim)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttackedByPlayer(LivingAttackEvent event) {
        if (!CoreConfig.ENABLE_PVP_MANAGER.asBoolean()) return;
        if (event.getEntityLiving() instanceof EntityPlayerMP
                && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            EntityPlayerMP attacker = (EntityPlayerMP) event.getSource().getTrueSource();
            EntityPlayerMP victim = (EntityPlayerMP) event.getEntityLiving();
            if (!OxygenManagerServer.instance().getPVPManager().canPlayerAttack(attacker, victim)) {
                event.setCanceled(true);
            }
        }
    }
}
