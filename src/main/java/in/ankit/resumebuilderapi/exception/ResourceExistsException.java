package in.ankit.resumebuilderapi.exception;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String messageString){
        super(messageString);
    }
}
