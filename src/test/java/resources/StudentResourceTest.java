package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Student;
import br.ufal.ic.resources.StudentResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentResourceTest{

    private GenericDAO dao = mock(GenericDAO.class);
    private StudentResource resource = new StudentResource(dao);

    public ResourceExtension RULE = ResourceExtension.builder()
            .addResource(resource)
            .build();


    private Student expected;
    @BeforeEach
    @SneakyThrows
    public void setUp() {

        expected = new Student("eric2", "c789123");
        FieldUtils.writeField(expected, "id", 12L, true);

        when(dao.get(Student.class, expected.getId())).thenReturn(expected);
    }
    @AfterEach
    public void tearDown(){
        reset(dao);
    }
    @Test
    public void testGet(){

        Student saved = RULE.target("/student/" + expected.getId()).request().get(Student.class);

        assertNotNull(saved);
        assertEquals(expected.getName(), saved.getName());
        assertEquals(expected.getId(), saved.getId());
    }
    @Test
    public void testStudentNotFound(){
        when(dao.get(Student.class,10)).thenReturn(null);

        Response response = RULE.client().target("/student/10").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    @Test
    public void testAddStudent(){
        final Form form = new Form()
            .param("name", "eric123")
            .param("code", "c456123");
        Response response = RULE.client().target("/student/create").request().post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);
    }
    @Test
    public void testFindAll(){
        when(dao.findAll("br.ufal.ic.model.Student.findAll")).thenReturn(
                Arrays.asList(
                        new Student("eric123", "c123465"),
                        new Student("eric456", "c465789")
                )
        );

        Response response = RULE.client().target("/student").request().get();

        assertNotNull(response);
    }
    @Test
    public void testFindAllEmpty(){
        when(dao.findAll("br.ufal.ic.model.Student.findAll")).thenReturn(null);

        Response response = RULE.client().target("/student").request().get();

        assertNotNull(response);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        System.out.println(response.getHeaders());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);

    }
}
