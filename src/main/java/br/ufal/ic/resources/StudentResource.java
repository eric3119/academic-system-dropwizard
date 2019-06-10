package br.ufal.ic.resources;

import br.ufal.ic.DAO.StudentDAO;
import br.ufal.ic.model.Student;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/student")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    private StudentDAO studentDAO;

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Student findStudent(@PathParam("id") LongParam id) {
        return studentDAO.findById(id.get());
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name, @FormParam("code") String code) {
        Student d = new Student(name, code);
        studentDAO.create(d);
        return Response.ok(d).build();
    }
}
