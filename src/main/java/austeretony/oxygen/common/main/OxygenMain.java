package austeretony.oxygen.common.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.ListenersRegistryClient;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.handler.OxygenKeyHandler;
import austeretony.oxygen.client.handler.OxygenOverlayHandler;
import austeretony.oxygen.client.listener.OxygenListenerClient;
import austeretony.oxygen.common.ListenersRegistryServer;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.events.OxygenEvents;
import austeretony.oxygen.common.listener.OxygenListenerServer;
import austeretony.oxygen.common.network.client.CPCommand;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncFriendListEntries;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncValidFriendEntriesIds;
import austeretony.oxygen.common.network.server.SPChangeStatus;
import austeretony.oxygen.common.network.server.SPEditFriendListEntryNote;
import austeretony.oxygen.common.network.server.SPGroupSyncRequest;
import austeretony.oxygen.common.network.server.SPManageFriendList;
import austeretony.oxygen.common.network.server.SPRequest;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.network.server.SPSendAbsentFriendListEntriesIds;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;
import net.minecraft.util.ResourceLocation;
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
    VERSION = "0.3.0", 
    VERSION_CUSTOM = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + "-Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + "-Privilege");

    private static OxygenNetwork network;

    public static final int 
    OXYGEN_MOD_INDEX = 0,
    FRIEND_REQUEST_ID = 0,
    STATUS_DATA_ID = 0;

    public static final DateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    static {
        ListenersRegistryServer.create();
        ListenersRegistryClient.create();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenHelperServer.registerConfig(new OxygenConfig());
        OxygenHelperServer.registerConfig(new OxygenTelemetryConfig());
        OxygenHelperServer.registerConfig(new OxygenPrivilegeConfig());
        ConfigLoader.loadConfigs();
        if (event.getSide() == Side.CLIENT)
            GUISettings.create();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.initNetwork();
        OxygenManagerServer.create();
        OxygenManagerServer.instance().createOxygenServerThreads();
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().startTelemetryThreads();
        CommonReference.registerEvent(new OxygenEvents());

        OxygenListenerServer listenerServer = new OxygenListenerServer();
        OxygenHelperServer.registerPlayerLogInListener(listenerServer);
        OxygenHelperServer.registerPlayerLogOutListener(listenerServer);

        if (event.getSide() == Side.CLIENT) {
            CommonReference.registerEvent(new OxygenKeyHandler());
            CommonReference.registerEvent(new OxygenOverlayHandler());
            OxygenManagerClient.create();

            OxygenListenerClient listenerClient = new OxygenListenerClient();
            OxygenHelperClient.registerChatMessageInfoListener(listenerClient);

            OxygenHelperClient.registerNotificationIcon(FRIEND_REQUEST_ID, new ResourceLocation(MODID, "textures/gui/invitation_request_icon.png"));
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        OXYGEN_LOGGER.info("Initializing IO for world: {}.", worldName);
        OxygenManagerServer.instance().getLoader().createOrLoadWorldIdDelegated(worldFolder, event.getServer().getMaxPlayers());
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().initIO();
        OxygenManagerServer.instance().getPrivilegeManager().initIO();    
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            TelemetryIO.instance().forceSave();           
    }

    public static OxygenNetwork network() {
        return network;
    }

    private void initNetwork() {
        network = OxygenHelperServer.createNetworkHandler(MODID + ":core");

        network.registerPacket(CPCommand.class);
        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncSharedPlayersData.class);
        network.registerPacket(CPShowMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncValidFriendEntriesIds.class);
        network.registerPacket(CPSyncFriendListEntries.class);

        network.registerPacket(SPRequest.class);
        network.registerPacket(SPGroupSyncRequest.class);
        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPChangeStatus.class);
        network.registerPacket(SPSendAbsentFriendListEntriesIds.class);
        network.registerPacket(SPManageFriendList.class);
        network.registerPacket(SPEditFriendListEntryNote.class);
    }
}
