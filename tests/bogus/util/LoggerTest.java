package tests.bogus.util;

import bogus.util.*;
import tests.*;

public class LoggerTest extends Test {
    public LoggerTest(String name){
        super(name);
    }

    @Override
    public void run() {
        Logger logger1 = new Logger("TestLogger1");
        Logger logger2 = new Logger("TestLogger2", LogUrgency.ERROR);

        logger1.log("Test Log 1", LogUrgency.LOG);
        logger2.log("Default Urgency Test");
    }
}
