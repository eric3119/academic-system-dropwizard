package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.SecretaryType;
import br.ufal.ic.model.Subject;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subject")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectResource {
    private GenericDAO subjectDAO;

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Subject findById(@PathParam("id") LongParam id) {
        return subjectDAO.get(Subject.class, id.get());
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name,
                           @FormParam("code") String code,
                           @FormParam("credits") Integer credits,
                           @FormParam("min_credits") Integer min_credits,
                          // @FormParam("requirements") List<Subject> requirements, // TODO form param requirements
                           @FormParam("department") Long department_id,
                           @FormParam("secretary") Long secretary_id
                           ) {
        Department department  = new Department("teste", new Secretary(SecretaryType.Graduation)); // TODO remove department mock
        Subject d = new Subject(
                name,
                code,
                credits,
                min_credits,
                //requirements,
                department,
                new Secretary(SecretaryType.Graduation)//secretary_id
        );
        subjectDAO.persist(Subject.class, d);
        return Response.ok(d).build();
    }
}
