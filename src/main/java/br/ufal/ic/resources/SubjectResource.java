package br.ufal.ic.resources;

import br.ufal.ic.DAO.SubjectDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Subject;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/subject")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectResource {
    private SubjectDAO subjectDAO;

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Subject findById(@PathParam("id") LongParam id) {
        return subjectDAO.findById(id.get());
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name,
                           @FormParam("code") String code,
                           @FormParam("credits") Integer credits,
                           @FormParam("min_credits") Integer min_credits
//                           ,@FormParam("requirements") List<Subject> requirements,
//                           @FormParam("department") Long department_id,
//                           @FormParam("secretary") Long secretary_id
                           ) {
        Subject d = new Subject(name,code,credits,min_credits/*,requirements,DepartmentDAO.findById(department_id),secretary*/);
        subjectDAO.create(d);
        return Response.ok(d).build();
    }
}