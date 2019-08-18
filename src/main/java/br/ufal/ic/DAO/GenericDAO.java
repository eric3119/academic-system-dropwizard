package br.ufal.ic.DAO;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class GenericDAO extends AbstractDAO<Object> {

    public GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public <T> T get(Class<T> clazz, Serializable id) throws HibernateException {
        return currentSession().get(clazz, id);
    }

    public <T> T persist(Class<T> clazz, T entity) throws HibernateException {
        return (T) super.persist(entity);
    }

    public List<Object> findAll(String queryName) throws HibernateException {
        return super.list(namedQuery(queryName));
    }
}
