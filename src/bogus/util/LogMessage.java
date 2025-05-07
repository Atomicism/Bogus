package bogus.util;

public class LogMessage {
    public LogUrgency urgency;
    public String message;
    public String source;

    public LogMessage(LogUrgency urgency, String source, String message){
        this.urgency = urgency;
        this.source = source;
        this.message = message;
    }

    public LogMessage(String source, String message){
        this(LogUrgency.LOG, source, message);
    }

    public LogMessage(String message){
        this(LogUrgency.LOG, "Unknown", message);
    }

    public String toString(){
        return String.format("[%s][%s] %s", urgency.str, source, message);
    }
}
