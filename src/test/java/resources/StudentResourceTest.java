package resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Student;
import br.ufal.ic.resources.StudentResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentResourceTest{

    private StudentResource resource;
    private GenericDAO dao;

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

    @BeforeEach
    public void setup(){
        dao = mock(GenericDAO.class);
        resource = new StudentResource(dao);
    }

    @Test
    public void testAdd(){
        Student expected = new Student("eric", "c123456");
        Student saved = RULE.target("/exemplos/" + expected.getId()).request().get(Student.class);

        assertNotNull(saved);
        assertEquals(expected.getName(), saved.getName());
        assertEquals(expected.getId(), saved.getId());

    }
}

//package resources;
//
//import br.ufal.ic.DAO.GenericDAO;
//import br.ufal.ic.model.Student;
//import br.ufal.ic.resources.StudentResource;
//import io.dropwizard.testing.junit.ResourceTestRule;
//import io.dropwizard.testing.junit5.ResourceExtension;
//import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class StudentResourceTest {
//
//    @Mock
//    private static final GenericDAO<Student> dao = null;
//
//    public static final ResourceExtension RULE = ResourceExtension.builder()
//            .addResource(new StudentResource(dao))
//            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
//            .build();
//
//    private final Student person = new Student("blah", "blah@example.com");
//
//    @BeforeEach
//    public void setup() {
//        when(dao.findById(1L)).thenReturn(person);
//    }
//
//    @AfterEach
//    public void tearDown(){
//        // we have to reset the mock after each test because of the
//        // @ClassRule, or use a @Rule as mentioned below.
//        //reset(dao);
//    }
//
//
//    public void testGetPerson() {
//        //assertThat(resources.client().target("/person/blah").request().get(Person.class))
//          //      .isEqualTo(person);
//        //verify(dao).fetchPerson("blah");
//    }
//}
