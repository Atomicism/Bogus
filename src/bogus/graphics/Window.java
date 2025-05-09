package bogus.graphics;

import bogus.util.*;

import java.awt.*;

public class Window extends Frame {
    static Logger logger = new Logger("loaderName");
    
    public Window(String tital) {
        super(tital);
        logger.log("Making new Window");
    }

}