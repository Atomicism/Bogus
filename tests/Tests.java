package tests;

import tests.bogus.FilesTest;
import tests.bogus.LoggerTest;
import tests.bogus.Vec2DTest;

public class Tests {

    static Test[] tests = {
        new LoggerTest("LoggerTest"),
        new FilesTest("FilesTest"),
        new Vec2DTest("Vec2DTest")
    };

    public static void main(String[] args) {
        for(Test t : tests){
            System.out.println("\n" + t.name);
            t.run();
        }
    }
}