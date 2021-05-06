package austeretony.oxygen_core.server;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.event.OxygenPlayerEvent;
import austeretony.oxygen_core.server.player.OxygenPlayerData;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManagerServer {

    private final Map<UUID, OxygenPlayerData> playersMap = new HashMap<>();

    public void playerLoggedIn(UUID playerUUID) {
        OxygenPlayerData playerData = getPlayerData(playerUUID);
        if (playerData == null) {
            playerData = new OxygenPlayerData(playerUUID);
            playersMap.put(playerUUID, playerData);
            OxygenServer.loadPersistentData(playerData);
        }
    }

    @Nullable
    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return playersMap.get(playerUUID);
    }

    @Nullable
    public ActivityStatus getPlayerStatus(UUID playerUUID) {
        OxygenPlayerData playerData = getPlayerData(playerUUID);
        return playerData != null ? playerData.getStatus() : null;
    }

    public boolean setPlayerStatus(EntityPlayerMP playerMP, ActivityStatus status) {
        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        OxygenPlayerData playerData = getPlayerData(playerUUID);
        if (playerData != null) {
            PlayerSharedData sharedData = OxygenServer.getPlayerSharedData(playerUUID);
            if (sharedData != null) {
                sharedData.setValue(OxygenMain.SHARED_ACTIVITY_STATUS, status.ordinal());
                if (status == ActivityStatus.OFFLINE) {
                    sharedData.setValue(OxygenMain.SHARED_LAST_ACTIVITY_TIME, OxygenServer.getCurrentTimeMillis());
                }
            }

            ActivityStatus old = playerData.getStatus();
            playerData.setStatus(status);
            playerData.markChanged();
            MinecraftCommon.delegateToServerThread(
                    () -> {
                        MinecraftCommon.postEvent(new OxygenPlayerEvent.ChangedActivityStatus(playerMP, old, status));
                    });

            return true;
        }
        return false;
    }

    public void save() {
        for (OxygenPlayerData data : playersMap.values()) {
            if (data.isChanged()) {
                data.resetChangedMark();
                OxygenServer.savePersistentData(data);
            }
        }
    }
}
