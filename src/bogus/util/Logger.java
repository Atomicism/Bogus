package bogus.util;

public class Logger {

    String name;
    LogUrgency defaultUrgency;

    public void log(String message, LogUrgency urgency){
        LogMessage toLog = new LogMessage(urgency, name, message);
        Log.messages.add(toLog);
        System.out.println(toLog);

        if(urgency == LogUrgency.CRITICAL){
            // TODO: This likely needs to be changed into a more standard quit function
            System.exit(1);
        }
    }

    public void log(String message){
        LogMessage toLog = new LogMessage(defaultUrgency, name, message);
        Log.messages.add(toLog);
        System.out.println(toLog);

        if(defaultUrgency == LogUrgency.CRITICAL){
            // TODO: This likely needs to be changed into a more standard quit function
            System.exit(1);
        }
    }

    public void warn(String message){
        LogMessage toLog = new LogMessage(LogUrgency.WARNING, name, message);
        Log.messages.add(toLog);
        System.out.println(toLog);
    }

    public void err(String message){
        LogMessage toLog = new LogMessage(LogUrgency.ERROR, name, message);
        Log.messages.add(toLog);
        System.out.println(toLog);
    }

    public void critical(String message){
        LogMessage toLog = new LogMessage(LogUrgency.CRITICAL, name, message);
        Log.messages.add(toLog);
        System.out.println(toLog);
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