package austeretony.oxygen.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.network.OxygenNetworkHandler;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncPlayersData;
import austeretony.oxygen.common.network.server.SPGroupSyncRequest;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.command.CommandPrivilege;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import austeretony.oxygen.common.reference.CommonReference;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    VERSION = "0.1.0", 
    VERSION_CUSTOM = VERSION + ":alpha:0",
    GAME_VERSION = "1.12.2",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/versions_forge.json";

    public static final Logger 
    OXYGEN_LOGGER = LogManager.getLogger(NAME),
    TELEMETRY_LOGGER = LogManager.getLogger(NAME + "-Telemetry"),
    PRIVILEGE_LOGGER = LogManager.getLogger(NAME + "-Privilege");

    private static OxygenManagerServer oxygenManagerServer;

    @SideOnly(Side.CLIENT)
    private static OxygenManagerClient oxygenManagerClient;

    private static OxygenNetworkHandler network;

    public static final int OXYGEN_MOD_INDEX = 0;

    static {
        OxygenHelperServer.loadConfig(CommonReference.getGameFolder() + "/config/oxygen/config.json", 
                "assets/oxygen/config.json", new OxygenConfig());
        if (OxygenConfig.TELEMETRY.getBooleanValue())
            OxygenHelperServer.loadConfig(CommonReference.getGameFolder() + "/config/oxygen/telemetry/telemetry.json", 
                    "assets/oxygen/telemetry.json", new OxygenTelemetryConfig());
        if (OxygenConfig.PRIVILEGES.getBooleanValue())
            OxygenHelperServer.loadConfig(CommonReference.getGameFolder() + "/config/oxygen/privilege/privilege.json", 
                    "assets/oxygen/privilege.json", new OxygenPrivilegeConfig());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.initNetwork();
        oxygenManagerServer = OxygenManagerServer.create();
        oxygenManagerServer.createOxygenServerThreads();
        oxygenManagerServer.init(event);
        CommonReference.registerEvent(new OxygenServerEvents());
        if (event.getSide() == Side.CLIENT) {
            oxygenManagerClient = OxygenManagerClient.create();
            OxygenHelperClient.registerChatMessageInfoListener(new OxygenChatMessagesListener());
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        OXYGEN_LOGGER.info("Initializing IO for world: {}.", worldName);
        oxygenManagerServer.initIO(worldFolder, event.getServer().getMaxPlayers());
        if (OxygenConfig.TELEMETRY.getBooleanValue())
            oxygenManagerServer.getTelemetryManager().initIO();
        oxygenManagerServer.getPrivilegeManagerServer().initIO();    
        if (OxygenConfig.PRIVILEGES.getBooleanValue())
            CommonReference.registerCommand(event, new CommandPrivilege("privilege"));
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (OxygenConfig.TELEMETRY.getBooleanValue())
            TelemetryIO.instance().forceSave();           
    }

    public static OxygenManagerServer getOxygenManagerServer() {
        return oxygenManagerServer;
    }

    @SideOnly(Side.CLIENT)
    public static OxygenManagerClient getOxygenManagerClient() {
        return oxygenManagerClient;
    }

    public static TelemetryManager getTelemetryManager() {
        return oxygenManagerServer.getTelemetryManager();
    }

    public static PrivilegeManagerServer getPrivilegeManagerServer() {
        return oxygenManagerServer.getPrivilegeManagerServer();
    }

    @SideOnly(Side.CLIENT)
    public static PrivilegeManagerClient getPrivilegeManagerClient() {
        return oxygenManagerClient.getPrivilegeManagerClient();
    }

    public static OxygenNetworkHandler network() {
        return network;
    }

    private void initNetwork() {
        network = OxygenHelperServer.createNetworkHandler(MODID + ":core");

        network.register(CPSyncConfigs.class);
        network.register(CPSyncMainData.class);
        network.register(CPSyncGroup.class);
        network.register(CPSyncPlayersData.class);
        network.register(CPShowMessage.class);

        network.register(SPGroupSyncRequest.class);
    }
}
