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

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private Student studentPost;
    private Secretary secretaryPost;
    private Secretary secretaryGrad;
    private Department department;
    private Subject subjectToEnroll;
    private Subject subjectRequired;
    private SubjectEnrollment subjectEnrollment;
    private List<Student> studentList;
    private List<Subject> subjectList;
    private List<SubjectEnrollment> subjectEnrollmentList;
    @BeforeEach
    @SneakyThrows
    public void setUp() {
        secretaryPost = new Secretary(SecretaryType.PostGraduation);
        secretaryGrad = new Secretary(SecretaryType.Graduation);
        FieldUtils.writeField(secretaryPost, "id", 5L, true);
        when(dao.get(Secretary.class, secretaryPost.getId())).thenReturn(secretaryPost);
        FieldUtils.writeField(secretaryGrad, "id", 6L, true);
        when(dao.get(Secretary.class, secretaryGrad.getId())).thenReturn(secretaryGrad);

        department = new Department("deptTest", secretaryPost);
        FieldUtils.writeField(department, "id", 5L, true);
        when(dao.get(Department.class, department.getId())).thenReturn(department);

        studentPost = new Student("eric", "c789123", department, secretaryPost, 153);
        FieldUtils.writeField(studentPost, "id", 12L, true);
        when(dao.get(Student.class, studentPost.getId())).thenReturn(studentPost);
        studentList = Collections.singletonList(studentPost);
        when(dao.findAll(Student.class)).thenReturn(studentList);

        subjectRequired = new Subject("subjRequired","c159553", 123, studentPost.getCredits(), new ArrayList<>(), department, secretaryPost);
        FieldUtils.writeField(subjectRequired, "id", 54L, true);
        when(dao.get(Subject.class, subjectRequired.getId())).thenReturn(subjectRequired);

        subjectToEnroll = new Subject("subjTest","c159753", 123, studentPost.getCredits(), Collections.singletonList(subjectRequired), department, secretaryPost);
        FieldUtils.writeField(subjectToEnroll, "id", 55L, true);
        when(dao.get(Subject.class, subjectToEnroll.getId())).thenReturn(subjectToEnroll);

        subjectList = Arrays.asList(subjectRequired, subjectToEnroll);
        when(dao.findAll(Subject.class)).thenReturn(subjectList);

        subjectEnrollment = new SubjectEnrollment(subjectRequired, studentPost);
        subjectEnrollmentList = Collections.singletonList(subjectEnrollment);
        when(dao.findAll(SubjectEnrollment.class)).thenReturn(subjectEnrollmentList);
    }
    @AfterEach
    public void tearDown(){
        reset(dao);
    }


    @Test
    public void testListStudents(){
        final List<Student> studentList = Collections.singletonList(studentPost);
        when(dao.findAll(Student.class)).thenReturn(studentList);

        final List<Student> response = RULE.target("/enrollsubject")
                .request().get(new GenericType<List<Student>>() {
                });
        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.containsAll(studentList));
    }

    @Test
    public void testListSubjects(){


        final List<Subject> response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .request().get(new GenericType<List<Subject>>() {
                });
        assertNotNull(response);
        assertEquals(subjectList.size(), response.size());
        assertTrue(response.containsAll(subjectList));
    }

    @Test
    public void testEnrollStudent(){
        final SubjectEnrollment subjectEnrollment = new SubjectEnrollment(subjectToEnroll, studentPost);
        when(dao.persist(SubjectEnrollment.class, subjectEnrollment)).thenReturn(subjectEnrollment);

        final SubjectEnrollment response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request().get(SubjectEnrollment.class);

        assertNotNull(response);
        assertEquals(subjectEnrollment, response);
    }

    @Test
    void testEnrollStudentStatusCodes(){

        Response response = RULE.target("/enrollsubject").request().get();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        SubjectEnrollment newEnrollment = new SubjectEnrollment(subjectToEnroll, studentPost);
        when(dao.persist(SubjectEnrollment.class, newEnrollment))
        .thenReturn(newEnrollment);

        response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
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
    }

    @Test
    void testSubjectNotFound(){
        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .queryParam("id_subject", -1)
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentNotEnoughCredits(){

        Student s1 = new Student("eric", "c789123", department, secretaryPost, 0);
        try {
            FieldUtils.writeField(s1, "id", 15L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Student.class, s1.getId())).thenReturn(s1);

        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", s1.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentSubjectsNotSatisfied(){

        Student s1 = new Student("studentNoSubjects", "c789123", department, secretaryPost, subjectToEnroll.getMin_credits());
        try {
            FieldUtils.writeField(s1, "id", 15L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Student.class, s1.getId())).thenReturn(s1);

        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", s1.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentAlreadyEnrolled(){
        Student s1 = new Student("studentNoSubjects", "c789123", department, secretaryPost, subjectToEnroll.getMin_credits());
        try {
            FieldUtils.writeField(s1, "id", 15L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Student.class, s1.getId())).thenReturn(s1);
        SubjectEnrollment subjectEnrollment2 = new SubjectEnrollment(subjectToEnroll, s1);
        when(dao.get(SubjectEnrollment.class, subjectEnrollment.getId())).thenReturn(subjectEnrollment);
        subjectEnrollmentList = Arrays.asList(subjectEnrollment, subjectEnrollment2);
        when(dao.findAll(SubjectEnrollment.class)).thenReturn(subjectEnrollmentList);

        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", s1.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentPostGradException(){

        subjectToEnroll = new Subject("subjTest","c159753", 123, studentPost.getCredits(), Collections.singletonList(subjectRequired), department, secretaryGrad);
        try {
            FieldUtils.writeField(subjectToEnroll, "id", 56L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Subject.class, subjectToEnroll.getId())).thenReturn(subjectToEnroll);

        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentPost.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
    @Test
    void testStudentGradMinCreditsException(){

        Student studentGrad = new Student("eric", "c789123", department, secretaryGrad, subjectToEnroll.getMin_credits());
        try {
            FieldUtils.writeField(studentGrad, "id", 14L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Student.class, studentGrad.getId())).thenReturn(studentGrad);
        studentList = Arrays.asList(studentPost, studentGrad);
        when(dao.findAll(Student.class)).thenReturn(studentList);

        Response response = RULE.target("/enrollsubject")
                .queryParam("id_student", studentGrad.getId())
                .queryParam("id_subject", subjectToEnroll.getId())
                .request()
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}
