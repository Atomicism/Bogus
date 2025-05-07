package tests.bogus;

import bogus.util.Files;

import tests.Test;

public class FilesTest extends Test {
    public FilesTest(String name){
        super(name);
    }

    @Override
    public void run() {
        System.out.println(Files.read("README.md").split("\n")[0]);
    }
    
}
