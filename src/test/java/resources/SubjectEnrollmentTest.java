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
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SubjectEnrollmentTest {

    private static final String SEMESTER = "2019.1";
    private GenericDAO dao = mock(GenericDAO.class);
    private SubjectEnrollmentResource resource = new SubjectEnrollmentResource(dao);
    private ArgumentCaptor<SubjectEnrollment> argumentCaptor = ArgumentCaptor.forClass(SubjectEnrollment.class);

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
    private AcademicOffer requiredOffer;
    private AcademicOffer toEnrollOffer;
    private Professor professor;
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

        professor = new Professor("proftest", "c123456", department);
        FieldUtils.writeField(professor, "id", 1L, true);
        when(dao.get(Professor.class, professor.getId())).thenReturn(professor);

        requiredOffer = new AcademicOffer(SEMESTER, professor, subjectRequired);
        FieldUtils.writeField(requiredOffer, "id", 1L, true);
        when(dao.get(AcademicOffer.class, requiredOffer.getId())).thenReturn(requiredOffer);

        toEnrollOffer = new AcademicOffer(SEMESTER, professor, subjectToEnroll);
        FieldUtils.writeField(toEnrollOffer, "id", 2L, true);
        when(dao.get(AcademicOffer.class, toEnrollOffer.getId())).thenReturn(toEnrollOffer);

        when(dao.findAll(AcademicOffer.class)).thenReturn(Arrays.asList(requiredOffer, toEnrollOffer));

        subjectEnrollment = new SubjectEnrollment(studentPost, requiredOffer);
        subjectEnrollmentList = Collections.singletonList(subjectEnrollment);
        when(dao.findAll(SubjectEnrollment.class)).thenReturn(subjectEnrollmentList);
    }
    @AfterEach
    public void tearDown(){
        reset(dao);
    }

    @Test
    public void testEnrollStudent(){
        final SubjectEnrollment subjectEnrollment = new SubjectEnrollment(studentPost, toEnrollOffer);
        when(dao.persist(SubjectEnrollment.class, subjectEnrollment)).thenReturn(subjectEnrollment);

        Form form = new Form()
                .param("id_student", studentPost.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        final Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request().post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        verify(dao).persist(eq(SubjectEnrollment.class), argumentCaptor.capture());
    }

    @Test
    void testStudentNotFound(){
        Long id = new Random().nextLong();
        Form form = new Form()
                .param("id_student", id.toString())
                .param("id_subject", id.toString());
        when(dao.get(Student.class, id)).thenReturn(null);

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testSubjectNotFound(){
        Random random = new Random();
        Long id = random.nextLong();

        Form form = new Form()
                .param("id_student", studentPost.getId().toString())
                .param("id_subject", id.toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

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

        Form form = new Form()
                .param("id_student", s1.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());
        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

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

        Form form = new Form()
                .param("id_student", s1.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

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
        SubjectEnrollment subjectEnrollment2 = new SubjectEnrollment(s1, toEnrollOffer);
        when(dao.get(SubjectEnrollment.class, subjectEnrollment.getId())).thenReturn(subjectEnrollment);
        subjectEnrollmentList = Arrays.asList(subjectEnrollment, subjectEnrollment2);
        when(dao.findAll(SubjectEnrollment.class)).thenReturn(subjectEnrollmentList);

        Form form = new Form()
                .param("id_student", s1.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testStudentPostGradException(){

        subjectToEnroll = new Subject("subjPostGradTest","c159753", 123, studentPost.getCredits(), Collections.singletonList(subjectRequired), department, secretaryGrad);
        try {
            FieldUtils.writeField(subjectToEnroll, "id", 56L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Subject.class, subjectToEnroll.getId())).thenReturn(subjectToEnroll);
        when(dao.findAll(Subject.class)).thenReturn(Arrays.asList(subjectRequired, subjectToEnroll));

        AcademicOffer academicOffer = new AcademicOffer(SEMESTER, professor, subjectToEnroll);
        when(dao.findAll(AcademicOffer.class)).thenReturn(Arrays.asList(academicOffer, requiredOffer));

        Form form = new Form()
                .param("id_student", studentPost.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testSubjectNotOffer(){

        subjectToEnroll = new Subject("subjNotOffer","c159753", 123, studentPost.getCredits(), Collections.singletonList(subjectRequired), department, secretaryGrad);
        try {
            FieldUtils.writeField(subjectToEnroll, "id", 56L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(Subject.class, subjectToEnroll.getId())).thenReturn(subjectToEnroll);
        when(dao.findAll(Subject.class)).thenReturn(Arrays.asList(subjectRequired, subjectToEnroll));

        AcademicOffer academicOffer = new AcademicOffer("2018.2", professor, subjectToEnroll);
        when(dao.findAll(AcademicOffer.class)).thenReturn(Arrays.asList(academicOffer, requiredOffer));

        Form form = new Form()
                .param("id_student", studentPost.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

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

        Form form = new Form()
                .param("id_student", studentGrad.getId().toString())
                .param("id_subject", subjectToEnroll.getId().toString());

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void testEmptyForm(){
        Form form = new Form()
                .param("id_student", null)
                .param("id_subject", null);

        Response response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

        form = new Form()
                .param("id_student", "0")
                .param("id_subject", null);

        response = RULE.target("/enrollstudent/"+SEMESTER)
                .request()
                .post(Entity.form(form));

        assertNotNull(response);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

    }
}
