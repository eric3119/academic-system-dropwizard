package br.ufal.ic;

import br.ufal.ic.DAO.StudentDAO;
import br.ufal.ic.health.TemplateHealthCheck;
import br.ufal.ic.model.Student;
import br.ufal.ic.resources.AcademicSystemResources;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

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
        final AcademicSystemResources resource = new AcademicSystemResources(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck templateHealthCheck = new TemplateHealthCheck(configuration.getTemplate());

//        final Database db = new Database(hibernateBundle.getSessionFactory());

        environment.jersey().register(resource);

        environment.healthChecks().register("template", templateHealthCheck);
        environment.jersey().register(resource);
    }

    private final HibernateBundle<AcademicSystemConfiguration> hibernateBundle = new HibernateBundle<AcademicSystemConfiguration>(Student.class) {
        public DataSourceFactory getDataSourceFactory(AcademicSystemConfiguration academicSystemConfiguration) {
            return academicSystemConfiguration.getDataSourceFactory();
        }
    };
}
