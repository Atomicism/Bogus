package bogus.util;

public class BogusRuntimeException extends RuntimeException{
    private static final long serialVersionUID = 6735854402467673117L;

    public BogusRuntimeException(String message){
        super(message);
    }

    public BogusRuntimeException(Throwable t){
        super(t);
    }

    public BogusRuntimeException(String message, Throwable t){
        super(message, t);
    }
}
