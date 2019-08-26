package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.*;
import br.ufal.ic.resources.StudentResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentResourceTest{

    private GenericDAO dao = mock(GenericDAO.class);
    private StudentResource resource = new StudentResource(dao);
    private ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

    public ResourceExtension RULE = ResourceExtension.builder()
            .addResource(resource)
            .build();


    private Student student;
    private Secretary secretary;
    private Department department;
    private AcademicOffer academicOffer;
    private SubjectEnrollment subjectEnrollment;
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
        when(dao.persist(Student.class, student)).thenReturn(student);
    }
    @AfterEach
    public void tearDown(){
        reset(dao);
    }

    @Test
    public void testEnrollmentProof(){

    }

    @Test
    public void testGet(){

        Student saved = RULE.target("/student/" + student.getId()).request().get(Student.class);

        assertNotNull(saved);
        assertEquals(student.getName(), saved.getName());
        assertEquals(student.getId(), saved.getId());
    }
    @Test
    public void testStudentNotFound(){
        Random random = new Random();
        Long id = random.nextLong();
        when(dao.get(Student.class,id)).thenReturn(null);

        Response response = RULE.client().target("/student/"+id).request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    @Test
    public void testAddStudent(){
        when(dao.persist(eq(Student.class), any(Student.class))).thenReturn(student);
        final Form form = new Form()
            .param("name", student.getName())
            .param("code", student.getCode())
            .param("id_department", String.valueOf(department.getId()))
            .param("id_secretary", String.valueOf(secretary.getId()))
            .param("credits", String.valueOf(student.getCredits()));
        Response response = RULE.client().target("/student/create").request().post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(dao).persist(eq(Student.class), studentCaptor.capture());
    }
    @Test
    public void testFindAll(){

        List<Student> findAllStudents = Arrays.asList(
                new Student("eric123", "c123465", new Department(), new Secretary(SecretaryType.PostGraduation), 0),
                new Student("eric456", "c465789", new Department(), new Secretary(SecretaryType.PostGraduation), 0)
        );

        when(dao.findAll(Student.class)).thenReturn(findAllStudents);

        List<Student> response = RULE.client().target("/student")
                .request().get(new GenericType<List<Student>>() {});

        assertNotNull(response);
        assertEquals(findAllStudents.size(), response.size());
        assertTrue(response.containsAll(findAllStudents));
    }
//    @Test
//    public void testEmptyEnrollmentProof(){
//        when(dao.get(Student.class, expected.getId())).thenReturn(expected);
//
//        Response response = RULE.client().target("/student/"+expected.getId()).request().get();
//        assertNotNull(response);
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//    }
    @Test
    public void testFindAllEmpty(){
        when(dao.findAll(Student.class)).thenReturn(Collections.emptyList());

        List<Student> studentList = RULE.client().target("/student").request().get(
                new GenericType<List<Student>>(){}
        );
        assertNotNull(studentList);
        assertEquals(0, studentList.size());

    }
    @Test
    void testBadRequest(){
        Form form = new Form();
        Response response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        form.param("id_department", String.valueOf(department.getId()));
        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        form.param("id_secretary", String.valueOf(secretary.getId()));
        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        form.param("name", "eric123");
        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        form.param("code", "c456123");
        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        when(dao.get(Secretary.class, secretary.getId())).thenReturn(null);

        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        when(dao.get(Department.class, department.getId())).thenReturn(null);

        response = RULE.client().target("/student/create").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
