package jp.kobespiral.yuya.todo.exception;

public class ToDoAppException extends RuntimeException {
    public static final int NO_SUCH_MEMBER_EXISTS = 101;
    public static final int MEMBER_ALREADY_EXISTS = 102;
    public static final int INVALID_MEMBER_INFO = 103;
    public static final int INVALID_MEMBER_OPERATION = 104;
    public static final int NO_SUCH_TODO_EXISTS = 201;
    public static final int INVALID_TODO_INFO = 202;
    public static final int INVALID_TODO_OPERATION = 203;
    int code;

    public ToDoAppException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ToDoAppException(int code, String message, Exception cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}