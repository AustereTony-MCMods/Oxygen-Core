package austeretony.oxygen_core.common.scripting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

/**
 * Temporary class used to wrap script file content for direct compilation.
 * Holds file name and script text represented as java String.
 * 
 * @author AustereTony
 */
public class ScriptWrapper {

    private final String name, scriptText;

    public ScriptWrapper(String name, String scriptText) {
        this.name = name;
        this.scriptText = scriptText;
    }

    public String getName() {
        return this.name;
    }

    public String getScriptText() {
        return this.scriptText;
    }

    @Nullable
    public static ScriptWrapper fromFile(String pathToFile, String name) throws IOException {
        Path path = Paths.get(pathToFile);
        if (Files.exists(path))
            return new ScriptWrapper(name, new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        return null;
    }
}
