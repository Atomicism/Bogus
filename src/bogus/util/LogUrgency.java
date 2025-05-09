package bogus.util;

public enum LogUrgency {
    LOG("LOG"),
    WARNING("WARN"),
    ERROR("ERROR"),
    CRITICAL("CRITICAL");

    public String str;

    LogUrgency(String name){
        this.str = name;
    }
}
