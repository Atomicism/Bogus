package tests.bogus;

import bogus.graphics.*;
import tests.Test;

public class GraphicsTest extends Test {
    public GraphicsTest(String name){
        super(name);
    }
    
    @Override
    public void run(){
        Window winny = new Window("tester", 960, 540, 480,270);
        winny.setVisible(true);
    }
}
