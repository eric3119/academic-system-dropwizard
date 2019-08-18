package dao;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.SecretaryType;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SecretaryDAOTest {
    private GenericDAO<Secretary> dao;

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(Secretary.class)
            .build();

    @BeforeEach
    public void setup(){
        dao = new GenericDAO<>(daoTestRule.getSessionFactory());
    }

    @Test
    public void testAdd(){

        Secretary s1 = new Secretary(SecretaryType.Graduation);
        final Secretary secretary = daoTestRule.inTransaction(()-> dao.create(s1));

        assertNotNull(secretary);
        assertTrue(secretary.getId() > 0);
        assertNotNull(secretary.getId());
        assertEquals(s1.getSecretaryType(), secretary.getSecretaryType());
    }
}
