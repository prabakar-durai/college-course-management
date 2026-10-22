import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findByCode(String code);

    List<Course> findAll();

    boolean deleteByCode(String code);
}