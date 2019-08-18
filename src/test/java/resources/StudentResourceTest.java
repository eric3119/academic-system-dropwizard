package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Student;
import br.ufal.ic.resources.StudentResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentResourceTest{

    private GenericDAO dao = mock(GenericDAO.class);
    private StudentResource resource = new StudentResource(dao);

    public ResourceExtension RULE = ResourceExtension.builder()
            .addProvider(new MockBinder())
            .addResource(resource)
            .build();

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    public class MockBinder extends AbstractBinder {

        @Override
        protected void configure() {

            Student requestStudent = new Student("eric", "c123456");

            when(request.getAttribute(any())).thenReturn(requestStudent);

            bind(request).to(HttpServletRequest.class);
        }
    }

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
    public void testAdd(){

        Student saved = RULE.target("/student/" + expected.getId()).request().get(Student.class);

        assertNotNull(saved);
        assertEquals(expected.getName(), saved.getName());
        assertEquals(expected.getId(), saved.getId());

    }
}
