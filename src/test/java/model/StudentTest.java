package model;

import br.ufal.ic.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private static final ObjectMapper objectMapper = Jackson.newObjectMapper();//TODO test model
    private Student student;

    @BeforeEach
    void setUp(){
        student = null;
    }

    @Test
    void testCreate(){
        assertNull(student);

        String student1 = "teste";
        String scode1 = "code123";

        student = new Student(student1, scode1);

        assertEquals(student1, student.getName());
        assertEquals(scode1, student.getCode());
    }

//    @Test
//    public void testSerializeJson() throws Exception {
//        student = new Student("teste", "c123456");
//
//        final String expected = objectMapper.writeValueAsString(
//                objectMapper.readValue(fixture("fixtures/student.json"), Student.class)
//        );
//
//        assertEquals(expected, objectMapper.writeValueAsString(student));
//    }
}
