package bogus.mod;

import java.nio.file.*;
import java.util.*;
import javax.script.*;

import bogus.core.*;
import bogus.util.*;
import bogus.util.Files;

public class ScriptModLoader extends Loader {
    public static ScriptEngineManager manager = new ScriptEngineManager();
    public static ScriptEngine engine = manager.getEngineByName("nashorn");

    public static String scriptsDirectory = "script-test";

    @Override
    public void load() {
        logger.log("Beginning script loading");
        // this.bindings();

        // logger.log("Loading scripts");
        // logger.log("Working dir: " + new java.io.File(".").getAbsolutePath());

        List<ScriptMod> mods = loadAllMods(scriptsDirectory);

        for (ScriptMod mod : mods) {
            logger.log("Loading mod: " + mod.name);

            for (Path script : mod.scripts) {
                try {
                    String scriptCode;

                    if (mod.isZip) {
                        scriptCode = Files.readFromZip(mod.sourcePath.toString(), script.toString().replace("\\", "/"));
                    } else {
                        scriptCode = Files.read(script.toString());
                    }

                    bindings(mod.name);

                    engine.eval(scriptCode);
                    logger.log("Loaded script: " + script.getFileName());                    
                    // Optional onLoad() call
                    if (engine instanceof Invocable) {
                        Invocable inv = (Invocable) engine;
                        try {
                            inv.invokeFunction("onLoad");
                            // logger.log("Called onLoad in: " + script.getFileName());
                        } catch (NoSuchMethodException ignored) {

                        }
                    }                    
                } catch (Exception e) {
                    logger.err("Error loading script: " + script.getFileName());
                }
            }
        }
    }

    private List<ScriptMod> loadAllMods(String modPath) {
        List<ScriptMod> mods = new ArrayList<>();

        java.io.File folder = new java.io.File(modPath);
        if (!folder.exists() || !folder.isDirectory()) {
            logger.log("Mod directory does not exist");
            return mods;
        }

        for (java.io.File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isDirectory()) {
                mods.add(new ScriptMod(f.getPath()));
            } else if (f.getName().endsWith(".zip") || f.getName().endsWith(".jar")) {
                mods.add(new ScriptMod(f.getPath(), true));
            }
        }

        return mods;
    }

    public void bindings(String name){
        engine.put("logger", new Logger(name));

        // TODO: When a global game state is added this needs to updated to allow modifications
        try {
            engine.eval("var Vec2D = Java.type('bogus.math.Vec2D');");
            engine.eval("var Files = Java.type('bogus.util.Files');");
            engine.eval("var Pair = Java.type('bogus.data.Pair');");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
