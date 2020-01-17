package austeretony.oxygen_core.common.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.OxygenStatusMessagesHandler;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenGUIHelper;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.OxygenOverlayHandler;
import austeretony.oxygen_core.client.command.CommandOxygenClient;
import austeretony.oxygen_core.client.command.CoreArgumentClient;
import austeretony.oxygen_core.client.currency.OxygenCoinsCurrencyProperties;
import austeretony.oxygen_core.client.currency.OxygenShardsCurrencyProperties;
import austeretony.oxygen_core.client.currency.OxygenVouchersCurrencyProperties;
import austeretony.oxygen_core.client.event.OxygenEventsClient;
import austeretony.oxygen_core.client.gui.notifications.NotificationsScreen;
import austeretony.oxygen_core.client.gui.overlay.RequestOverlay;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.client.gui.settings.BaseSettingsContainer;
import austeretony.oxygen_core.client.gui.settings.CoreSettingsContainer;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;
import austeretony.oxygen_core.common.InstantDataAbsorption;
import austeretony.oxygen_core.common.InstantDataArmor;
import austeretony.oxygen_core.common.InstantDataHealth;
import austeretony.oxygen_core.common.InstantDataMaxHealth;
import austeretony.oxygen_core.common.InstantDataPotionEffects;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.network.Network;
import austeretony.oxygen_core.common.network.client.CPAddSharedData;
import austeretony.oxygen_core.common.network.client.CPPlaySoundEvent;
import austeretony.oxygen_core.common.network.client.CPPrivilegeAction;
import austeretony.oxygen_core.common.network.client.CPRemoveSharedData;
import austeretony.oxygen_core.common.network.client.CPRoleAction;
import austeretony.oxygen_core.common.network.client.CPShowStatusMessage;
import austeretony.oxygen_core.common.network.client.CPSyncConfigs;
import austeretony.oxygen_core.common.network.client.CPSyncAbsentData;
import austeretony.oxygen_core.common.network.client.CPSyncInstantData;
import austeretony.oxygen_core.common.network.client.CPSyncMainData;
import austeretony.oxygen_core.common.network.client.CPSyncNotification;
import austeretony.oxygen_core.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen_core.common.network.client.CPSyncPlayerRoles;
import austeretony.oxygen_core.common.network.client.CPSyncPreset;
import austeretony.oxygen_core.common.network.client.CPSyncPresetsVersions;
import austeretony.oxygen_core.common.network.client.CPSyncPrivilegesData;
import austeretony.oxygen_core.common.network.client.CPSyncRolesData;
import austeretony.oxygen_core.common.network.client.CPSyncSharedData;
import austeretony.oxygen_core.common.network.client.CPSyncValidDataIds;
import austeretony.oxygen_core.common.network.client.CPSyncWatchedValue;
import austeretony.oxygen_core.common.network.server.SPAbsentDataIds;
import austeretony.oxygen_core.common.network.server.SPAddPrivilege;
import austeretony.oxygen_core.common.network.server.SPCreateRole;
import austeretony.oxygen_core.common.network.server.SPManagePlayerRoles;
import austeretony.oxygen_core.common.network.server.SPRemovePrivilege;
import austeretony.oxygen_core.common.network.server.SPRemoveRole;
import austeretony.oxygen_core.common.network.server.SPRequestPresetSync;
import austeretony.oxygen_core.common.network.server.SPRequestPrivilegesData;
import austeretony.oxygen_core.common.network.server.SPRequestReply;
import austeretony.oxygen_core.common.network.server.SPRequestSharedDataSync;
import austeretony.oxygen_core.common.network.server.SPSetActivityStatus;
import austeretony.oxygen_core.common.network.server.SPSetChatFormattingRole;
import austeretony.oxygen_core.common.network.server.SPStartDataSync;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegesProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenWorldLoadedEvent;
import austeretony.oxygen_core.server.command.CommandOxygenOperator;
import austeretony.oxygen_core.server.command.CommandOxygenServer;
import austeretony.oxygen_core.server.command.CoreArgumentOperator;
import austeretony.oxygen_core.server.currency.OxygenCoinsCurrencyProvider;
import austeretony.oxygen_core.server.currency.OxygenShardsCurrencyProvider;
import austeretony.oxygen_core.server.currency.OxygenVouchersCurrencyProvider;
import austeretony.oxygen_core.server.event.OxygenEventsServer;
import austeretony.oxygen_core.server.instant.InstantDataRegistryServer;
import austeretony.oxygen_core.server.item.ItemsBlackList;
import austeretony.oxygen_core.server.network.NetworkRequestsRegistryServer;
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
    VERSION = "0.10.0", 
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
     * Player Interaction - 7
     * Mail - 8
     * Chat - 9
     * Trade - 10
     * Guilds - 11
     * Interaction - 12
     */

    //Shared Constants

    public static final int 
    OXYGEN_CORE_MOD_INDEX = 0,

    SIMPLE_NOTIFICATION_ID = 0,

    ACTIVITY_STATUS_SHARED_DATA_ID = 0,
    DIMENSION_SHARED_DATA_ID = 1,

    ROLES_SHARED_DATA_STARTING_INDEX = 10,
    DEFAULT_ROLE_INDEX = - 1,
    MAX_ROLES_PER_PLAYER = 5,

    REQUEST_PRESETS_REQUEST_ID = 0,
    REQUEST_SHARED_DATA_REQUEST_ID = 1,
    REQUEST_REPLY_REQUEST_ID = 2,
    SET_ACTIVITY_STATUS_REQUEST_ID = 3,
    MANAGE_PRIVILEGES_REQUEST_ID = 4,

    ITEM_CATEGORIES_PRESET_ID = 0,

    COMMON_CURRENCY_INDEX = 0,

    OPERATOR_ROLE_ID = 100,

    HEALTH_INSTANT_DATA_INDEX = 0,
    MAX_HEALTH_INSTANT_DATA_INDEX = 1,
    TOTAL_ARMOR_INSTANT_DATA_INDEX = 2,
    ABSORPTION_INSTANT_DATA_INDEX = 3,
    ACTIVE_EFFECTS_INSTANT_DATA_INDEX = 10;

    public static final DateFormat ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigManager.create();
        OxygenHelperCommon.registerConfig(new OxygenConfig());
        OxygenHelperCommon.registerConfig(new PrivilegesConfig());
        if (event.getSide() == Side.CLIENT)
            CommandOxygenClient.registerArgument(new CoreArgumentClient());
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
        for (int i = 0; i < MAX_ROLES_PER_PLAYER; i++)
            OxygenHelperServer.registerSharedDataValue(i + ROLES_SHARED_DATA_STARTING_INDEX, Byte.BYTES);
        CommandOxygenOperator.registerArgument(new CoreArgumentOperator());
        NetworkRequestsRegistryServer.registerRequest(REQUEST_PRESETS_REQUEST_ID, 100000);
        NetworkRequestsRegistryServer.registerRequest(REQUEST_SHARED_DATA_REQUEST_ID, 1000);
        NetworkRequestsRegistryServer.registerRequest(REQUEST_REPLY_REQUEST_ID, 1000);
        NetworkRequestsRegistryServer.registerRequest(SET_ACTIVITY_STATUS_REQUEST_ID, 1000);
        NetworkRequestsRegistryServer.registerRequest(MANAGE_PRIVILEGES_REQUEST_ID, 1000);
        CurrencyHelperServer.registerCurrencyProvider(new OxygenCoinsCurrencyProvider());
        CurrencyHelperServer.registerCurrencyProvider(new OxygenShardsCurrencyProvider());
        CurrencyHelperServer.registerCurrencyProvider(new OxygenVouchersCurrencyProvider());
        InstantDataRegistryServer.registerInstantData(new InstantDataHealth());
        InstantDataRegistryServer.registerInstantData(new InstantDataMaxHealth());
        InstantDataRegistryServer.registerInstantData(new InstantDataAbsorption());
        InstantDataRegistryServer.registerInstantData(new InstantDataArmor());
        InstantDataRegistryServer.registerInstantData(new InstantDataPotionEffects());
        EnumOxygenPrivilege.register();
        if (event.getSide() == Side.CLIENT) {     
            OxygenManagerClient.create();
            ClientReference.registerCommand(new CommandOxygenClient("oxygenc"));
            CommonReference.registerEvent(new OxygenEventsClient());
            CommonReference.registerEvent(new OxygenOverlayHandler());     
            OxygenGUIHelper.registerOverlay(new RequestOverlay());
            OxygenGUIHelper.registerOxygenMenuEntry(NotificationsScreen.NOTIFICATIONS_MENU_ENTRY);
            OxygenGUIHelper.registerOxygenMenuEntry(SettingsScreen.SETTINGS_MENU_ENTRY);
            OxygenGUIHelper.registerOxygenMenuEntry(PrivilegesScreen.PRIVILEGES_MENU_ENTRY);
            OxygenHelperClient.registerStatusMessagesHandler(new OxygenStatusMessagesHandler());
            EnumBaseClientSetting.register();
            EnumCoreClientSetting.register();
            EnumBaseGUISetting.register();
            EnumCoreGUISetting.register();
            SettingsScreen.registerSettingsContainer(new BaseSettingsContainer());
            SettingsScreen.registerSettingsContainer(new CoreSettingsContainer());
            OxygenHelperClient.registerCurrencyProperties(new OxygenCoinsCurrencyProperties());
            OxygenHelperClient.registerCurrencyProperties(new OxygenShardsCurrencyProperties());
            OxygenHelperClient.registerCurrencyProperties(new OxygenVouchersCurrencyProperties());
            OxygenHelperClient.registerInstantData(new InstantDataHealth());
            OxygenHelperClient.registerInstantData(new InstantDataMaxHealth());
            OxygenHelperClient.registerInstantData(new InstantDataAbsorption());
            OxygenHelperClient.registerInstantData(new InstantDataArmor());
            OxygenHelperClient.registerInstantData(new InstantDataPotionEffects());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemsBlackList.loadBlackLists();
        OxygenManagerServer.instance().getPresetsManager().loadPresets();
        if (event.getSide() == Side.CLIENT)
            OxygenManagerClient.instance().init();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommonReference.registerCommand(event, new CommandOxygenOperator("oxygenop"));
        CommonReference.registerCommand(event, new CommandOxygenServer("oxygens"));
        OxygenManagerServer.instance().getChatChannelsManager().initChannels(event);

        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        LOGGER.info("Initializing world: {}", worldName);
        OxygenManagerServer.instance().worldLoaded(worldFolder, event.getServer().getMaxPlayers());
        PrivilegesLoaderServer.loadPrivilegeData();

        LOGGER.info("Active common currency provider: <{}>", OxygenManagerServer.instance().getCurrencyManager().getCommonCurrencyProvider().getDisplayName());
        LOGGER.info("Loaded currency providers:");
        CurrencyHelperServer.getCurrencyProviders()
        .stream()
        .sorted((p1, p2)->p1.getIndex() - p2.getIndex())
        .forEach((provider)->LOGGER.info(" - index: <{}>, name: <{}>", provider.getIndex(), provider.getDisplayName()));

        MinecraftForge.EVENT_BUS.post(new OxygenWorldLoadedEvent());
    }

    public static void addDefaultPrivileges() {
        if (PrivilegesProviderServer.getRole(OPERATOR_ROLE_ID).getPrivilege(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id()) == null) {
            PrivilegesProviderServer.getRole(OPERATOR_ROLE_ID).addPrivilege(PrivilegeUtils.getPrivilege(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), true));
            LOGGER.info("Default Operator role privileges added.");
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
        network.registerPacket(CPSyncPlayerRoles.class);
        network.registerPacket(CPSyncRolesData.class);
        network.registerPacket(CPSyncSharedData.class);
        network.registerPacket(CPShowStatusMessage.class);
        network.registerPacket(CPSyncNotification.class);
        network.registerPacket(CPSyncObservedPlayersData.class);
        network.registerPacket(CPPlaySoundEvent.class);
        network.registerPacket(CPSyncValidDataIds.class);
        network.registerPacket(CPSyncAbsentData.class);
        network.registerPacket(CPSyncPresetsVersions.class);
        network.registerPacket(SPRequestPresetSync.class);
        network.registerPacket(CPSyncPreset.class);
        network.registerPacket(CPSyncWatchedValue.class);
        network.registerPacket(CPAddSharedData.class);
        network.registerPacket(CPRemoveSharedData.class);
        network.registerPacket(CPSyncPrivilegesData.class);
        network.registerPacket(CPRoleAction.class);
        network.registerPacket(CPPrivilegeAction.class);
        network.registerPacket(CPSyncInstantData.class);

        network.registerPacket(SPRequestReply.class);
        network.registerPacket(SPSetActivityStatus.class);
        network.registerPacket(SPStartDataSync.class);
        network.registerPacket(SPAbsentDataIds.class);
        network.registerPacket(SPRequestSharedDataSync.class);
        network.registerPacket(SPSetChatFormattingRole.class);
        network.registerPacket(SPRequestPrivilegesData.class);
        network.registerPacket(SPCreateRole.class);
        network.registerPacket(SPRemoveRole.class);
        network.registerPacket(SPAddPrivilege.class);
        network.registerPacket(SPRemovePrivilege.class);
        network.registerPacket(SPManagePlayerRoles.class);
    }

    public static Network network() {
        return network;
    }
}
