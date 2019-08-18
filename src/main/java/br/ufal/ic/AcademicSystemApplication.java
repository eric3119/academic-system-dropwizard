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

        final GenericDAO dao = new GenericDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new AcademicOfferResource(dao));
        environment.jersey().register(new CourseResource(dao));
        environment.jersey().register(new CourseEnrollmentResource(dao));
        environment.jersey().register(new DepartmentResource(dao));
        environment.jersey().register(new ProfessorResource(dao));
        environment.jersey().register(new SecretaryResource(dao));
        environment.jersey().register(new StudentResource(dao));
        environment.jersey().register(new SubjectResource(dao));
        environment.jersey().register(new SubjectEnrollmentResource(dao));
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
