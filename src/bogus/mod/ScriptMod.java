package bogus.mod;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bogus.util.Files;

public class ScriptMod {
    public String name;
    public List<Path> scripts = new ArrayList<>();
    public List<Path> assets = new ArrayList<>();
    public boolean isZip = false;
    public Path sourcePath;

    public ScriptMod(String pathStr) {
        this(pathStr, false);
    }

    public ScriptMod(String pathStr, boolean zip) {
        this.isZip = zip;
        this.sourcePath = Paths.get(pathStr);
        this.name = sourcePath.getFileName().toString();

        if (zip) {
            this.scripts = Files.listZipScripts(sourcePath);
        } else {
            try (Stream<Path> walk = java.nio.file.Files.walk(sourcePath)) {
                this.scripts = walk
                    .filter(p -> p.toString().endsWith(".js"))
                    .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
