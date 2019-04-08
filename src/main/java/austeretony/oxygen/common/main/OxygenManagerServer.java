package austeretony.oxygen.common.main;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncPlayersData;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.PrivilegesChatHandler;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.reference.CommonReference;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private OxygenIOServer oxygenIO;

    private OxygenThread ioThreadServer, routineThreadServer, networkThreadServer;

    private TelemetryManager telemetryManager;

    private PrivilegeManagerServer privilegeManagerServer;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private final Map<Long, ITemporaryProcess> processes = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private boolean processesExist;

    private OxygenManagerServer() {}

    public static void create() {
        OxygenMain.OXYGEN_LOGGER.info("Created Oxygen server manager.");
        instance = new OxygenManagerServer();
    }

    public static OxygenManagerServer instance() {
        return instance;
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
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue()) {
            telemetryManager = TelemetryManager.create();
            telemetryManager.startTelemetryThreads();
        }
        privilegeManagerServer = PrivilegeManagerServer.create();
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerEvent(new PrivilegesChatHandler());
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

    public TelemetryManager getTelemetryManager() {
        return this.telemetryManager;
    }

    public PrivilegeManagerServer getPrivilegeManager() {
        return this.privilegeManagerServer;
    }

    public Map<UUID, OxygenPlayerData> getPlayersData() {
        return this.playersData;
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public void syncPlayersData(EntityPlayer player, int... identifiers) {
        OxygenMain.network().sendTo(new CPSyncPlayersData(CommonReference.isOpped(player), identifiers), (EntityPlayerMP) player);
    }

    public void addWorldProcess(ITemporaryProcess process) {
        this.processes.put(process.getId(), process);
    }

    public void addPlayerProcess(EntityPlayer player, ITemporaryProcess process) {
        this.playersData.get(CommonReference.uuid(player)).addProcess(process);
    }

    public void addNotification(EntityPlayer player, IOxygenNotification notification) {
        if (notification.getType() == EnumNotifications.REQUEST)
            this.playersData.get(CommonReference.uuid(player)).addProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), (EntityPlayerMP) player);
    }

    public void processPlayers() {
        for (OxygenPlayerData playerData : this.playersData.values())
            playerData.process();
    }

    public Map<Long, ITemporaryProcess> getProcesses() {
        return this.processes;
    }

    public void addProcess(ITemporaryProcess process) {
        this.processes.put(process.getId(), process);
        this.processesExist = true;
    }

    public boolean haveProcess(long processId) {
        return this.processes.containsKey(processId);
    }

    public ITemporaryProcess getProcess(long processId) {
        return this.processes.get(processId);
    }

    public void processWorld() {
        if (this.processesExist) {
            Iterator<ITemporaryProcess> iterator = this.processes.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.processesExist = this.processes.size() > 0;
                }
            }
        }
    }

    public void onPlayerLoggedIn(EntityPlayer player) {
        UUID playerUUID = CommonReference.uuid(player);
        this.getPlayersData().put(playerUUID, new OxygenPlayerData(playerUUID, CommonReference.username(player), PrivilegeProviderServer.getPlayerGroup(playerUUID).getTitle(), player.dimension));
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncMainData(), (EntityPlayerMP) player);
    }

    public void onPlayerLoggedOut(EntityPlayer player) {
        UUID playerUUID = CommonReference.uuid(player);
        this.getPlayersData().remove(playerUUID);
    }

    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = CommonReference.uuid(player);
        this.getPlayerData(playerUUID).setDimension(currDimension);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        UUID playerUUID = CommonReference.uuid(player);
        if (this.getPlayerData(playerUUID).haveProcess(id)) {
            switch (reply) {
            case ACCEPT:
                ((IOxygenNotification) this.getPlayerData(playerUUID).getProcess(id)).accepted(player);
                break;
            case REJECT:
                ((IOxygenNotification) this.getPlayerData(playerUUID).getProcess(id)).rejected(player);
                break;
            }
            this.getPlayerData(playerUUID).getProcesses().remove(id);
        }
    }
}
