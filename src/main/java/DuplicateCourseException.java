public class DuplicateCourseException extends RuntimeException {

    public DuplicateCourseException(String code) {
        super("Course already exists: " + code);
    }
}