package austeretony.oxygen_core.common.scripting.adapter;

import javax.annotation.Nullable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.scripting.ScriptingProvider;
import austeretony.oxygen_core.common.scripting.Shell;

public class ECMAScriptShell implements Shell {

    private final ScriptEngine engine;

    public ECMAScriptShell(ScriptEngine engine) {
        this.engine = engine;
    }

    @Override
    public void put(String name, Object object) {
        this.engine.put(name, object);
    }

    @Override
    @Nullable
    public Object get(String name) {
        return this.engine.get(name);
    }

    @Override
    @Nullable
    public Object evaluate(String script, String fileName, boolean debug) {
        Object result = null;
        try {
            if (!debug)
                result = this.engine.eval(script);
            else {
                long start, end;

                start = System.nanoTime();
                result = this.engine.eval(script);
                end = System.nanoTime();

                OxygenMain.LOGGER.info("[Core/ECMAScript Adapter] Executed script <{}> with time: {}.",
                        fileName,
                        ScriptingProvider.formatDeltaTime(end - start));
            }
        } catch (ScriptException exception) {
            OxygenMain.LOGGER.error("[Core/ECMAScript Adapter] Script compilation failure! Script: {}.", fileName);
            exception.printStackTrace();
        }
        return result;
    }
}
