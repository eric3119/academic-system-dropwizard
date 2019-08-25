package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
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

    private GenericDAO dao;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(dao.findAll(Student.class)).build();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Student findStudent(@PathParam("id") LongParam id) {

        Student student =  dao.get(Student.class, id.get());

        if (student == null){
            throw new WebApplicationException("Student not found", Response.Status.NOT_FOUND);
        }else
            return student;
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(
            @FormParam("name") String name,
            @FormParam("code") String code,
            @FormParam("id_department") Long idDepartment,
            @FormParam("id_secretary") Long idSecretary,
            @FormParam("credits") Integer credits
            ) {

        if (idDepartment == null){
            throw new WebApplicationException("Empty department", Response.Status.BAD_REQUEST);
        }
        if (idSecretary == null){
            throw new WebApplicationException("Empty secretary", Response.Status.BAD_REQUEST);
        }

        if (name == null){
            throw new WebApplicationException("Empty name", Response.Status.BAD_REQUEST);
        }
        if (code == null){
            throw new WebApplicationException("Empty code", Response.Status.BAD_REQUEST);
        }
        if (credits == null) credits = 0;

        Department d = dao.get(Department.class, idDepartment);
        if (d == null){
            throw new WebApplicationException("Department not found", Response.Status.NOT_FOUND);
        }
        Secretary s = dao.get(Secretary.class, idSecretary);
        if (s == null){
            throw new WebApplicationException("Secretary not found", Response.Status.NOT_FOUND);
        }

        Student student = new Student(name, code, d, s, credits);
        return Response.ok(dao.persist(Student.class, student)).build();
    }
}
