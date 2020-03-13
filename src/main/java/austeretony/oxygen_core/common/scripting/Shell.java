package austeretony.oxygen_core.common.scripting;

import javax.annotation.Nullable;

public interface Shell {

    void put(String name, Object object);

    @Nullable
    Object get(String name);

    @Nullable
    Object evaluate(String script, String fileName, boolean debug);
}
