package bogus.mod;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

import bogus.core.*;
import bogus.util.*;

public class ModLoader extends Loader {

    Logger logger = new Logger(loaderName);

    public void load() {
        File modsDir = new File("mod-test");
        File[] files = modsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null){
            logger.log("No mods found"); 
            return;
        }

        try {
            for (File jar : files) { // hopefully never needs debugging
                logger.log("Loading " + jar.getName());
                URL jarUrl = jar.toURI().toURL();
                URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

                try (JarFile jarFile = new JarFile(jar)) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class")) {
                            String className = entry.getName()
                                .replace("/", ".")
                                .replace(".class", "");

                            Class<?> clazz = Class.forName(className, true, loader);

                            if (Mod.class.isAssignableFrom(clazz)) {
                                Mod mod = (Mod) clazz.getDeclaredConstructor().newInstance();
                                mod.onLoad();
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            logger.log("Mod loading failed", LogUrgency.CRITICAL);
        }
    }
}
