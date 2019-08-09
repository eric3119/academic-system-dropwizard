package br.ufal.ic.DAO;

import br.ufal.ic.model.Department;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class DepartmentDAO extends AbstractDAO<Department> {

    public DepartmentDAO(SessionFactory factory) {
        super(factory);
    }

    public Department findById(Long id) {
        return get(id);
    }

    public long create(Department department) {
        return persist(department).getId();
    }

    public List<Department> findAll() {
        return list(namedQuery("br.ufal.ic.model.Department.findAll"));
    }
}
