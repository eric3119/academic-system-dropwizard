package br.ufal.ic.resources;

import br.ufal.ic.DAO.StudentDAO;
import br.ufal.ic.api.Saying;
import br.ufal.ic.model.Student;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@AllArgsConstructor
@Path("/academic")
@Produces(MediaType.APPLICATION_JSON)
public class AcademicSystemResources {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    private StudentDAO studentDAO;

    public AcademicSystemResources(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name){
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Student findStudent(@PathParam("id") LongParam id) {
        return studentDAO.findById(id.get());
    }
}
