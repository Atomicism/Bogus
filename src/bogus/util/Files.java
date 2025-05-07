package bogus.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Files {
    public static String read(String filename){
        Path path = Paths.get(filename);
        String lines = "";
        try (Stream<String> stream = java.nio.file.Files.lines(path)) {
            for(Object str : stream.toArray()){
                lines += (String)str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
        return lines;
    }
}
