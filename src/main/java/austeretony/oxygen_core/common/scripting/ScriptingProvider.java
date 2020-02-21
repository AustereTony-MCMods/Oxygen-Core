package austeretony.oxygen_core.common.scripting;

public class ScriptingProvider {

    private static ScriptingAdapter adapter;

    public static void registerAdapter(ScriptingAdapter scriptingAdapter) {
        adapter = scriptingAdapter;
    }

    public static ScriptingAdapter getAdapter() {
        return adapter;
    }

    public static Shell createShell(Object... args) {
        return adapter.createShell(args);
    }
}
