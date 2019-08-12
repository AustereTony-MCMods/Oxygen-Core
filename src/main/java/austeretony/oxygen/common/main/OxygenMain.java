package austeretony.oxygen.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.WatcherManagerClient;
import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.api.WatcherHelperClient;
import austeretony.oxygen.client.command.CommandOxygenClient;
import austeretony.oxygen.client.command.CurrencyArgumentExecutorClient;
import austeretony.oxygen.client.command.GUIArgumentExecutor;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.event.OxygenEventsClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen.client.gui.overlay.OxygenOverlayHandler;
import austeretony.oxygen.client.gui.overlay.RequestOverlay;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.InteractKeyHandler;
import austeretony.oxygen.client.input.NotificationsMenuKeyHandler;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.client.sync.gui.api.AdvancedGUIHandlerClient;
import austeretony.oxygen.client.sync.gui.api.ComplexGUIHandlerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.WatcherManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.WatcherHelperServer;
import austeretony.oxygen.common.api.event.OxygenWorldLoadedEvent;
import austeretony.oxygen.common.api.event.OxygenWorldUnloadedEvent;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.command.CommandOxygenServer;
import austeretony.oxygen.common.command.CurrencyArgumentExecutorServer;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.OxygenClientConfig;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.event.OxygenEventsServer;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPOpenOxygenScreen;
import austeretony.oxygen.common.network.client.CPPlaySoundEvent;
import austeretony.oxygen.common.network.client.CPPlayerLoggedOut;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersSharedData;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncWatchedValues;
import austeretony.oxygen.common.network.server.SPChangeActivityStatus;
import austeretony.oxygen.common.network.server.SPOpenOxygenScreen;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.privilege.api.Privilege;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderServer;
import austeretony.oxygen.common.sync.gui.api.AdvancedGUIHandlerServer;
import austeretony.oxygen.common.sync.gui.api.ComplexGUIHandlerServer;
import austeretony.oxygen.common.sync.gui.network.CPSyncSharedData;
import austeretony.oxygen.common.update.UpdateAdaptersManager;
import austeretony.oxygen.common.watcher.WatchedValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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
    VERSION = "0.8.2", 
    VERSION_CUSTOM = VERSION + ":beta:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/mod_versions_forge.json";

    public static final Logger OXYGEN_LOGGER = LogManager.getLogger(NAME);

    private static OxygenNetwork network, watcherNetwork;

    public static final int 
    OXYGEN_MOD_INDEX = 0,//Teleportation - 1, Groups - 2, Exchange - 3, Merchants - 4, Players List - 5, Friends List - 6, Interaction - 7, Mail - 8, Chat - 9 

    NOTIFICATIONS_MENU_SCREEN_ID = 0,

    SIMPLE_NOTIFICATION_ID = 0,
    ALERT_NOTIFICATION_ID = 1,

    ACTIVITY_STATUS_SHARED_DATA_ID = 1,
    DIMENSION_SHARED_DATA_ID = 2,

    HIDE_REQUESTS_OVERLAY_SETTING_ID = 1;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenHelperServer.registerConfig(new OxygenConfig());
        OxygenHelperServer.registerConfig(new OxygenClientConfig());
        OxygenHelperServer.registerConfig(new OxygenPrivilegeConfig());
        if (event.getSide() == Side.CLIENT) {
            CommandOxygenClient.registerArgumentExecutor(new GUIArgumentExecutor("gui", true));
            CommandOxygenClient.registerArgumentExecutor(new CurrencyArgumentExecutorClient("currency", true));
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigLoader.loadConfigs();
        this.initNetwork();
        OxygenManagerServer.create();
        OxygenManagerServer.instance().createOxygenServerThreads();
        WatcherManagerServer.create();
        CommonReference.registerEvent(new OxygenSoundEffects());
        CommonReference.registerEvent(new OxygenEventsServer());
        OxygenHelperServer.registerSharedDataValue(ACTIVITY_STATUS_SHARED_DATA_ID, Byte.BYTES);
        OxygenHelperServer.registerSharedDataValue(DIMENSION_SHARED_DATA_ID, Integer.BYTES);
        WatcherHelperServer.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, Integer.BYTES, new OxygenCoinsInitializer()));
        CommandOxygenServer.registerArgumentExecutor(new CurrencyArgumentExecutorServer("currency", true));
        if (event.getSide() == Side.CLIENT) {     
            GUISettings.create();
            OxygenManagerClient.create();
            OxygenManagerClient.instance().createOxygenClientThreads();
            WatcherManagerClient.create();
            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));
            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenOverlayHandler());     
            CommonReference.registerEvent(new OxygenKeyHandler());
            if (!OxygenGUIHelper.isOxygenMenuEnabled())
                CommonReference.registerEvent(new NotificationsMenuKeyHandler());
            if (!OxygenClientConfig.INTERACT_WITH_RMB.getBooleanValue())
                CommonReference.registerEvent(new InteractKeyHandler());
            WatcherHelperClient.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, Integer.BYTES));
            OxygenHelperClient.registerSharedDataValue(ACTIVITY_STATUS_SHARED_DATA_ID, Byte.BYTES);
            OxygenHelperClient.registerSharedDataValue(DIMENSION_SHARED_DATA_ID, Integer.BYTES);
            OxygenHelperClient.registerNotificationIcon(SIMPLE_NOTIFICATION_ID, OxygenGUITextures.NOTIFICATION_ICONS);
            OxygenHelperClient.registerNotificationIcon(ALERT_NOTIFICATION_ID, OxygenGUITextures.ALERT_ICONS);
            OxygenHelperClient.registerClientSetting(HIDE_REQUESTS_OVERLAY_SETTING_ID);
            OxygenGUIHelper.registerOverlay(new RequestOverlay());
            OxygenGUIHelper.registerOxygenMenuEntry(NotificationsGUIScreen.NOTIFICATIONS_MENU_ENTRY);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OxygenManagerServer.instance().validateCurrencyProvider();
        AdvancedGUIHandlerServer.init();
        ComplexGUIHandlerServer.init();
        if (event.getSide() == Side.CLIENT) {    
            AdvancedGUIHandlerClient.init();
            ComplexGUIHandlerClient.init();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        OXYGEN_LOGGER.info("Active currency provider: <{}>.", OxygenManagerServer.instance().getCurrencyProvider().getName());
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        OXYGEN_LOGGER.info("Initializing IO for world: {}.", worldName);
        OxygenManagerServer.instance().reset();
        OxygenManagerServer.instance().getLoader().createOrLoadWorldId(worldFolder, event.getServer().getMaxPlayers());
        UpdateAdaptersManager.applyChanges();
        WatcherManagerServer.instance().reset();
        PrivilegeLoaderServer.loadPrivilegeDataDelegated();
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));
        CommonReference.registerCommand(event, new CommandOxygenServer("oxygens"));
        OxygenManagerServer.instance().getSharedDataManager().load();
        MinecraftForge.EVENT_BUS.post(new OxygenWorldLoadedEvent());
    }

    public static void addDefaultPrivileges() {
        if (!PrivilegeProviderServer.getGroup(PrivilegedGroup.OPERATORS_GROUP.groupName).hasPrivilege(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString())) {
            PrivilegeProviderServer.addPrivilege(PrivilegedGroup.OPERATORS_GROUP.groupName, 
                    new Privilege(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), true), true);
            OXYGEN_LOGGER.info("Default <{}> group privileges added.", PrivilegedGroup.OPERATORS_GROUP.groupName);
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) { 
        //TODO Debug shared data i/o operations on server stop
        OxygenManagerServer.instance().getServiceIOThread().interrupt();//Prevent concurrent shared data i/o operations (probably writing) on server stop
        OxygenHelperServer.savePersistentData(OxygenManagerServer.instance().getSharedDataManager());

        MinecraftForge.EVENT_BUS.post(new OxygenWorldUnloadedEvent());
    }

    private void initNetwork() {
        network = OxygenHelperServer.createNetworkHandler(MODID);

        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncSharedPlayersData.class);
        network.registerPacket(CPSyncSharedData.class);
        network.registerPacket(CPShowMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncPlayersSharedData.class);
        network.registerPacket(CPPlayerLoggedOut.class);
        network.registerPacket(CPAddSharedDataEntry.class);
        network.registerPacket(CPSyncObservedPlayersData.class);
        network.registerPacket(CPPlaySoundEvent.class);
        network.registerPacket(CPOpenOxygenScreen.class);

        network.registerPacket(SPOxygenRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeActivityStatus.class);
        network.registerPacket(SPOpenOxygenScreen.class);

        watcherNetwork = OxygenHelperServer.createNetworkHandler(MODID + "_w");

        watcherNetwork.registerPacket(CPSyncWatchedValues.class);
    }

    public static OxygenNetwork network() {
        return network;
    }

    public static OxygenNetwork watcherNetwork() {
        return watcherNetwork;
    }
}
