package bogus.util;

public class Logger {

    String name;

    public void log(String message, LogUrgency urgency){
        System.out.println(String.format("[%s from %s] %s", urgency.str, this.name, message));
    }

    public Logger(String name){
        this.name = name;
    }
}