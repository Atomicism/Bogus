package bogus.util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.*;
import java.util.zip.*;
import java.util.stream.*;

public class Files {

    /** Reads a plain text file from the filesystem */
    public static String read(String filename) {
        Path path = Paths.get(filename);
        StringBuilder content = new StringBuilder();
        try (Stream<String> stream = java.nio.file.Files.lines(path)) {
            stream.forEach(content::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /** Reads a file from inside a ZIP or JAR file */
    public static String readFromZip(String zipPath, String innerPath) {
        StringBuilder content = new StringBuilder();
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            ZipEntry entry = zipFile.getEntry(innerPath);
            if (entry == null) {
                System.err.println("File not found in zip: " + innerPath);
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /** Lists all .js mod scripts in a folder */
    public static List<Path> listScripts(String modFolderPath) {
        try (Stream<Path> paths = java.nio.file.Files.walk(Paths.get(modFolderPath))) {
            return paths
                .filter(Files::isJavaScriptFile)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Path> listZipScripts(Path zipPath) {
        List<Path> scripts = new ArrayList<>();
        try (FileSystem zipfs = FileSystems.newFileSystem(zipPath, (ClassLoader) null)) {
            for (Path root : zipfs.getRootDirectories()) {
                java.nio.file.Files.walk(root).forEach(path -> {
                    if (path.toString().endsWith(".js")) {
                        scripts.add(path);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scripts;
    }


    /** Simple check to filter for .js files */
    private static boolean isJavaScriptFile(Path path) {
        return java.nio.file.Files.isRegularFile(path) && path.toString().endsWith(".js");
    }

    // List non-script assets in a folder
    public static List<Path> listAssets(String folderPath) {
        try (Stream<Path> paths = java.nio.file.Files.walk(Paths.get(folderPath))) {
            return paths
                .filter(Files::isAssetFile)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<String> listAssetsInZip(String zipPath) {
        List<String> assets = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!entry.isDirectory() && !name.endsWith(".js")) {
                    assets.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assets;
    }

    private static boolean isAssetFile(Path path) {
        return java.nio.file.Files.isRegularFile(path) &&
            !path.toString().endsWith(".js");
    }

}
