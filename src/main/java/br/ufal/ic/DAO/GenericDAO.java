package br.ufal.ic.DAO;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class GenericDAO<T> extends AbstractDAO<T> {

    public GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public T get(Serializable id) throws HibernateException {
        return super.get(id);
    }

    public T create(T t) throws HibernateException{
        return super.persist(t);
    }

    public List<T> findAll(String queryName) throws HibernateException {
        return super.list(namedQuery(queryName));
    }
}
