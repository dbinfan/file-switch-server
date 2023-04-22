package site.dbin.fileswitch.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String field, String val) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return (entity)
                + " with " + field + " "+ val + " does not exist";
    }
}
