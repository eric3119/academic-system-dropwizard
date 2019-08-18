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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentDAOTest {
    private GenericDAO<Student> dao;

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class)
            .addEntityClass(Department.class)
            .addEntityClass(Secretary.class)
            .build();

    @BeforeEach
    public void setup(){
        dao = new GenericDAO<>(daoTestRule.getSessionFactory());
    }

    @Test
    public void testAdd(){
        final Student student = daoTestRule.inTransaction(()-> dao.create(new Student("eric", "c123456")));

        assertTrue(student.getId() > 0);
    }

    @Test
    public void testGet(){
        final Student saved = daoTestRule.inTransaction(()-> dao.create(new Student("eric", "c123456")));
        assertNotNull(saved);

        final Student student = daoTestRule.inTransaction(()-> dao.get(saved.getId()));
        assertNotNull(student);


    }

}
