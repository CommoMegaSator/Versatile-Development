package versatile_development.exception;

public class EmptyUserDataException extends RuntimeException {

    public EmptyUserDataException(){
        super("User data is empty.");
    }

    public EmptyUserDataException(String errorMessage){
        super(errorMessage);
    }
}
