import br.ufal.ic.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

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

    //TODO teste
//    @Test
//    public void getAllStudents() {
//
//        Client client = null;
//        final Response response = client.target
//                .path("/student")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .get();
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//
//        final List<Student> result = response.readEntity(new GenericType<List<Student>>() {});
//
//        assertTrue(result.size() > 0);
//    }
}
