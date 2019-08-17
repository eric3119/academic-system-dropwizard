package br.ufal.ic.DAO;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDAO<T> extends AbstractDAO<T> {

    public GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public T findById(Long id) {
        return get(id);
    }

    public long create(T t){
        Integer id = null;

        Session session=currentSession().getSessionFactory().openSession();
        Transaction trans=session.beginTransaction();
        try{
            session.save(t);
            id= (Integer) session.createQuery("Select LAST_INSERT_ID()").uniqueResult();
            trans.commit();
        }

        catch(HibernateException he){
            System.err.println("Error writing session: " + he);
        }

        if (id != null) {
            return id.longValue();
        }else{
            return -1;
        }
    }

    public List<T> findAll(){
        return list(namedQuery("findAll"));
    }
}
