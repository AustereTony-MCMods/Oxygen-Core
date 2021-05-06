package austeretony.oxygen_core.server.currency;

import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.value.ValueType;
import austeretony.oxygen_core.server.api.OxygenServer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CurrencyManagerServer {

    private final Map<Integer, CurrencyProvider> providersMap = new HashMap<>(3);
    private final Map<UUID, PlayerWallet> playersMap = new HashMap<>();

    private final ExecutorService executor;

    public CurrencyManagerServer() {
        int threads = Math.max(1, CoreConfig.CURRENCY_DB_THREADS.asInt());
        executor = Executors.newFixedThreadPool(threads, new ThreadFactoryBuilder()
                .setNameFormat("Oxygen DB Currency #%d")
                .setDaemon(true)
                .build());
    }

    public ExecutorService getDBExecutor() {
        return executor;
    }

    public void registerCurrencyProvider(CurrencyProvider provider) {
        providersMap.put(provider.getIndex(), provider);
        OxygenServer.registerWatcherValue(provider.getIndex(), ValueType.LONG);
    }

    public Collection<CurrencyProvider> getCurrencyProviders() {
        return providersMap.values();
    }

    @Nullable
    public CurrencyProvider getCurrencyProvider(int index) {
        return providersMap.get(index);
    }

    public void queryBalance(UUID playerUUID, String username, int index, @Nonnull Consumer<Long> consumer,
                             CallingThread caller) {
        CurrencyProvider provider = providersMap.get(index);
        if (provider == null) {
            consumer.accept(null);
        } else {
            executeTask(() -> {
                Long balance = provider.getBalance(playerUUID, username);
                applyResult(consumer, balance, provider.getSource(), caller);
            }, provider.getSource(), caller);
        }
    }

    public void setBalance(UUID playerUUID, String username, int index, long value, CallingThread caller,
                           @Nullable Consumer<Long> consumer) {
        CurrencyProvider provider = providersMap.get(index);
        if (provider == null) {
            if (consumer != null) consumer.accept(null);
            return;
        }

        executeTask(() -> {
            Long newBalance = provider.setBalance(playerUUID, username, value);
            if (newBalance != null) {
                provider.updated(playerUUID, username, newBalance);
                OxygenServer.updateWatcherValue(playerUUID, provider.getIndex(), newBalance); // synchronized
            }
            applyResult(consumer, newBalance, provider.getSource(), caller);
        }, provider.getSource(), caller);
    }

    public void setBalance(UUID playerUUID, String username, int index, long value, CallingThread caller) {
        setBalance(playerUUID, username, index, value, caller, null);
    }

    public void incrementBalance(UUID playerUUID, String username, int index, long increment, CallingThread caller,
                                 @Nullable Consumer<Long> consumer) {
        CurrencyProvider provider = providersMap.get(index);
        if (provider == null) {
            if (consumer != null) consumer.accept(null);
            return;
        }

        executeTask(() -> {
            Long newBalance = provider.incrementBalance(playerUUID, username, increment);
            if (newBalance != null) {
                provider.updated(playerUUID, username, newBalance);
                OxygenServer.updateWatcherValue(playerUUID, provider.getIndex(), newBalance);
            }
            applyResult(consumer, newBalance, provider.getSource(), caller);
        }, provider.getSource(), caller);
    }

    public void incrementBalance(UUID playerUUID, String username, int index, long increment, CallingThread caller) {
        incrementBalance(playerUUID, username, index, increment, caller, null);
    }

    public void decrementBalance(UUID playerUUID, String username, int index, long decrement, CallingThread caller,
                                 @Nullable Consumer<Long> consumer) {
        CurrencyProvider provider = providersMap.get(index);
        if (provider == null) {
            if (consumer != null) consumer.accept(null);
            return;
        }

        executeTask(() -> {
            Long newBalance = provider.decrementBalance(playerUUID, username, decrement);
            if (newBalance != null) {
                provider.updated(playerUUID, username, newBalance);
                OxygenServer.updateWatcherValue(playerUUID, provider.getIndex(), newBalance);
            }
            applyResult(consumer, newBalance, provider.getSource(), caller);
        }, provider.getSource(), caller);
    }

    public void decrementBalance(UUID playerUUID, String username, int index, long decrement, CallingThread caller) {
        decrementBalance(playerUUID, username, index, decrement, caller, null);
    }

    private void executeTask(Runnable task, CurrencySource source, CallingThread caller) {
        switch (source) {
            case OXYGEN:
                if (caller == CallingThread.OXYGEN) {
                    task.run();
                } else {
                    OxygenServer.addTask(task);
                }
                break;
            case MINECRAFT:
                if (caller == CallingThread.MINECRAFT) {
                    task.run();
                } else {
                    MinecraftCommon.delegateToServerThread(task);
                }
                break;
            case DATA_BASE:
                executor.submit(task);
                break;
        }
    }

    private void applyResult(@Nullable Consumer<Long> consumer, @Nullable Long result, CurrencySource source,
                             CallingThread listener) {
        if (consumer == null) return;
        switch (listener) {
            case OXYGEN:
                if (source == CurrencySource.OXYGEN) {
                    consumer.accept(result);
                } else {
                    OxygenServer.addTask(() -> consumer.accept(result));
                }
                break;
            case MINECRAFT:
                if (source == CurrencySource.MINECRAFT) {
                    consumer.accept(result);
                } else {
                    MinecraftCommon.delegateToServerThread(() -> consumer.accept(result));
                }
                break;
            case OTHER:
                consumer.accept(result); // consumer should handle synchronization on its own
                break;
        }
    }

    /* oxygen economy stuff */

    public void playerLoggedIn(UUID playerUUID, String username) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        if (wallet == null) {
            wallet = new PlayerWallet(playerUUID);
            playersMap.put(playerUUID, wallet);
            OxygenServer.loadPersistentData(wallet);
        }

        for (CurrencyProvider provider : providersMap.values()) {
            queryBalance(playerUUID, username, provider.getIndex(),
                    balance -> {
                        if (balance != null) {
                            OxygenServer.updateWatcherValue(playerUUID, provider.getIndex(), balance);
                        }
                    },
                    CallingThread.OXYGEN);
        }
    }

    @Nullable
    public PlayerWallet getPlayerWallet(UUID playerUUID) {
        return playersMap.get(playerUUID);
    }

    public void save() {
        for (PlayerWallet wallet : playersMap.values()) {
            if (wallet.isChanged()) {
                wallet.resetChangedMark();
                OxygenServer.savePersistentData(wallet);
            }
        }
    }
}
