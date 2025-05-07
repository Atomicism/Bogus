package tests;

public abstract class Test {
    public String name;

    public abstract void run();

    public Test(String name){
        this.name = name;
    }
}
