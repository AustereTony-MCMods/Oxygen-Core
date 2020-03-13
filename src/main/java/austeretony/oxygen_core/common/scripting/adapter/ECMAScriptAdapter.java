package austeretony.oxygen_core.common.scripting.adapter;

import javax.script.ScriptEngineManager;

import austeretony.oxygen_core.common.scripting.ScriptingAdapter;
import austeretony.oxygen_core.common.scripting.Shell;

public class ECMAScriptAdapter implements ScriptingAdapter {

    private final ScriptEngineManager factory = new ScriptEngineManager();

    @Override
    public Shell createShell(Object... args) {
        return new ECMAScriptShell(this.factory.getEngineByName("nashorn"));
    }
}
