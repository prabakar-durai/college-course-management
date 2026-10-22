import java.util.List;

public interface Teachable {
    void assignCourse(Course course);
    List<Course> getAssignedCourses();
}
