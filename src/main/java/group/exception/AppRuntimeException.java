package group.exception;

public class AppRuntimeException extends RuntimeException {
    int code;
    String msg;

    public AppRuntimeException(ExceptionKind exceptionKind) {
        this.code = exceptionKind.code;
        this.msg = exceptionKind.msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
