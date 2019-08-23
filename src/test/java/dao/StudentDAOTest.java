package dao;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.SecretaryType;
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
    private GenericDAO dao;
    private Department department;
    private Secretary secretary;

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class)
            .addEntityClass(Department.class)
            .addEntityClass(Secretary.class)
            .build();

    @BeforeEach
    public void setup(){
        dao = new GenericDAO(daoTestRule.getSessionFactory());
        secretary = daoTestRule.inTransaction(()-> dao.persist(Secretary.class, new Secretary(SecretaryType.PostGraduation)));
        department = daoTestRule.inTransaction(()-> dao.persist(Department.class, new Department("deptTest",secretary)));
    }

    @Test
    public void testAdd(){

        final Student student = daoTestRule.inTransaction(()-> dao.persist(Student.class, new Student("eric", "c123456", department, secretary, 0)));

        assertTrue(student.getId() > 0);
    }

    @Test
    public void testGet(){

        final Student saved = daoTestRule.inTransaction(()-> dao.persist(Student.class, new Student("eric", "c123456", department, secretary, 0)));
        assertNotNull(saved);

        final Student student = daoTestRule.inTransaction(()-> dao.get(Student.class, saved.getId()));
        assertNotNull(student);
        assertEquals(saved.getId(), student.getId());
    }

    @Test
    public void testGetAll(){
        final Student saved1 = daoTestRule.inTransaction(()-> dao.persist(Student.class, new Student("eric1", "c123456", department, secretary, 0)));
        final Student saved2 = daoTestRule.inTransaction(()-> dao.persist(Student.class, new Student("eric2", "c789123", department, secretary, 0)));
        final Student saved3 = daoTestRule.inTransaction(()-> dao.persist(Student.class, new Student("eric3", "c123789", department, secretary, 0)));
        assertNotNull(saved1);
        assertNotNull(saved2);
        assertNotNull(saved3);

        final List<Student> studentList = daoTestRule.inTransaction(
                ()-> dao.findAll(Student.class)
        );

        assertEquals(3, studentList.size());

    }

}
