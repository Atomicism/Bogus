package tests;

import tests.bogus.FilesTest;
import tests.bogus.LoggerTest;

public class Tests {

    static Test[] tests = {
        new LoggerTest("LoggerTest"),
        new FilesTest("FilesTest"),
    };

    public static void main(String[] args) {
        for(Test t : tests){
            System.out.println("\n" + t.name);
            t.run();
        }
    }
}
