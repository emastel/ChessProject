package service;

public class AlreadyTakenException extends Exception{
    public AlreadyTakenException(String message){
        super(message);
    }
}
