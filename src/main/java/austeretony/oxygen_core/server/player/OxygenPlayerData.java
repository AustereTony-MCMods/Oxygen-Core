package austeretony.oxygen_core.server.player;

import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.util.nbt.NBTUtils;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class OxygenPlayerData extends AbstractPersistentData {

    private UUID playerUUID;
    private ActivityStatus status;

    public OxygenPlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        status = ActivityStatus.ONLINE;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return "core:player_data_server";
    }

    @Override
    public String getPath() {
        return OxygenServer.getDataFolder() + "/players/" + playerUUID + "/core/player_data.dat";
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setTag("player_uuid", NBTUtils.toNBTUUID(playerUUID));
        tagCompound.setByte("status_ordinal", (byte) status.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        playerUUID = NBTUtils.fromNBTUUID(tagCompound.getTag("player_uuid"));
        status = ActivityStatus.values()[tagCompound.getByte("status_ordinal")];
    }

    @Override
    public void reset() {}
}
