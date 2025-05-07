package bogus.util;

public enum LogUrgency {
    LOG("Log"),
    WARNING("Warn"),
    ERROR("Error"),
    CRITICAL("Critical Error");

    public String str;

    LogUrgency(String name){
        this.str = name;
    }
}