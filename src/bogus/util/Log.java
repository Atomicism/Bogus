package bogus.util;

import java.util.ArrayList;
import java.util.List;

public class Log {
    public static List<LogMessage> messages = new ArrayList<>();

    public static void clear(){
        messages = new ArrayList<>();
        messages.add(
            new LogMessage("Log cleared")
        );
    }

    public static String string(){
        String out = "";
        for(LogMessage l : messages){
            out += l.toString();
        }
        return out;
    }
}
