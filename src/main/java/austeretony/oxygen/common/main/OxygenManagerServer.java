package austeretony.oxygen.common.main;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncPlayersData;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.reference.CommonReference;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class OxygenManagerServer {

    private OxygenIOServer oxygenIO;

    private OxygenThread ioThreadServer, routineThreadServer, networkThreadServer;

    private static TelemetryManager telemetryManager;

    private static PrivilegeManagerServer privilegeManagerServer;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private OxygenManagerServer() {}

    public static OxygenManagerServer create() {
        OxygenMain.OXYGEN_LOGGER.info("Created Oxygen server manager.");
        return new OxygenManagerServer();
    }

    public static OxygenManagerServer instance() {
        return OxygenMain.getOxygenManagerServer();
    }

    public void initIO(String worldFolder, int maxPlayers) {
        this.oxygenIO = OxygenIOServer.create(worldFolder, maxPlayers);
    }

    public OxygenIOServer getIO() {
        return this.oxygenIO;
    }

    public void createOxygenServerThreads() {
        this.ioThreadServer = new OxygenThread("Oxygen IO Server");
        this.routineThreadServer = new OxygenThread("Oxygen Routine Server");
        this.networkThreadServer = new OxygenThread("Oxygen Network Server");
        OxygenMain.OXYGEN_LOGGER.info("Starting IO server thread...");
        this.ioThreadServer.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Routine server thread...");
        this.routineThreadServer.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Network server thread...");
        this.networkThreadServer.start();
    }

    public void init(FMLInitializationEvent event) {
        if (OxygenConfig.TELEMETRY.getBooleanValue()) {
            telemetryManager = TelemetryManager.create();
            telemetryManager.startTelemetryThreads();
        }
        privilegeManagerServer = PrivilegeManagerServer.create();
        if (OxygenConfig.PRIVILEGES.getBooleanValue())
            CommonReference.registerEvent(new OxygenChatHandler());
    }

    public OxygenThread getIOServerThread() {
        return this.ioThreadServer;
    }

    public OxygenThread getProcessServerThread() {
        return this.routineThreadServer;
    }

    public OxygenThread getSyncServerThread() {
        return this.networkThreadServer;
    }

    public void addIOTaskServer(IOxygenTask task) {
        this.ioThreadServer.addTask(task);
    }

    public void addRoutineTaskServer(IOxygenTask task) {
        this.routineThreadServer.addTask(task);
    }

    public void addNetworkTaskServer(IOxygenTask task) {
        this.networkThreadServer.addTask(task);
    }

    public static TelemetryManager getTelemetryManager() {
        return telemetryManager;
    }

    public static PrivilegeManagerServer getPrivilegeManagerServer() {
        return privilegeManagerServer;
    }

    public Map<UUID, OxygenPlayerData> getPlayersData() {
        return this.playersData;
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public void syncPlayersData(EntityPlayer player, int... identifiers) {
        OxygenMain.network().sendTo(new CPSyncPlayersData(identifiers), (EntityPlayerMP) player);
    }

    //TODO onPlayerLoggedIn()
    public void onPlayerLoggedIn(EntityPlayer player) {
        UUID playerUUID = OxygenHelperServer.uuid(player);
        this.getPlayersData().put(playerUUID, new OxygenPlayerData(playerUUID, OxygenHelperServer.username(player), PrivilegeProviderServer.getPlayerGroup(playerUUID).getTitle(), player.dimension));
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncMainData(), (EntityPlayerMP) player);
    }

    //TODO onPlayerLoggedOut()
    public void onPlayerLoggedOut(EntityPlayer player) {
        UUID playerUUID = OxygenHelperServer.uuid(player);
        this.getPlayersData().remove(playerUUID);
    }

    //TODO onPlayerChangedDimension()
    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = OxygenHelperServer.uuid(player);
        this.getPlayerData(playerUUID).setDimension(currDimension);
    }
}
