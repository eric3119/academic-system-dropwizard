package br.ufal.ic.DAO;

import br.ufal.ic.model.Subject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class SubjectDAO extends AbstractDAO<Subject> {

    public SubjectDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Long create(Subject subject){
        return persist(subject).getId();
    }

    public Subject findById(Long aLong) {
        return get(aLong);
    }
}
