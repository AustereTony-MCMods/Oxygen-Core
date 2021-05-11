package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.command.AcceptArgumentClient;
import austeretony.oxygen_core.client.command.RejectArgumentClient;
import austeretony.oxygen_core.client.condition.ConditionsClient;
import austeretony.oxygen_core.client.event.OxygenEventsClient;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuHelper;
import austeretony.oxygen_core.client.preset.CurrencyPropertiesPresetClient;
import austeretony.oxygen_core.client.preset.DimensionNamesPresetClient;
import austeretony.oxygen_core.client.preset.ItemCategoriesPresetClient;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.settings.gui.SettingsScreen;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.command.CommandOxygenClient;
import austeretony.oxygen_core.client.command.CoreArgumentClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.network.Network;
import austeretony.oxygen_core.common.network.packets.client.*;
import austeretony.oxygen_core.common.network.packets.server.*;
import austeretony.oxygen_core.common.player.shared.SharedDataRegistry;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.LoggingUtils;
import austeretony.oxygen_core.common.util.value.ValueType;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.command.CommandOxygenOperator;
import austeretony.oxygen_core.server.command.CommandOxygenServer;
import austeretony.oxygen_core.server.command.CoreArgumentOperator;
import austeretony.oxygen_core.server.command.CoreArgumentServer;
import austeretony.oxygen_core.server.condition.ConditionsServer;
import austeretony.oxygen_core.server.currency.CoinsProvider;
import austeretony.oxygen_core.server.currency.CurrencyProvider;
import austeretony.oxygen_core.server.currency.ShardsProvider;
import austeretony.oxygen_core.server.currency.VouchersProvider;
import austeretony.oxygen_core.server.event.OxygenEventsServer;
import austeretony.oxygen_core.server.preset.CurrencyPropertiesPresetServer;
import austeretony.oxygen_core.server.preset.DimensionNamesPresetServer;
import austeretony.oxygen_core.server.preset.ItemCategoriesPresetServer;
import austeretony.oxygen_core.common.util.value.custom.PotionEffectsListValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Mod(
        modid = OxygenMain.MOD_ID,
        name = OxygenMain.NAME,
        version = OxygenMain.VERSION,
        certificateFingerprint = "@FINGERPRINT@",
        updateJSON = OxygenMain.VERSIONS_FORGE_URL)
public class OxygenMain {

    public static final String
            MOD_ID = "oxygen_core",
            NAME = "Oxygen Core",
            VERSION = "0.12.0",
            VERSION_CUSTOM = VERSION + ":beta:0",
            VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Oxygen-Core/info/versions.json";

    private static final Logger LOGGER = LoggingUtils.getLogger("common", "oxygen", "Oxygen");
    public static final Logger OPERATIONS_LOGGER = LoggingUtils.getLogger("operations", "operations", "Oxygen Operations");

    private static Network network;

    public static final DateTimeFormatter
            ID_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss", Locale.ROOT),
            DEBUG_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ROOT);

    public static final String SYSTEM_SENDER = "oxygen_core.sender.sys";
    public static final UUID SYSTEM_UUID = UUID.fromString("d10d07f6-ae3c-4ec6-a055-1160c4cf848a");

    //oxygen module index
    public static int MODULE_INDEX = 0;

    //presets ids
    public static final int
            PRESET_DIMENSION_NAMES = 0,
            PRESET_CURRENCY_PROPERTIES = 1,
            PRESET_ITEM_CATEGORIES = 2;

    //shared data ids
    public static final int
            SHARED_LAST_ACTIVITY_TIME = 0,
            SHARED_ACTIVITY_STATUS = 1,
            SHARED_DIMENSION = 2;

    //network requests ids
    public static final int
            NET_REQUEST_PRESETS_SYNC = 0,
            NET_REQUEST_SHARED_DATA_SYNC = 1,
            NET_REQUEST_REQUEST_ACTION = 3,
            NET_REQUEST_ACTIVITY_STATUS_CHANGE = 4;

    //currency ids
    public static final int
            CURRENCY_COINS = 0,
            CURRENCY_SHARDS = 1,
            CURRENCY_VOUCHERS = 2;

    //observable entity values ids
    public static final int
            OBSERVED_HEALTH = 0,
            OBSERVED_MAX_HEALTH = 1,
            OBSERVED_ABSORPTION = 2,
            OBSERVED_ARMOR = 3,
            OBSERVED_POTION_EFFECTS = 4;

    //screen ids
    public static final int SCREEN_ID_SETTINGS = 0;

    //operations
    public static final String
            OPERATION_CURRENCY_GAIN = "core:currency_gain",
            OPERATION_CURRENCY_WITHDRAW = "core:currency_withdraw";

    public static final String KEY_BINDINGS_CATEGORY = "Oxygen";
    public static int USERNAME_FIELD_LENGTH = 24;

    public static Network network() {
        return network;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OxygenCommon.registerConfig(new CoreConfig());
        if (event.getSide() == Side.CLIENT) {
            CommandOxygenClient.registerArgument(new CoreArgumentClient());
            CommandOxygenClient.registerArgument(new AcceptArgumentClient());
            CommandOxygenClient.registerArgument(new RejectArgumentClient());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigManager.instance().loadConfigs();
        initNetwork();
        MinecraftCommon.registerEventHandler(new OxygenEventsServer());
        CommandOxygenServer.registerArgument(new CoreArgumentServer());
        CommandOxygenOperator.registerArgument(new CoreArgumentOperator());
        OxygenServer.registerCurrencyProvider(new CoinsProvider());
        OxygenServer.registerCurrencyProvider(new ShardsProvider());
        OxygenServer.registerCurrencyProvider(new VouchersProvider());
        SharedDataRegistry.register(SHARED_LAST_ACTIVITY_TIME, ValueType.LONG);
        SharedDataRegistry.register(SHARED_ACTIVITY_STATUS, ValueType.BYTE);
        SharedDataRegistry.register(SHARED_DIMENSION, ValueType.INTEGER);
        OxygenServer.registerNetworkRequest(NET_REQUEST_PRESETS_SYNC, 10000);
        OxygenServer.registerNetworkRequest(NET_REQUEST_SHARED_DATA_SYNC, 1000);
        OxygenServer.registerNetworkRequest(NET_REQUEST_REQUEST_ACTION, 1000);
        OxygenServer.registerNetworkRequest(NET_REQUEST_ACTIVITY_STATUS_CHANGE, 1000);
        OxygenServer.registerPreset(new DimensionNamesPresetServer());
        OxygenServer.registerPreset(new CurrencyPropertiesPresetServer());
        OxygenServer.registerPreset(new ItemCategoriesPresetServer());
        OxygenServer.registerObservedValue(OBSERVED_HEALTH, ValueType.FLOAT, entityId -> {
            EntityLivingBase entity = MinecraftCommon.getEntityLivingBaseById(entityId);
            if (entity != null) return entity.getHealth();
            return 0F;
        });
        OxygenServer.registerObservedValue(OBSERVED_MAX_HEALTH, ValueType.FLOAT, entityId -> {
            EntityLivingBase entity = MinecraftCommon.getEntityLivingBaseById(entityId);
            if (entity != null) return entity.getMaxHealth();
            return 0F;
        });
        OxygenServer.registerObservedValue(OBSERVED_ABSORPTION, ValueType.FLOAT, entityId -> {
            EntityLivingBase entity = MinecraftCommon.getEntityLivingBaseById(entityId);
            if (entity != null) return entity.getAbsorptionAmount();
            return 0F;
        });
        OxygenServer.registerObservedValue(OBSERVED_ARMOR, ValueType.INTEGER, entityId -> {
            EntityLivingBase entity = MinecraftCommon.getEntityLivingBaseById(entityId);
            if (entity != null) return entity.getTotalArmorValue();
            return 0;
        });
        OxygenServer.registerObservedValue(OBSERVED_POTION_EFFECTS,
                PotionEffectsListValue::new,
                entityId -> {
                    EntityLivingBase entity = MinecraftCommon.getEntityLivingBaseById(entityId);
                    if (entity != null) return new ArrayList<>(entity.getActivePotionEffects());
                    return null;
                });
        CorePrivileges.register();
        ConditionsServer.register();
        if (event.getSide() == Side.CLIENT) {
            MinecraftCommon.registerEventHandler(new OxygenEventsClient());
            MinecraftClient.registerCommand(new CommandOxygenClient("oxygenc"));
            OxygenClient.registerPreset(new DimensionNamesPresetClient());
            OxygenClient.registerPreset(new CurrencyPropertiesPresetClient());
            OxygenClient.registerPreset(new ItemCategoriesPresetClient());
            OxygenClient.registerWatcherValue(CURRENCY_COINS, ValueType.LONG);
            OxygenClient.registerWatcherValue(CURRENCY_SHARDS, ValueType.LONG);
            OxygenClient.registerWatcherValue(CURRENCY_VOUCHERS, ValueType.LONG);
            OxygenClient.registerObservedValue(OBSERVED_HEALTH, ValueType.FLOAT);
            OxygenClient.registerObservedValue(OBSERVED_MAX_HEALTH, ValueType.FLOAT);
            OxygenClient.registerObservedValue(OBSERVED_ABSORPTION, ValueType.FLOAT);
            OxygenClient.registerObservedValue(OBSERVED_ARMOR, ValueType.INTEGER);
            OxygenClient.registerObservedValue(OBSERVED_POTION_EFFECTS, PotionEffectsListValue::new);
            CoreSettings.register();
            OxygenMenuHelper.addMenuEntry(SettingsScreen.SETTINGS_SCREEN_ENTRY);
            OxygenClient.registerScreen(SCREEN_ID_SETTINGS, SettingsScreen::open);
            ConditionsClient.register();
            OxygenManagerClient.instance().getKeyHandler().registerKeyBindings();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OxygenManagerServer.instance().getItemsBlacklistManager().loadBlacklists();
        if (event.getSide() == Side.CLIENT) {
            OxygenManagerClient.instance().getSettingsManager().loadSettings();
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        MinecraftCommon.registerCommand(event, new CommandOxygenOperator("oxygenop"));
        MinecraftCommon.registerCommand(event, new CommandOxygenServer("oxygens"));

        String
                worldName = event.getServer().getFolderName(),
                worldFolder = event.getServer().isSinglePlayer()
                        ? MinecraftCommon.getGameFolder() + "/saves/" + worldName
                        : MinecraftCommon.getGameFolder() + "/" + worldName;
        logInfo(1,"[Core] Initializing world: {}", worldName);
        OxygenManagerServer.instance().serverStarting(worldFolder);

        logInfo(1, "[Core] Loaded currency providers:");
        OxygenServer.getCurrencyProviders()
                .stream()
                .sorted(Comparator.comparing(CurrencyProvider::getIndex))
                .forEach(provider ->
                        logInfo(1,"[Core] - index: <{}>, name: <{}>", provider.getIndex(), provider.getName()));
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        OxygenManagerServer.instance().serverStopping(event.getSide());
    }

    private void initNetwork() {
        network = Network.create(MOD_ID);

        network.registerPacket(CPSyncConfigs.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncAbsentData.class);
        network.registerPacket(CPSyncValidDataIds.class);
        network.registerPacket(CPDataSyncFailed.class);
        network.registerPacket(CPSyncWatcherValues.class);
        network.registerPacket(CPSyncSharedData.class);
        network.registerPacket(CPSyncPresetsVersions.class);
        network.registerPacket(CPSyncPreset.class);
        network.registerPacket(CPSyncObservedEntitiesData.class);
        network.registerPacket(CPSendRequest.class);
        network.registerPacket(CPSendStatusMessage.class);
        network.registerPacket(CPOpenOxygenGUI.class);
        network.registerPacket(CPSyncSharedDataList.class);
        network.registerPacket(CPRemoveSharedDataList.class);
        network.registerPacket(CPPerformOperation.class);

        network.registerPacket(SPAbsentDataIds.class);
        network.registerPacket(SPRequestDataSync.class);
        network.registerPacket(SPRequestSharedDataSync.class);
        network.registerPacket(SPRequestPresetsSync.class);
        network.registerPacket(SPRequestAction.class);
        network.registerPacket(SPChangeActivityStatus.class);
        network.registerPacket(SPPerformOperation.class);
        network.registerPacket(CPPlaySoundAtPlayer.class);
    }

    public static void log(Level loggingLevel, int level, String message, Object... args) {
        if (level <= CoreConfig.LOGGING_LEVEL.asInt()) {
            LOGGER.log(loggingLevel, message, args);
        }
    }

    public static void log(Level loggingLevel, int level, String message, Throwable throwable) {
        if (level <= CoreConfig.LOGGING_LEVEL.asInt()) {
            LOGGER.log(loggingLevel, message, throwable);
        }
    }

    public static void logInfo(int level, String message, Object... args) {
        log(Level.INFO, level, message, args);
    }

    public static void logError(int level, String message, Object... args) {
        log(Level.ERROR, level, message, args);
    }

    public static void logError(int level, String message, Throwable throwable) {
        log(Level.ERROR, level, message, throwable);
    }
}
