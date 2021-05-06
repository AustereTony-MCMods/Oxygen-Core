package austeretony.oxygen_core.server.currency;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.OxygenManagerServer;

import javax.annotation.Nullable;
import java.util.UUID;

public class CoinsProvider implements CurrencyProvider {

    @Override
    public String getName() {
        return "oxygen_coins";
    }

    @Override
    public int getIndex() {
        return OxygenMain.CURRENCY_COINS;
    }

    @Override
    public CurrencySource getSource() {
        return CurrencySource.OXYGEN;
    }

    @Override
    public boolean isForcedSync() {
        return false;
    }

    @Override
    public Long getBalance(UUID playerUUID, String username) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        return wallet != null ? wallet.getBalance(OxygenMain.CURRENCY_COINS) : null;
    }

    @Override
    public Long setBalance(UUID playerUUID, String username, long value) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        if (wallet != null) {
            wallet.setBalance(OxygenMain.CURRENCY_COINS, value);
            return value;
        }
        return null;
    }

    @Override
    public Long incrementBalance(UUID playerUUID, String username, long increment) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        if (wallet != null) {
            long newBalance = wallet.getBalance(OxygenMain.CURRENCY_COINS) + increment;
            wallet.setBalance(OxygenMain.CURRENCY_COINS, newBalance);
            return newBalance;
        }
        return null;
    }

    @Override
    public Long decrementBalance(UUID playerUUID, String username, long decrement) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        if (wallet != null) {
            long newBalance = wallet.getBalance(OxygenMain.CURRENCY_COINS) - decrement;
            wallet.setBalance(OxygenMain.CURRENCY_COINS, newBalance);
            return newBalance;
        }
        return null;
    }

    @Override
    public void updated(UUID playerUUID, String username, Long balance) {
        PlayerWallet wallet = getPlayerWallet(playerUUID);
        if (wallet != null) {
            wallet.markChanged();
        }
    }

    @Nullable
    private PlayerWallet getPlayerWallet(UUID playerUUID) {
        return OxygenManagerServer.instance().getCurrencyManager().getPlayerWallet(playerUUID);
    }
}
