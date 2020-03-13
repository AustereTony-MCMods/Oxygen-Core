package austeretony.oxygen_core.common.scripting.adapter;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.scripting.ScriptingAdapter;
import austeretony.oxygen_core.common.scripting.Shell;

public class DummyScriptingAdapter implements ScriptingAdapter {

    private final Shell dummy = new Shell() {

        @Override
        public void put(String name, Object object) {}

        @Override
        @Nullable
        public Object get(String name) {
            return null;
        }

        @Override
        @Nullable
        public Object evaluate(String script, String fileName, boolean debug) {
            OxygenMain.LOGGER.info("[Core] Scripting adapter is missing!");
            return null;
        }
    };

    @Override
    public Shell createShell(Object... args) {
        return this.dummy;
    }
}
