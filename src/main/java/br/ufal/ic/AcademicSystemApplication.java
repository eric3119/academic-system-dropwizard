package br.ufal.ic;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
// import com.example.helloworld.resources.HelloWorldResource;
// import com.example.helloworld.health.TemplateHealthCheck;

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

    public void run(AcademicSystemConfiguration configuration,
                    Environment environment){// throws Exception {
        //
    }
}
