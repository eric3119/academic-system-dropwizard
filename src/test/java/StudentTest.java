import br.ufal.ic.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private Student student;

    @BeforeEach
    void setUp(){
        student = null;
    }

    @Test
    void testAdd(){
        assertNull(student);

        String student1 = "teste";
        String scode1 = "code123";

        student = new Student(student1, scode1);

        assertEquals(student1, student.getName());
        assertEquals(scode1, student.getCode());
    }
}
