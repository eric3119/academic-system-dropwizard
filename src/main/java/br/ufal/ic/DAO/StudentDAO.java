package br.ufal.ic.DAO;

import br.ufal.ic.model.Student;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class StudentDAO extends AbstractDAO<Student> {
    public StudentDAO(SessionFactory factory) {
        super(factory);
    }

    public Student findById(Long id) {
        return get(id);
    }

    public long create(Student student) {
        return persist(student).getId();
    }

    public List<Student> findAll() {
        return list(namedQuery("br.ufal.ic.model.Student.findAll"));
    }
}

