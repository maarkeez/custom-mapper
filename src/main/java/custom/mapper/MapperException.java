package custom.mapper;

public class MapperException extends Exception {
    public MapperException(String message, Throwable e) {
        super(message, e);
    }

    public MapperException(String message) {
        super(message);
    }
}
