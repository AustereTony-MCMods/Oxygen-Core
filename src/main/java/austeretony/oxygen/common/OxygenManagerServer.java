package austeretony.oxygen.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.event.OxygenPlayerUnloadedEvent;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.currency.ICurrencyProvider;
import austeretony.oxygen.common.currency.OxygenCoinsProvider;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.EnumOxygenChatMessage;
import austeretony.oxygen.common.main.EnumOxygenPrivilege;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen.common.network.client.CPOpenOxygenScreen;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.notification.EnumNotification;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.request.IRequestValidator;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.common.sync.gui.api.AdvancedGUIHandlerServer;
import austeretony.oxygen.common.sync.gui.api.ComplexGUIHandlerServer;
import austeretony.oxygen.common.sync.gui.network.CPComplexSyncValidIdentifiers;
import austeretony.oxygen.common.sync.gui.network.CPSyncSharedData;
import austeretony.oxygen.common.sync.gui.network.CPSyncValidIdentifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private OxygenThread commonIOThread, serviceIOThread;

    private final OxygenLoaderServer loader;

    private final PrivilegeManagerServer privilegeManager;

    private final SharedDataManagerServer sharedDataManager;

    private final ProcessesManagerServer processesManager;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private Set<IRequestValidator> requestValidators;

    private ICurrencyProvider currencyProvider;

    private final Random random = new Random();

    private OxygenManagerServer() {
        this.loader = new OxygenLoaderServer();
        this.privilegeManager = new PrivilegeManagerServer();
        this.sharedDataManager = new SharedDataManagerServer();
        this.processesManager = new ProcessesManagerServer();
    }

    public static void create() {
        if (instance == null)
            instance = new OxygenManagerServer();
    }

    public static OxygenManagerServer instance() {
        return instance;
    }

    public Random getRandom() {
        return this.random;
    }

    public OxygenLoaderServer getLoader() {
        return this.loader;
    }

    public PrivilegeManagerServer getPrivilegeManager() {
        return this.privilegeManager;
    }

    public SharedDataManagerServer getSharedDataManager() {
        return this.sharedDataManager;
    }

    public ProcessesManagerServer getProcessesManager() {
        return this.processesManager;
    }

    public void createOxygenServerThreads() {
        this.commonIOThread = new OxygenThread("Oxygen IO Server");
        this.serviceIOThread = new OxygenThread("Oxygen Service IO Server");
        OxygenMain.OXYGEN_LOGGER.info("Starting IO server thread...");
        this.commonIOThread.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Service IO server thread...");
        this.serviceIOThread.start();
    }

    public OxygenThread getCommonIOThread() {
        return this.commonIOThread;
    }

    public OxygenThread getServiceIOThread() {
        return this.serviceIOThread;
    }

    public void addCommonIOTask(IOxygenTask task) {
        this.commonIOThread.addTask(task);
    }

    public void addServiceIOTask(IOxygenTask task) {
        this.serviceIOThread.addTask(task);
    }

    public Collection<OxygenPlayerData> getPlayersData() {
        return this.playersData.values();
    }

    public void addPlayerData(OxygenPlayerData playerData) {
        this.playersData.put(playerData.getPlayerUUID(), playerData);
    }

    public OxygenPlayerData createPlayerData(UUID playerUUID) {
        OxygenPlayerData oxygenData = new OxygenPlayerData(playerUUID);
        this.playersData.put(playerUUID, oxygenData);
        return oxygenData;
    }

    public void removePlayerData(UUID playerUUID) {
        this.playersData.remove(playerUUID);
    }

    public boolean playerDataExist(UUID playerUUID) {
        return this.playersData.containsKey(playerUUID);
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public void syncSharedPlayersData(EntityPlayerMP playerMP, int... identifiers) {
        OxygenMain.network().sendTo(new CPSyncSharedPlayersData(identifiers), playerMP);
    }

    public void addNotification(EntityPlayer player, INotification notification) {
        if (notification.getType() == EnumNotification.REQUEST)
            this.playersData.get(CommonReference.getPersistentUUID(player)).addTemporaryProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), (EntityPlayerMP) player);
    }

    public void registerRequestValidator(IRequestValidator validator) {
        if (this.requestValidators == null)
            this.requestValidators = new HashSet<IRequestValidator>(3);
        this.requestValidators.add(validator);
    }

    public Set<IRequestValidator> getRequestValidators() {
        return this.requestValidators;
    }

    private boolean validateRequest(UUID senderUUID, UUID requestedUUID) {
        if (this.requestValidators == null)
            return true;
        for (IRequestValidator validator : this.requestValidators)
            if (!validator.isValid(senderUUID, requestedUUID))
                return false;
        return true;
    }

    public void sendRequest(EntityPlayer sender, EntityPlayer target, INotification notification, boolean setRequesting) {
        UUID 
        senderUUID = CommonReference.getPersistentUUID(sender),
        targetUUID = CommonReference.getPersistentUUID(target);
        OxygenPlayerData 
        senderData = this.getPlayerData(senderUUID),
        targetData = this.getPlayerData(targetUUID);
        if (!senderData.isRequesting() 
                && (targetData.getStatus() != EnumActivityStatus.OFFLINE || PrivilegeProviderServer.getPrivilegeValue(senderUUID, EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), false))
                && this.validateRequest(senderUUID, targetUUID)) {
            this.addNotification(target, notification);
            if (setRequesting)
                senderData.setRequesting(true);
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessage.REQUEST_SENT.ordinal());
        } else
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessage.REQUEST_RESET.ordinal());
    }

    //TODO onPlayerLoggedIn()
    public void onPlayerLoggedIn(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncMainData(playerUUID), (EntityPlayerMP) player);
        if (!this.playerDataExist(playerUUID)) {
            OxygenPlayerData oxygenData = this.createPlayerData(playerUUID);
            oxygenData.setSyncing(true);
            OxygenLoaderServer.loadPlayerDataCreateSharedEntryDelegated(playerUUID, oxygenData, player);
        }
    }

    //TODO onPlayerLoggedOut()
    public void onPlayerLoggedOut(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        if (this.playerDataExist(playerUUID)) {
            MinecraftForge.EVENT_BUS.post(new OxygenPlayerUnloadedEvent(player));

            this.sharedDataManager.removePlayerSharedDataEntrySynced(playerUUID);
            this.removePlayerData(playerUUID);
        }
    }

    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        this.sharedDataManager.updateDimensionData(playerUUID, currDimension);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.getPlayerData(CommonReference.getPersistentUUID(player)).processRequestReply(player, reply, id);
    }

    public void changeActivityStatus(EntityPlayerMP playerMP, EnumActivityStatus status) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.getPlayerData(playerUUID);
        if (status != playerData.getStatus()) {
            playerData.setStatus(status);
            OxygenHelperServer.savePersistentDataDelegated(playerData);
            this.getSharedDataManager().updateStatusData(playerUUID, status);
        }
    }

    public void openSharedDataListenerScreen(EntityPlayerMP playerMP, int screenId) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (!OxygenHelperServer.isSyncing(playerUUID)) {
            this.syncSharedPlayersData(playerMP, this.getSharedDataIdentifiersForScreen(screenId));
            OxygenMain.network().sendTo(new CPOpenOxygenScreen(EnumScreenType.SHARED_DATA_LISTENER_SCREEN, screenId), playerMP);
        }
    }

    public void openAdvancedScreen(EntityPlayerMP playerMP, int screenId) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = OxygenHelperServer.getPlayerData(playerUUID);
        if (!playerData.isSyncing()) {
            playerData.setSyncing(true);
            if (this.DATA_IDENTIFIERS.containsKey(screenId))
                OxygenMain.network().sendTo(new CPSyncSharedData(OxygenHelperServer.getSharedDataIdentifiersForScreen(screenId)), playerMP);
            AdvancedGUIHandlerServer.getNetwork(screenId).sendTo(new CPSyncValidIdentifiers(screenId, playerUUID), playerMP);
        }
    }

    public void openComplexScreen(EntityPlayerMP playerMP, int screenId) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = OxygenHelperServer.getPlayerData(playerUUID);
        if (!playerData.isSyncing()) {
            playerData.setSyncing(true);
            if (this.DATA_IDENTIFIERS.containsKey(screenId))
                OxygenMain.network().sendTo(new CPSyncSharedData(OxygenHelperServer.getSharedDataIdentifiersForScreen(screenId)), playerMP);
            ComplexGUIHandlerServer.getNetwork(screenId).sendTo(new CPComplexSyncValidIdentifiers(screenId, playerUUID), playerMP);
        }
    }

    private static final Map<Integer, int[]> DATA_IDENTIFIERS = new HashMap<Integer, int[]>(10);

    public void registerSharedDataIdentifierForScreen(int screenId, int dataIdentifier) {
        if (!DATA_IDENTIFIERS.containsKey(screenId)) {
            int[] ids = new int[10];
            ids[0] = dataIdentifier;
            DATA_IDENTIFIERS.put(screenId, ids);
        } else {
            int[] ids = DATA_IDENTIFIERS.get(screenId);
            for (int i = 0; i < 10; i++) {
                if (ids[i] == 0) {
                    ids[i] = dataIdentifier;
                    break;
                }
            }
            DATA_IDENTIFIERS.put(screenId, ids);
        }
    }

    public int[] getSharedDataIdentifiersForScreen(int screenId) {
        return DATA_IDENTIFIERS.get(screenId);
    }

    public void registerCurrencyProvider(ICurrencyProvider provider) {
        if (this.currencyProvider == null)
            this.currencyProvider = provider;
    }

    public ICurrencyProvider getCurrencyProvider() {
        return this.currencyProvider;
    }

    public void validateCurrencyProvider() {
        if (this.currencyProvider == null)
            this.currencyProvider = new OxygenCoinsProvider();
    }

    public long getCurrency(UUID playerUUID) {
        return this.currencyProvider.getCurrency(playerUUID);
    }

    public boolean enoughCurrency(UUID playerUUID, long required) {
        return this.currencyProvider.enoughCurrency(playerUUID, required);
    }

    public void setCurrency(UUID playerUUID, long value) {
        this.currencyProvider.setCurrency(playerUUID, value);
    }

    public void addCurrency(UUID playerUUID, long value) {
        this.currencyProvider.addCurrency(playerUUID, value);
    }

    public void removeCurrency(UUID playerUUID, long value) {
        this.currencyProvider.removeCurrency(playerUUID, value);
    }

    public void save(UUID playerUUID) {
        this.currencyProvider.save(playerUUID);
    }

    public void reset() {
        this.playersData.clear();
        this.processesManager.reset();
        this.sharedDataManager.reset();
    }
}
