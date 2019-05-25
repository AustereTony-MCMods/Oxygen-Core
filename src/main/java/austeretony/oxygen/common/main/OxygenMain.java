package austeretony.oxygen.common.main;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.OxygenStatWatcherManagerClient;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.api.StatWatcherHelperClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.event.OxygenEventsClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.interaction.executors.AddFriendInteractionExecutor;
import austeretony.oxygen.client.gui.interaction.executors.AddIgnoredActionExecutor;
import austeretony.oxygen.client.gui.overlay.InteractionGUIOverlay;
import austeretony.oxygen.client.gui.overlay.RequestGUIOverlay;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.OxygenStatWatcherManagerServer;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.StatWatcherHelperServer;
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
import austeretony.oxygen.common.network.client.CPOxygenCommand;
import austeretony.oxygen.common.network.client.CPPlaySoundEvent;
import austeretony.oxygen.common.network.client.CPRemoveSharedDataEntry;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncFriendListEntries;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersImmutableData;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncValidFriendEntriesIds;
import austeretony.oxygen.common.network.client.CPSyncWatchedStats;
import austeretony.oxygen.common.network.server.SPChangeStatus;
import austeretony.oxygen.common.network.server.SPEditFriendListEntryNote;
import austeretony.oxygen.common.network.server.SPManageFriendList;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.network.server.SPSendAbsentFriendListEntriesIds;
import austeretony.oxygen.common.privilege.api.Privilege;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;
import austeretony.oxygen.common.util.OxygenUtils;
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
    VERSION = "0.6.0", 
    VERSION_EXTENDED = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/mod_versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + ": Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + ": Privilege");

    private static OxygenNetwork network, statWatcherNetwork;

    public static final int 
    OXYGEN_MOD_INDEX = 0,

    SIMPLE_NOTIFICATION_ID = 0,
    ALERT_NOTIFICATION_ID = 1,
    FRIEND_REQUEST_ID = 2,

    STATUS_DATA_ID = 1,
    DIMENSION_DATA_ID = 2,

    PLAYER_LIST_SCREEN_ID = 1,
    FRIEND_LIST_SCREEN_ID = 2,
    IGNORE_LIST_SCREEN_ID = 3,
    INTERACTION_SCREEN_ID = 4,

    HIDE_REQUESTS_OVERLAY_SETTING = 1,
    FRIEND_REQUESTS_AUTO_ACCEPT_SETTING = 2,

    CURRENCY_GOLD_STAT_ID = 0;//stored as int

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenUtils.removePreviousData("oxygen", true);//TODO If 'true' previous version data for 'oxygen' module will be removed.

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

        OxygenStatWatcherManagerServer.create();

        CommonReference.registerEvent(new OxygenSoundEffects());
        CommonReference.registerEvent(new OxygenEventsServer());

        StatWatcherHelperServer.registerStat(new WatchedValue(0, Integer.BYTES, new CurrencyGoldStatInitializer()));

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().startTelemetryThreads();

        OxygenHelperServer.registerSharedDataIdentifierForScreen(PLAYER_LIST_SCREEN_ID, STATUS_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(PLAYER_LIST_SCREEN_ID, DIMENSION_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(FRIEND_LIST_SCREEN_ID, STATUS_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(FRIEND_LIST_SCREEN_ID, DIMENSION_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(INTERACTION_SCREEN_ID, STATUS_DATA_ID);

        if (event.getSide() == Side.CLIENT) {     
            GUISettings.create();

            OxygenManagerClient.create();//client manager
            OxygenManagerClient.instance().createOxygenClientThreads();

            OxygenStatWatcherManagerClient.create();

            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));

            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenKeyHandler());
            CommonReference.registerEvent(new InteractionGUIOverlay());
            CommonReference.registerEvent(new RequestGUIOverlay());     

            StatWatcherHelperClient.registerStat(new WatchedValue(0, Integer.BYTES));

            OxygenHelperClient.registerSharedDataBuffer(STATUS_DATA_ID, Byte.BYTES);
            OxygenHelperClient.registerSharedDataBuffer(DIMENSION_DATA_ID, Integer.BYTES);

            OxygenGUIHelper.registerScreenId(PLAYER_LIST_SCREEN_ID);
            OxygenGUIHelper.registerScreenId(FRIEND_LIST_SCREEN_ID);
            OxygenGUIHelper.registerScreenId(IGNORE_LIST_SCREEN_ID);
            OxygenGUIHelper.registerScreenId(INTERACTION_SCREEN_ID);

            OxygenGUIHelper.registerSharedDataListenerScreen(PLAYER_LIST_SCREEN_ID);
            OxygenGUIHelper.registerSharedDataListenerScreen(INTERACTION_SCREEN_ID);

            OxygenHelperClient.registerInteractionMenuAction(new AddFriendInteractionExecutor());
            OxygenHelperClient.registerInteractionMenuAction(new AddIgnoredActionExecutor());

            OxygenHelperClient.registerNotificationIcon(SIMPLE_NOTIFICATION_ID, OxygenGUITextures.SIMPLE_NOTIFICATION_ICON);
            OxygenHelperClient.registerNotificationIcon(ALERT_NOTIFICATION_ID, OxygenGUITextures.ALERT_NOTIFICATION_ICON);
            OxygenHelperClient.registerNotificationIcon(FRIEND_REQUEST_ID, OxygenGUITextures.REQUEST_ICON);

            OxygenHelperClient.registerClientSetting(HIDE_REQUESTS_OVERLAY_SETTING);
            OxygenHelperClient.registerClientSetting(FRIEND_REQUESTS_AUTO_ACCEPT_SETTING);

            if (OxygenConfig.DISABLE_TAB_OVERLAY.getBooleanValue()) {
                ClientReference.getGameSettings().keyBindPlayerList.setKeyCode(0);
                ClientReference.getGameSettings().keyBindings = ArrayUtils.remove(ClientReference.getGameSettings().keyBindings, 12); 
            }
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

        OxygenStatWatcherManagerServer.instance().reset();

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().initIO();

        OxygenManagerServer.instance().getPrivilegeLoader().loadPrivilegeDataDelegated();
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));

        CommonReference.registerCommand(event, new CommandOxygenServer("oxygens"));

        OxygenHelperServer.loadWorldDataDelegated(OxygenManagerServer.instance().getSharedDataManager());

        this.addDefaultPrivilegesDelegated();
    }

    //TODO Need better solution (queue or something).
    private void addDefaultPrivilegesDelegated() {
        OxygenHelperServer.addIOTask(new IOxygenTask() {//delayed to insure this will be done after privileges loaded from disc.

            @Override
            public void execute() {
                if (!PrivilegeProviderServer.getGroup(PrivilegedGroup.OPERATORS_GROUP.groupName).hasPrivilege(EnumOxygenPrivileges.PREVENT_IGNORE.toString())) {
                    PrivilegeProviderServer.addPrivileges(PrivilegedGroup.OPERATORS_GROUP.groupName, true, 
                            new Privilege(EnumOxygenPrivileges.PREVENT_IGNORE.toString(), true),
                            new Privilege(EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString(), true));
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
        OxygenHelperServer.saveWorldDataDelegated(OxygenManagerServer.instance().getSharedDataManager());

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
        return statWatcherNetwork;
    }

    private void initNetwork() {
        network = OxygenHelperServer.createNetworkHandler(MODID + ":core");

        network.registerPacket(CPOxygenCommand.class);
        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncSharedPlayersData.class);
        network.registerPacket(CPShowMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncValidFriendEntriesIds.class);
        network.registerPacket(CPSyncFriendListEntries.class);
        network.registerPacket(CPSyncPlayersImmutableData.class);
        network.registerPacket(CPRemoveSharedDataEntry.class);
        network.registerPacket(CPAddSharedDataEntry.class);
        network.registerPacket(CPSyncObservedPlayersData.class);
        network.registerPacket(CPCacheObservedData.class);
        network.registerPacket(CPPlaySoundEvent.class);

        network.registerPacket(SPOxygenRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeStatus.class);
        network.registerPacket(SPSendAbsentFriendListEntriesIds.class);
        network.registerPacket(SPManageFriendList.class);
        network.registerPacket(SPEditFriendListEntryNote.class);

        statWatcherNetwork = OxygenHelperServer.createNetworkHandler(MODID + ":stat_watcher");

        statWatcherNetwork.registerPacket(CPSyncWatchedStats.class);
    }
}
