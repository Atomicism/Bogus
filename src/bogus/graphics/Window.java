package bogus.graphics;

import java.awt.*;
import java.awt.event.*;

public class Window extends Frame {
    public Window(String name, int xSize, int ySize, int xpos, int ypos){
        super(name);
        this.setSize(xSize, ySize);
        this.setLocation(xpos, ypos);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
    }
    
    public Window(String name){
        super(name);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
    }

    public Window(){
        super();
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
    }
}
