package austeretony.oxygen.common.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.event.OxygenEventsClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.interaction.executors.AddFriendInteractionExecutor;
import austeretony.oxygen.client.gui.interaction.executors.AddIgnoredActionExecutor;
import austeretony.oxygen.client.gui.overlay.InteractionGUIOverlay;
import austeretony.oxygen.client.gui.overlay.RequestGUIOverlay;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.command.CommandOxygenClient;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.ClientReference;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.event.OxygenEventsServer;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPCacheObservedData;
import austeretony.oxygen.common.network.client.CPOxygenCommand;
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
    NAME = "Oxygen", 
    VERSION = "0.4.1", 
    VERSION_CUSTOM = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/mod_versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + "-Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + "-Privilege");

    private static OxygenNetwork network;

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
    INTERACTION_SCREEN_ID = 4;

    public static final DateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenHelperServer.registerConfig(new OxygenConfig());
        OxygenHelperServer.registerConfig(new OxygenTelemetryConfig());
        OxygenHelperServer.registerConfig(new OxygenPrivilegeConfig());

        if (event.getSide() == Side.CLIENT)
            GUISettings.create();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigLoader.loadConfigs();

        this.initNetwork();

        OxygenManagerServer.create();//server manager
        OxygenManagerServer.instance().createOxygenServerThreads();

        CommonReference.registerEvent(new OxygenSoundEffects());
        CommonReference.registerEvent(new OxygenEventsServer());

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().startTelemetryThreads();

        OxygenHelperServer.registerSharedDataIdentifierForScreen(PLAYER_LIST_SCREEN_ID, STATUS_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(PLAYER_LIST_SCREEN_ID, DIMENSION_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(FRIEND_LIST_SCREEN_ID, STATUS_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(FRIEND_LIST_SCREEN_ID, DIMENSION_DATA_ID);
        OxygenHelperServer.registerSharedDataIdentifierForScreen(INTERACTION_SCREEN_ID, STATUS_DATA_ID);

        if (event.getSide() == Side.CLIENT) {      
            OxygenManagerClient.create();//client manager
            OxygenManagerClient.instance().createOxygenClientThreads();

            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));

            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenKeyHandler());
            CommonReference.registerEvent(new InteractionGUIOverlay());
            CommonReference.registerEvent(new RequestGUIOverlay());     

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

            if (OxygenConfig.DISABLE_TAB_OVERLAY.getBooleanValue()) {
                ClientReference.getMinecraft().gameSettings.keyBindPlayerList.setKeyCode(0);
                ClientReference.getMinecraft().gameSettings.keyBindings = ArrayUtils.remove(ClientReference.getMinecraft().gameSettings.keyBindings, 12); 
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

        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().initIO();

        OxygenManagerServer.instance().getPrivilegeLoader().loadPrivilegeDataDelegated();
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));

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
                            new Privilege(EnumOxygenPrivileges.PREVENT_IGNORE.toString()),
                            new Privilege(EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString()));
                    PRIVILEGE_LOGGER.info("Added default operators group privileges.");
                }
            }
        });
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            TelemetryIO.instance().forceSave();   
        OxygenHelperServer.saveWorldDataDelegated(OxygenManagerServer.instance().getSharedDataManager());
    }

    public static OxygenNetwork network() {
        return network;
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

        network.registerPacket(SPOxygenRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeStatus.class);
        network.registerPacket(SPSendAbsentFriendListEntriesIds.class);
        network.registerPacket(SPManageFriendList.class);
        network.registerPacket(SPEditFriendListEntryNote.class);
    }
}
