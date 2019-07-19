package austeretony.oxygen.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.WatcherManagerClient;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.api.WatcherHelperClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.event.OxygenEventsClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.overlay.OxygenOverlayHandler;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.WatcherManagerServer;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.WatcherHelperServer;
import austeretony.oxygen.common.api.event.OxygenWorldLoadedEvent;
import austeretony.oxygen.common.api.event.OxygenWorldUnloadedEvent;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.command.CommandOxygenClient;
import austeretony.oxygen.common.command.CommandOxygenServer;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.event.OxygenEventsServer;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPCacheObservedData;
import austeretony.oxygen.common.network.client.CPPlaySoundEvent;
import austeretony.oxygen.common.network.client.CPRemoveSharedDataEntry;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersImmutableData;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncWatchedValues;
import austeretony.oxygen.common.network.server.SPChangeStatus;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.privilege.api.Privilege;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderServer;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;
import austeretony.oxygen.common.watcher.WatchedValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
        modid = OxygenMain.MODID, 
        name = OxygenMain.NAME, 
        version = OxygenMain.VERSION,
        certificateFingerprint = "@FINGERPRINT@",
        updateJSON = OxygenMain.VERSIONS_FORGE_URL)
public class OxygenMain {

    public static final String 
    MODID = "oxygen", 
    NAME = "Oxygen Core", 
    VERSION = "0.7.0", 
    VERSION_CUSTOM = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/mod_versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + ": Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + ": Privilege");

    private static OxygenNetwork network, watcherNetwork;

    public static final int 
    OXYGEN_MOD_INDEX = 0,//Teleportation - 1, Groups - 2, Exchange - 3, Merchants - 4, Players List - 5, Friends List - 6, Interaction - 7

    SIMPLE_NOTIFICATION_ID = 0,
    ALERT_NOTIFICATION_ID = 1,

    ACTIVITY_STATUS_SHARED_DATA_ID = 1,
    DIMENSION_SHARED_DATA_ID = 2,

    HIDE_REQUESTS_OVERLAY_SETTING = 1;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenHelperServer.registerConfig(new OxygenConfig());
        OxygenHelperServer.registerConfig(new OxygenTelemetryConfig());
        OxygenHelperServer.registerConfig(new OxygenPrivilegeConfig());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigLoader.loadConfigs();

        this.initNetwork();

        OxygenManagerServer.create();//server manager
        OxygenManagerServer.instance().createOxygenServerThreads();

        WatcherManagerServer.create();

        CommonReference.registerEvent(new OxygenSoundEffects());
        CommonReference.registerEvent(new OxygenEventsServer());

        OxygenHelperServer.registerSharedDataValue(ACTIVITY_STATUS_SHARED_DATA_ID, Byte.BYTES);
        OxygenHelperServer.registerSharedDataValue(DIMENSION_SHARED_DATA_ID, Integer.BYTES);

        WatcherHelperServer.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_GOLD_ID, Integer.BYTES, new CurrencyGoldInitializer()));

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().startTelemetryThreads();

        if (event.getSide() == Side.CLIENT) {     
            GUISettings.create();

            OxygenManagerClient.create();//client manager
            OxygenManagerClient.instance().createOxygenClientThreads();

            WatcherManagerClient.create();

            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));

            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenKeyHandler());
            CommonReference.registerEvent(new OxygenOverlayHandler());     

            WatcherHelperClient.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_GOLD_ID, Integer.BYTES));

            OxygenHelperClient.registerSharedDataValue(ACTIVITY_STATUS_SHARED_DATA_ID, Byte.BYTES);
            OxygenHelperClient.registerSharedDataValue(DIMENSION_SHARED_DATA_ID, Integer.BYTES);

            OxygenHelperClient.registerNotificationIcon(SIMPLE_NOTIFICATION_ID, OxygenGUITextures.SIMPLE_NOTIFICATION_ICON);
            OxygenHelperClient.registerNotificationIcon(ALERT_NOTIFICATION_ID, OxygenGUITextures.ALERT_NOTIFICATION_ICON);

            OxygenHelperClient.registerClientSetting(HIDE_REQUESTS_OVERLAY_SETTING);
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        OXYGEN_LOGGER.info("Initializing IO for world: {}.", worldName);
        OxygenManagerServer.instance().reset();
        OxygenManagerServer.instance().getLoader().createOrLoadWorldIdDelegated(worldFolder, event.getServer().getMaxPlayers());

        WatcherManagerServer.instance().reset();

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().initIO();

        PrivilegeLoaderServer.loadPrivilegeDataDelegated();
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));

        CommonReference.registerCommand(event, new CommandOxygenServer("oxygens"));

        OxygenHelperServer.loadPersistentDataDelegated(OxygenManagerServer.instance().getSharedDataManager());

        this.addDefaultPrivilegesDelegated();
    }

    //TODO Need better solution (queue or something).
    private void addDefaultPrivilegesDelegated() {
        OxygenHelperServer.addIOTask(new IOxygenTask() {//delayed to insure this will be done after privileges loaded from disc.

            @Override
            public void execute() {
                if (!PrivilegeProviderServer.getGroup(PrivilegedGroup.OPERATORS_GROUP.groupName).hasPrivilege(EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString())) {
                    PrivilegeProviderServer.addPrivilege(PrivilegedGroup.OPERATORS_GROUP.groupName, 
                            new Privilege(EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString(), true), true);
                    PRIVILEGE_LOGGER.info("Default <{}> group privileges added.", PrivilegedGroup.OPERATORS_GROUP.groupName);
                }

                MinecraftForge.EVENT_BUS.post(new OxygenWorldLoadedEvent());//posting here to make it fire after all data loaded
            }
        });
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            TelemetryIO.instance().forceSave();   
        OxygenHelperServer.savePersistentDataDelegated(OxygenManagerServer.instance().getSharedDataManager());

        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                MinecraftForge.EVENT_BUS.post(new OxygenWorldUnloadedEvent());//posting here to make it fire after all data saved
            }
        });
    }

    public static OxygenNetwork network() {
        return network;
    }

    public static OxygenNetwork watcherNetwork() {
        return watcherNetwork;
    }

    private void initNetwork() {
        network = OxygenHelperServer.createNetworkHandler(MODID);

        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncSharedPlayersData.class);
        network.registerPacket(CPShowMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncPlayersImmutableData.class);
        network.registerPacket(CPRemoveSharedDataEntry.class);
        network.registerPacket(CPAddSharedDataEntry.class);
        network.registerPacket(CPSyncObservedPlayersData.class);
        network.registerPacket(CPCacheObservedData.class);
        network.registerPacket(CPPlaySoundEvent.class);

        network.registerPacket(SPOxygenRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeStatus.class);

        watcherNetwork = OxygenHelperServer.createNetworkHandler(MODID + "_watcher");

        watcherNetwork.registerPacket(CPSyncWatchedValues.class);
    }
}
