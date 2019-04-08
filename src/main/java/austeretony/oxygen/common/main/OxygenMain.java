package austeretony.oxygen.common.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.handler.OxygenKeyHandler;
import austeretony.oxygen.client.handler.OxygenOverlayHandler;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.events.OxygenEvents;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncPlayersData;
import austeretony.oxygen.common.network.server.SPGroupSyncRequest;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.reference.CommonReference;
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
    NAME = "Oxygen Core", 
    VERSION = "0.2.0", 
    VERSION_CUSTOM = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + "-Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + "-Privilege");

    private static OxygenNetwork network;

    public static final int OXYGEN_MOD_INDEX = 0;

    public static final DateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenHelperServer.registerConfig(new OxygenConfig());
        OxygenHelperServer.registerConfig(new OxygenTelemetryConfig());
        OxygenHelperServer.registerConfig(new OxygenPrivilegeConfig());
        ConfigLoader.loadConfigs();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.initNetwork();
        OxygenManagerServer.create();
        OxygenManagerServer.instance().createOxygenServerThreads();
        OxygenManagerServer.instance().init(event);
        CommonReference.registerEvent(new OxygenEvents());
        if (event.getSide() == Side.CLIENT) {
            CommonReference.registerEvent(new OxygenKeyHandler());
            CommonReference.registerEvent(new OxygenOverlayHandler());
            OxygenManagerClient.create();
            OxygenHelperClient.registerChatMessageInfoListener(new ChatMessagesInfoListener());
            GUISettings.create();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        OXYGEN_LOGGER.info("Initializing IO for world: {}.", worldName);
        OxygenManagerServer.instance().initIO(worldFolder, event.getServer().getMaxPlayers());
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

        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncGroup.class);
        network.registerPacket(CPSyncPlayersData.class);
        network.registerPacket(CPShowMessage.class);
        network.registerPacket(CPSyncNotification.class);

        network.registerPacket(SPGroupSyncRequest.class);
        network.registerPacket(SPRequestReply.class);
    }
}
