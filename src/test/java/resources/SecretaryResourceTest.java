package resources;


import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.*;
import br.ufal.ic.resources.SecretaryResource;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SecretaryResourceTest {

    private GenericDAO dao = mock(GenericDAO.class);
    private SecretaryResource resource = new SecretaryResource(dao);

    public ResourceExtension RULE = ResourceExtension.builder()
            .addResource(resource)
            .build();

    private Secretary expected;
    private Department department;
    private Subject subject;
    private Professor professor;
    @BeforeEach
    @SneakyThrows
    public void setUp() {
        expected = new Secretary(
                SecretaryType.values()[new Random().nextInt(SecretaryType.values().length)]
        );
        FieldUtils.writeField(expected, "id", 1L, true);
        when(dao.get(Secretary.class,1L)).thenReturn(expected);

        department = new Department("deptTest", expected);
        FieldUtils.writeField(department, "id", 5L, true);
        when(dao.get(Department.class, department.getId())).thenReturn(department);

        subject = new Subject("subjTest","c159553", 123, 456, new ArrayList<>(), department, expected);
        FieldUtils.writeField(subject, "id", 54L, true);
        when(dao.get(Subject.class, subject.getId())).thenReturn(subject);
        when(dao.findAll(Subject.class)).thenReturn(Collections.singletonList(subject));

        professor = new Professor("profTest", "c123456", department);
        FieldUtils.writeField(professor, "id", 1L, true);
        when(dao.get(Professor.class, professor.getId())).thenReturn(professor);
    }

    @AfterEach
    public void tearDown(){
        reset(dao);
    }

    @Test
    public void testSecretarySemester(){
        AcademicOffer academicOffer = new AcademicOffer("2019.1", professor, subject);
        try {
            FieldUtils.writeField(academicOffer, "id", 1L, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        when(dao.get(AcademicOffer.class, academicOffer.getId())).thenReturn(academicOffer);

        final List<Subject> response = RULE.target("/secretary/"+expected.getId()+"/"+academicOffer.getId())
                .request().get(new GenericType<List<Subject>>() {});

        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.contains(subject));
    }

    @Test
    public void testSecretaryNotFound(){
        Random random = new Random();
        Long id = random.nextLong();
        when(dao.get(Secretary.class,id)).thenReturn(null);

        Response response = RULE.client().target("/secretary/"+id+"/0").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testNoAcademicOffer(){

        Random random = new Random();
        Long id = random.nextLong();
        when(dao.get(AcademicOffer.class,id)).thenReturn(null);
        when(dao.get(Secretary.class,expected.getId())).thenReturn(expected);

        Response response = RULE.client().target("/secretary/"+expected.getId()+"/"+id).request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindAll(){
        when(dao.findAll(Secretary.class)).thenReturn(Collections.singletonList(expected));

        List<Secretary> response = RULE.client().target("/secretary").request().get(
                new GenericType<List<Secretary>>(){});

        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.contains(expected));
    }
}
