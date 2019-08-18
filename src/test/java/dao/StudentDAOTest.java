package dao;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.Student;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentDAOTest {
    private GenericDAO studentDAO;

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class)
            .addEntityClass(Department.class)
            .addEntityClass(Secretary.class)
            .build();

    @BeforeEach
    public void setup(){
        studentDAO = new GenericDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void testAdd(){
        final Student student = daoTestRule.inTransaction(()-> studentDAO.persist(Student.class, new Student("eric", "c123456")));

        assertTrue(student.getId() > 0);
    }

    @Test
    public void testGet(){

        final Student saved = daoTestRule.inTransaction(()-> studentDAO.persist(Student.class, new Student("eric", "c123456")));
        assertNotNull(saved);

        final Student student = daoTestRule.inTransaction(()-> studentDAO.get(Student.class, saved.getId()));
        assertNotNull(student);
        assertEquals(saved.getId(), student.getId());
    }

    @Test
    public void testGetAll(){
        final Student saved1 = daoTestRule.inTransaction(()-> studentDAO.persist(Student.class, new Student("eric1", "c123456")));
        final Student saved2 = daoTestRule.inTransaction(()-> studentDAO.persist(Student.class, new Student("eric2", "c789123")));
        final Student saved3 = daoTestRule.inTransaction(()-> studentDAO.persist(Student.class, new Student("eric3", "c123789")));
        assertNotNull(saved1);
        assertNotNull(saved2);
        assertNotNull(saved3);

        final List<Object> studentList = daoTestRule.inTransaction(
                ()-> studentDAO.findAll("br.ufal.ic.model.Student.findAll")
        );

        assertEquals(3, studentList.size());

    }

}
