package austeretony.oxygen_core.server;

import java.util.UUID;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncNotification;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegesProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenPlayerLoadedEvent;
import austeretony.oxygen_core.server.api.event.OxygenPlayerUnloadedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class PlayerDataManagerServer {

    private final OxygenManagerServer manager;

    public PlayerDataManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(playerUUID);
        if (playerData == null) {
            playerData = this.manager.getPlayerDataContainer().createPlayerData(playerUUID);
            OxygenHelperServer.loadPersistentData(playerData);              
        }
        this.manager.getSharedDataManager().createSharedDataEntry(playerMP);
        playerData.init();
        playerData.addTrackedEntity(playerUUID, true);
        MinecraftForge.EVENT_BUS.post(new OxygenPlayerLoadedEvent(playerMP));
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(playerUUID);
        if (playerData != null) {
            playerData.clearTrackedEntities();
            MinecraftForge.EVENT_BUS.post(new OxygenPlayerUnloadedEvent(playerMP));
            this.manager.getSharedDataManager().removeSharedDataEntry(playerUUID);
            //TODO 0.10 - Removing data may cause unsaved data loss (activity status, oxygen virtual currency balance)
            //this.manager.getPlayerDataContainer().removePlayerData(playerUUID);
        }
    }

    public void playerStartTracking(EntityPlayerMP playerMP, Entity target) {
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(CommonReference.getPersistentUUID(playerMP));
        if (playerData != null)
            playerData.addTrackedEntity(CommonReference.getPersistentUUID(target), false);
    }

    public void playerStopTracking(EntityPlayerMP playerMP, Entity target) {
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(CommonReference.getPersistentUUID(playerMP));
        if (playerData != null)
            playerData.removeTrackedEntity(CommonReference.getPersistentUUID(target), false);
    }

    public void addNotification(EntityPlayerMP playerMP, Notification notification) {
        if (notification.getType() == EnumNotification.REQUEST)
            this.manager.getPlayerDataContainer().getPlayerData(CommonReference.getPersistentUUID(playerMP)).addTemporaryProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), playerMP);
    }

    public void sendRequest(EntityPlayerMP sender, EntityPlayerMP target, Notification notification) {
        UUID 
        senderUUID = CommonReference.getPersistentUUID(sender),
        targetUUID = CommonReference.getPersistentUUID(target);
        OxygenPlayerData 
        senderData = this.manager.getPlayerDataContainer().getPlayerData(senderUUID),
        targetData = this.manager.getPlayerDataContainer().getPlayerData(targetUUID);
        if ((targetData.getActivityStatus() != EnumActivityStatus.OFFLINE || PrivilegesProviderServer.getAsBoolean(senderUUID, EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), false))
                && this.manager.getValidatorsManager().validateRequest(senderUUID, targetUUID)) {
            this.addNotification(target, notification);
            this.manager.sendStatusMessage(sender, EnumOxygenStatusMessage.REQUEST_SENT);
        } else
            this.manager.sendStatusMessage(sender, EnumOxygenStatusMessage.REQUEST_RESET);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.manager.getPlayerDataContainer().getPlayerData(CommonReference.getPersistentUUID(player)).processRequestReply(player, reply, id);
    }

    public void setActivityStatus(EntityPlayerMP playerMP, EnumActivityStatus status) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(playerUUID);
        if (status != playerData.getActivityStatus()) {
            playerData.setActivityStatus(status);
            playerData.setChanged(true);
            this.manager.getSharedDataManager().updateActivityStatus(playerMP, status);

            this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ACTIVITY_STATUS_CHANGED);
        }
    }

    protected void process() {
        OxygenHelperServer.addRoutineTask(()->{
            for (UUID playerUUID : this.manager.getSharedDataManager().getOnlinePlayersUUIDs())
                this.manager.getPlayerDataContainer().getPlayerData(playerUUID).process();
        });
    }
}
