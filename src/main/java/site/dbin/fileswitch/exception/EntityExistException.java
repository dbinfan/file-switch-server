package site.dbin.fileswitch.exception;

public class EntityExistException extends RuntimeException {

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return (entity)
                + " with " + field + " "+ val + " existed";
    }
}
