package fr.jerome.springbootrestserverapi.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
