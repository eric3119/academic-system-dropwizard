package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.*;
import br.ufal.ic.resources.SubjectEnrollmentResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SubjectEnrollmentTest {

    private GenericDAO dao = mock(GenericDAO.class);
    private SubjectEnrollmentResource resource = new SubjectEnrollmentResource(dao);

    public ResourceExtension RULE = ResourceExtension.builder()
            .addResource(resource)
            .build();

    private Student student;
    private Secretary secretary;
    private Department department;
    private Subject subject;
    @BeforeEach
    @SneakyThrows
    public void setUp() {
        secretary = new Secretary(SecretaryType.PostGraduation);
        FieldUtils.writeField(secretary, "id", 5L, true);
        when(dao.get(Secretary.class, secretary.getId())).thenReturn(secretary);

        department = new Department("deptTest",secretary);
        FieldUtils.writeField(department, "id", 5L, true);
        when(dao.get(Department.class, department.getId())).thenReturn(department);

        student = new Student("eric", "c789123", department, secretary, 0);
        FieldUtils.writeField(student, "id", 12L, true);
        when(dao.get(Student.class, student.getId())).thenReturn(student);

        subject = new Subject("subjTest","c159753", 123, 153, department, secretary);
        FieldUtils.writeField(subject, "id", 55L, true);
        when(dao.get(Subject.class, subject.getId())).thenReturn(subject);
    }
    @AfterEach
    public void tearDown(){
        reset(dao);
    }

    @Test
    void testEnrollStudent(){

        Response response = RULE.target("/enrollsubject").request().get();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);

        response = RULE.target("/enrollsubject")
                .queryParam("id_student", student.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);

        response = RULE.target("/enrollsubject")
                .queryParam("id_student", student.getId())
                .queryParam("id_subject", subject.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentNotFound(){
        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", -1)
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);
    }

    @Test
    void testSubjectNotFound(){
        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", student.getId())
                .queryParam("id_subject", -1)
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("Content-Type"));
        assertEquals(response.getHeaderString("Content-Type"), MediaType.APPLICATION_JSON);
    }
}
