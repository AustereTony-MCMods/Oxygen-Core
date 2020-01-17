package austeretony.oxygen_core.client.currency;

import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class OxygenShardsCurrencyProperties extends AbstractCurrencyProperties {

    public OxygenShardsCurrencyProperties() {
        super(
                "oxygen_core.currency.shards", 
                new ResourceLocation(OxygenMain.MODID, "textures/gui/currency/shard.png"), 
                8, 
                8, 
                0, 
                0);
    }

    @Override
    public int getIndex() {
        return 1;
    }
}
