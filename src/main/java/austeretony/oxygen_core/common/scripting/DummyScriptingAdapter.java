package austeretony.oxygen_core.common.scripting;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.main.OxygenMain;

public class DummyScriptingAdapter implements ScriptingAdapter {

    @Override
    public Shell createShell(Object... args) {
        return new Shell() {

            @Override
            public void put(String name, Object object) {}

            @Override
            @Nullable
            public Object get(String name) {
                return null;
            }

            @Override
            @Nullable
            public Object evaluate(String script, String fileName) {
                OxygenMain.LOGGER.info("Scripting adapter is missing!");
                return null;
            }
        };
    }
}
