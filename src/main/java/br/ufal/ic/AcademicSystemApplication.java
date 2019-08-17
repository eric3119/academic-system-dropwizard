package br.ufal.ic;

import br.ufal.ic.DAO.*;
import br.ufal.ic.model.*;
import br.ufal.ic.resources.*;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AcademicSystemApplication extends Application<AcademicSystemConfiguration> {

    public static void main(String[] args) throws Exception {
        new AcademicSystemApplication().run(args);
    }

    @Override
    public String getName(){
        return "academic-system";
    }

    @Override
    public void initialize(Bootstrap<AcademicSystemConfiguration> bootstrap) {
        //super.initialize(bootstrap);
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(AcademicSystemConfiguration configuration,
                    Environment environment) throws Exception {

        final GenericDAO<AcademicOffer> academicOfferDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Course> courseDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<CourseEnrollment> courseEnrollmentDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Department> departmentDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Professor> professorDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Secretary> secretaryDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Student> studentDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<Subject> subjectDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());
        final GenericDAO<SubjectEnrollment> subjectEnrollmentDAO = new GenericDAO<>(hibernateBundle.getSessionFactory());


        environment.jersey().register(new AcademicOfferResource(academicOfferDAO));
        environment.jersey().register(new CourseResource(courseDAO));
        environment.jersey().register(new CourseEnrollmentResource(courseEnrollmentDAO));
        environment.jersey().register(new DepartmentResource(departmentDAO));
        environment.jersey().register(new ProfessorResource(professorDAO));
        environment.jersey().register(new SecretaryResource(secretaryDAO));
        environment.jersey().register(new StudentResource(studentDAO));
        environment.jersey().register(new SubjectResource(subjectDAO));
        environment.jersey().register(new SubjectEnrollmentResource(subjectEnrollmentDAO));
    }

    private final HibernateBundle<AcademicSystemConfiguration> hibernateBundle = new HibernateBundle<AcademicSystemConfiguration>(
            AcademicOffer.class,
            Course.class,
            CourseEnrollment.class,
            Department.class,
            Professor.class,
            Secretary.class,
            Student.class,
            Subject.class,
            SubjectEnrollment.class
    ) {
        public DataSourceFactory getDataSourceFactory(AcademicSystemConfiguration academicSystemConfiguration) {
            return academicSystemConfiguration.getDataSourceFactory();
        }
    };
}
