package br.ufal.ic;

import br.ufal.ic.health.TemplateHealthCheck;
import br.ufal.ic.resources.AcademicSystemResources;
import io.dropwizard.Application;
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
    }

    @Override
    public void run(AcademicSystemConfiguration configuration,
                    Environment environment){// throws Exception {
        final AcademicSystemResources resource = new AcademicSystemResources(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck templateHealthCheck = new TemplateHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("template", templateHealthCheck);
        environment.jersey().register(resource);
    }
}
