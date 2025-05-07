package bogus.util;

public class Logger {

    String name;
    LogUrgency defaultUrgency;

    public void log(String message, LogUrgency urgency){
        System.out.println(String.format("[%s][%s] %s", urgency.str, this.name, message));
        if(urgency == LogUrgency.CRITICAL){
            // TODO: This likely needs to be changed into a more standard quit function
            System.exit(1);
        }
    }

    public void log(String message){
        System.out.println(String.format("[%s][%s] %s", defaultUrgency.str, this.name, message));
        if(defaultUrgency == LogUrgency.CRITICAL){
            // TODO: This likely needs to be changed into a more standard quit function
            System.exit(1);
        }
    }

    public Logger(String name){
        this.name = name;
        defaultUrgency = LogUrgency.LOG;
    }

    public Logger(String name, LogUrgency defaultUrgency){
        this.name = name;
        this.defaultUrgency = defaultUrgency;
    }
}