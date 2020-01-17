package austeretony.oxygen_core.client.currency;

import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class OxygenVouchersCurrencyProperties extends AbstractCurrencyProperties {

    public OxygenVouchersCurrencyProperties() {
        super(
                "oxygen_core.currency.vouchers", 
                new ResourceLocation(OxygenMain.MODID, "textures/gui/currency/voucher.png"), 
                8, 
                8, 
                0, 
                0);
    }

    @Override
    public int getIndex() {
        return 2;
    }
}
