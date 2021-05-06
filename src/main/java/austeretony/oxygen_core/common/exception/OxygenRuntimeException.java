package austeretony.oxygen_core.common.exception;

public class OxygenRuntimeException extends RuntimeException {

    public OxygenRuntimeException() {
        super();
    }

    public OxygenRuntimeException(String message) {
        super(message);
    }

    public OxygenRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OxygenRuntimeException(Throwable cause) {
        super(cause);
    }
}
