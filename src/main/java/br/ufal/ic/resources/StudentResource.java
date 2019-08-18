package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Student;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/student")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    private GenericDAO studentDAO;

    @GET
    @UnitOfWork
    public Response findAll(){

        List<Object> objectList = studentDAO.findAll("br.ufal.ic.model.Student.findAll");

        if (objectList == null){
            throw new WebApplicationException("No records found", Response.Status.NOT_FOUND);
        }else

        return Response.ok(objectList).build();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Student findStudent(@PathParam("id") LongParam id) {

        Student student =  studentDAO.get(Student.class, id.get());

        if (student == null){
            throw new WebApplicationException("Student not found", Response.Status.NOT_FOUND);
        }else
            return student;
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name, @FormParam("code") String code) {
        Student d = new Student(name, code);
        studentDAO.persist(Student.class, d);
        return Response.ok(d).build();
    }
}
