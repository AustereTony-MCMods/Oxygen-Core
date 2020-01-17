package austeretony.oxygen_core.client.currency;

import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class OxygenCoinsCurrencyProperties extends AbstractCurrencyProperties {

    public OxygenCoinsCurrencyProperties() {
        super(
                "oxygen_core.currency.coins", 
                new ResourceLocation(OxygenMain.MODID, "textures/gui/currency/coin.png"), 
                8, 
                8, 
                0, 
                0);
    }

    @Override
    public int getIndex() {
        return OxygenMain.COMMON_CURRENCY_INDEX;
    }
}
