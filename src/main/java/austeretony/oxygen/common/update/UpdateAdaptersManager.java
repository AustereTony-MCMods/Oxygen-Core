package austeretony.oxygen.common.update;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen.common.api.update.AbstractUpdateAdapter;

public class UpdateAdaptersManager {

    private static final Map<String, AbstractUpdateAdapter> ADAPTERS = new HashMap<String, AbstractUpdateAdapter>(5);

    public static void register(AbstractUpdateAdapter adapter) {
        ADAPTERS.put(adapter.getModId(), adapter);
    }

    public static boolean isAdaperExist(String modid) {
        return ADAPTERS.containsKey(modid);
    }

    public static AbstractUpdateAdapter get(String modid) {
        return ADAPTERS.get(modid);
    }

    public static void moduleUpdated(String modid, String oldVersion, String newVersion) {
        if (isAdaperExist(modid))
            get(modid).validate(oldVersion, newVersion);
    }

    public static void applyChanges() {
        for (AbstractUpdateAdapter adapter : ADAPTERS.values())
            adapter.applyChanges();
    }
}