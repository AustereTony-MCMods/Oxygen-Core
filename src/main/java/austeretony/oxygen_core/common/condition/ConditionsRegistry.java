package austeretony.oxygen_core.common.condition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ConditionsRegistry {

    private static final Map<String, Class<? extends Condition>> CONDITIONS = new HashMap<>();

    public static void registerCondition(String id, Class<? extends Condition> condition) {
        CONDITIONS.put(id, condition);
    }

    @Nullable
    public static Class<? extends Condition> getConditionClass(String id) {
        return CONDITIONS.get(id);
    }
}
