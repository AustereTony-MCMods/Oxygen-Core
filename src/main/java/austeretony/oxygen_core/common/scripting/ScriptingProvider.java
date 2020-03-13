package austeretony.oxygen_core.common.scripting;

import java.util.concurrent.TimeUnit;

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

    public static String formatDeltaTime(long delta) {
        return String.format("%d seconds, %d millis", 
                TimeUnit.NANOSECONDS.toSeconds(delta), 
                TimeUnit.NANOSECONDS.toMillis(delta) % 1000);
    }
}
