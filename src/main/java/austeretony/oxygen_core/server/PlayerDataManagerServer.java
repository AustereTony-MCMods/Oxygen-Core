package austeretony.oxygen_core.server;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncNotification;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenPlayerLoadedEvent;
import austeretony.oxygen_core.server.api.event.OxygenPlayerUnloadedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class PlayerDataManagerServer {

    private final OxygenManagerServer manager;

    public PlayerDataManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void informPlayer(EntityPlayerMP playerMP, EnumOxygenStatusMessage status) {
        OxygenHelperServer.sendStatusMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, status.ordinal());
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (!this.manager.getPlayerDataContainer().isPlayerDataExist(playerUUID)) {
            OxygenPlayerData oxygenData = this.manager.getPlayerDataContainer().createPlayerData(playerUUID);
            OxygenHelperServer.loadPersistentData(oxygenData);              
            this.manager.getSharedDataManager().createSharedDataEntry(playerMP);
            this.manager.getWatcherManager().initWatcher(playerMP, playerUUID);   
            CommonReference.delegateToServerThread(()->MinecraftForge.EVENT_BUS.post(new OxygenPlayerLoadedEvent(playerMP)));
        }
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (this.manager.getPlayerDataContainer().isPlayerDataExist(playerUUID)) {
            MinecraftForge.EVENT_BUS.post(new OxygenPlayerUnloadedEvent(playerMP));
            this.manager.getSharedDataManager().removeSharedDataEntry(playerUUID);
            this.manager.getPlayerDataContainer().removePlayerData(playerUUID);
        }
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
        if ((targetData.getActivityStatus() != EnumActivityStatus.OFFLINE || PrivilegeProviderServer.getValue(senderUUID, EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), false))
                && this.manager.getRequestsManager().validateRequest(senderUUID, targetUUID)) {
            this.addNotification(target, notification);
            this.informPlayer(sender, EnumOxygenStatusMessage.REQUEST_SENT);
        } else
            this.informPlayer(sender, EnumOxygenStatusMessage.REQUEST_RESET);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.manager.getPlayerDataContainer().getPlayerData(CommonReference.getPersistentUUID(player)).processRequestReply(player, reply, id);
    }

    public void changeActivityStatus(EntityPlayerMP playerMP, EnumActivityStatus status) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.manager.getPlayerDataContainer().getPlayerData(playerUUID);
        if (status != playerData.getActivityStatus()) {
            playerData.setActivityStatus(status);
            playerData.setChanged(true);
            this.manager.getSharedDataManager().updateActivityStatus(playerUUID, status);
        }
    }

    protected void processRequests() {
        OxygenHelperServer.addRoutineTask(()->{
            for (OxygenPlayerData profile : this.manager.getPlayerDataContainer().getPlayersData())
                profile.runTemporaryProcesses();
        });
    }
}
