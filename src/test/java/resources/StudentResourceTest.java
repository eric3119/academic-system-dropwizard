package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.SecretaryType;
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
    private Secretary secretary;
    private Department department;
    @BeforeEach
    @SneakyThrows
    public void setUp() {

        secretary = new Secretary(SecretaryType.PostGraduation);
        FieldUtils.writeField(secretary, "id", 5L, true);
        when(dao.get(Secretary.class, secretary.getId())).thenReturn(secretary);

        department = new Department("deptTest",secretary);
        FieldUtils.writeField(department, "id", 5L, true);
        when(dao.get(Department.class, department.getId())).thenReturn(department);

        expected = new Student("eric", "c789123", department, secretary, 0);
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
        when(dao.get(Student.class,10L)).thenReturn(null);

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
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);

        form.param("id_department", String.valueOf(department.getId()))
                .param("id_secretary", String.valueOf(secretary.getId()));

        response = RULE.client().target("/student/create").request().post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);
    }
    @Test
    public void testFindAll(){
        when(dao.findAll(Student.class)).thenReturn(
                Arrays.asList(
                        new Student("eric123", "c123465", new Department(), new Secretary(SecretaryType.PostGraduation), 0),
                        new Student("eric456", "c465789", new Department(), new Secretary(SecretaryType.PostGraduation), 0)
                )
        );

        Response response = RULE.client().target("/student").request().get();

        assertNotNull(response);
    }
    @Test
    public void testFindAllEmpty(){
        when(dao.findAll(Student.class)).thenReturn(null);

        Response response = RULE.client().target("/student").request().get();

        assertNotNull(response);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);

    }
}
