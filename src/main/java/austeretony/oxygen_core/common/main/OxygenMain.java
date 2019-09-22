package austeretony.oxygen_core.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.OxygenStatusMessagesHandler;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenGUIHelper;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.client.command.CommandOxygenClient;
import austeretony.oxygen_core.client.command.CurrencyArgumentExecutorClient;
import austeretony.oxygen_core.client.command.GUIArgumentExecutor;
import austeretony.oxygen_core.client.config.OxygenConfigClient;
import austeretony.oxygen_core.client.event.OxygenEventsClient;
import austeretony.oxygen_core.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen_core.client.gui.overlay.OxygenOverlayHandler;
import austeretony.oxygen_core.client.gui.overlay.RequestOverlay;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.client.input.InteractKeyHandler;
import austeretony.oxygen_core.client.input.NotificationsMenuKeyHandler;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.network.Network;
import austeretony.oxygen_core.common.network.client.CPPlaySoundEvent;
import austeretony.oxygen_core.common.network.client.CPRequestPresetSync;
import austeretony.oxygen_core.common.network.client.CPShowChatMessage;
import austeretony.oxygen_core.common.network.client.CPShowStatusMessage;
import austeretony.oxygen_core.common.network.client.CPSyncConfigs;
import austeretony.oxygen_core.common.network.client.CPSyncDataFragment;
import austeretony.oxygen_core.common.network.client.CPSyncGroup;
import austeretony.oxygen_core.common.network.client.CPSyncMainData;
import austeretony.oxygen_core.common.network.client.CPSyncNotification;
import austeretony.oxygen_core.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen_core.common.network.client.CPSyncPreset;
import austeretony.oxygen_core.common.network.client.CPSyncPresetsVersions;
import austeretony.oxygen_core.common.network.client.CPSyncSharedData;
import austeretony.oxygen_core.common.network.client.CPSyncValidDataIds;
import austeretony.oxygen_core.common.network.client.CPSyncWatchedValue;
import austeretony.oxygen_core.common.network.server.SPAbsentDataIds;
import austeretony.oxygen_core.common.network.server.SPChangeActivityStatus;
import austeretony.oxygen_core.common.network.server.SPOxygenRequest;
import austeretony.oxygen_core.common.network.server.SPRequestReply;
import austeretony.oxygen_core.common.network.server.SPRequestSharedDataSync;
import austeretony.oxygen_core.common.network.server.SPStartDataSync;
import austeretony.oxygen_core.common.privilege.PrivilegeImpl;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import austeretony.oxygen_core.common.update.UpdateAdaptersManager;
import austeretony.oxygen_core.common.watcher.WatchedValue;
import austeretony.oxygen_core.server.OxygenCoinsInitializer;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.OxygenPlayerData;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import austeretony.oxygen_core.server.api.WatcherHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenWorldLoadedEvent;
import austeretony.oxygen_core.server.command.CommandOxygenServer;
import austeretony.oxygen_core.server.command.CurrencyArgumentExecutorServer;
import austeretony.oxygen_core.server.command.privilege.CommandPrivilege;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import austeretony.oxygen_core.server.config.PrivilegesConfig;
import austeretony.oxygen_core.server.event.OxygenEventsServer;
import austeretony.oxygen_core.server.item.ItemsBlackList;
import austeretony.oxygen_core.server.privilege.PrivilegesLoaderServer;
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
    MODID = "oxygen_core", 
    NAME = "Oxygen Core", 
    VERSION = "0.9.1", 
    VERSION_CUSTOM = VERSION + ":beta:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/mod_versions_forge.json";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private static Network network;

    /*
     * Indexes:
     * Core - 0
     * Teleportation - 1 
     * Groups - 2
     * Exchange - 3 
     * Merchants - 4 
     * Players List - 5 
     * Friends List - 6
     * Interaction - 7
     * Mail - 8
     * Chat - 9
     * Trade - 10
     */

    public static final int 
    OXYGEN_CORE_MOD_INDEX = 0,

    SIMPLE_NOTIFICATION_ID = 0,

    ACTIVITY_STATUS_SHARED_DATA_ID = 0,
    DIMENSION_SHARED_DATA_ID = 1,

    HIDE_REQUESTS_OVERLAY_SETTING_ID = 0,

    SYNC_PRESETS_REQUEST_ID = 0,
    SYNC_PRIVILEGED_GROUP_REQUEST_ID = 1,
    SYNC_SHARED_DATA_REQUEST_ID = 2,
    REQUEST_REPLY_REQUEST_ID = 3,
    CHANGE_ACTIVITY_STATUS_REQUEST_ID = 4;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigManager.create();
        OxygenHelperCommon.registerConfig(new OxygenConfigClient());
        OxygenHelperCommon.registerConfig(new OxygenConfigServer());
        OxygenHelperCommon.registerConfig(new PrivilegesConfig());
        if (event.getSide() == Side.CLIENT) {
            CommandOxygenClient.registerArgumentExecutor(new GUIArgumentExecutor("gui", true));
            CommandOxygenClient.registerArgumentExecutor(new CurrencyArgumentExecutorClient("currency", true));
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigManager.instance().loadConfigs();
        this.initNetwork();
        OxygenManagerServer.create();
        CommonReference.registerEvent(new OxygenSoundEffects());
        CommonReference.registerEvent(new OxygenEventsServer());
        OxygenHelperServer.registerSharedDataValue(ACTIVITY_STATUS_SHARED_DATA_ID, Byte.BYTES);
        OxygenHelperServer.registerSharedDataValue(DIMENSION_SHARED_DATA_ID, Integer.BYTES);
        WatcherHelperServer.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, Long.BYTES, new OxygenCoinsInitializer()));
        CommandOxygenServer.registerArgumentExecutor(new CurrencyArgumentExecutorServer("currency", true));
        RequestsFilterHelper.registerNetworkRequest(SYNC_PRESETS_REQUEST_ID, 120);
        RequestsFilterHelper.registerNetworkRequest(SYNC_PRIVILEGED_GROUP_REQUEST_ID, 120);
        RequestsFilterHelper.registerNetworkRequest(SYNC_SHARED_DATA_REQUEST_ID, 1);
        RequestsFilterHelper.registerNetworkRequest(REQUEST_REPLY_REQUEST_ID, 1);
        RequestsFilterHelper.registerNetworkRequest(CHANGE_ACTIVITY_STATUS_REQUEST_ID, 1);
        if (event.getSide() == Side.CLIENT) {     
            GUISettings.create();
            OxygenManagerClient.create();
            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));
            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenOverlayHandler());     
            CommonReference.registerEvent(new OxygenKeyHandler());
            if (!OxygenGUIHelper.isOxygenMenuEnabled())
                CommonReference.registerEvent(new NotificationsMenuKeyHandler());
            if (!OxygenConfigClient.INTERACT_WITH_RMB.getBooleanValue())
                CommonReference.registerEvent(new InteractKeyHandler());
            WatcherHelperClient.registerValue(new WatchedValue(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, Long.BYTES));
            OxygenHelperClient.registerClientSetting(HIDE_REQUESTS_OVERLAY_SETTING_ID);
            OxygenGUIHelper.registerOverlay(new RequestOverlay());
            OxygenGUIHelper.registerOxygenMenuEntry(NotificationsGUIScreen.NOTIFICATIONS_MENU_ENTRY);
            OxygenHelperClient.registerStatusMessagesHandler(new OxygenStatusMessagesHandler());
        }
        EnumOxygenPrivilege.register();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OxygenManagerServer.instance().getCurrencyManager().validateCurrencyProvider();
        ItemsBlackList.loadBlackLists();
        OxygenManagerServer.instance().getPresetsManager().loadPresets();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));
        CommonReference.registerCommand(event, new CommandOxygenServer("oxygens"));

        UpdateAdaptersManager.applyChanges();
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        LOGGER.info("Initializing world: {}.", worldName);
        OxygenManagerServer.instance().worldLoaded(worldFolder, event.getServer().getMaxPlayers());
        PrivilegesLoaderServer.loadPrivilegeData();

        LOGGER.info("Active currency provider: <{}>.", OxygenManagerServer.instance().getCurrencyManager().getCurrencyProvider().getName());

        MinecraftForge.EVENT_BUS.post(new OxygenWorldLoadedEvent());
    }

    public static void addDefaultPrivileges() {
        if (!PrivilegeProviderServer.getGroup(PrivilegedGroupImpl.OPERATORS_GROUP.groupName).hasPrivilege(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString())) {
            PrivilegeProviderServer.addPrivilege(PrivilegedGroupImpl.OPERATORS_GROUP.groupName, 
                    new PrivilegeImpl(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), true), true);
            LOGGER.info("Default <{}> group privileges added.", PrivilegedGroupImpl.OPERATORS_GROUP.groupName);
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) { 
        OxygenManagerServer.instance().worldUnloaded();
        if (event.getSide() == Side.SERVER)
            OxygenManagerServer.instance().getExecutionManager().shutdown();
    }

    private void initNetwork() {
        network = Network.createNetworkHandler(MODID);

        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncSharedData.class);
        network.registerPacket(CPShowChatMessage.class);
        network.registerPacket(CPShowStatusMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncObservedPlayersData.class);
        network.registerPacket(CPPlaySoundEvent.class);
        network.registerPacket(CPSyncValidDataIds.class);
        network.registerPacket(CPSyncDataFragment.class);
        network.registerPacket(CPSyncPresetsVersions.class);
        network.registerPacket(CPRequestPresetSync.class);
        network.registerPacket(CPSyncPreset.class);
        network.registerPacket(CPSyncWatchedValue.class);

        network.registerPacket(SPOxygenRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeActivityStatus.class);
        network.registerPacket(SPStartDataSync.class);
        network.registerPacket(SPAbsentDataIds.class);
        network.registerPacket(SPRequestSharedDataSync.class);
    }

    public static Network network() {
        return network;
    }
}
