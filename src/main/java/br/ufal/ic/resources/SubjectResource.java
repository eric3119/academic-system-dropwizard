package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
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
    private GenericDAO subjectDAO;

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Response findById(@PathParam("id") LongParam id) {
        return Response.ok(subjectDAO.get(Subject.class, id.get())).build();
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name,
                           @FormParam("code") String code,
                           @FormParam("credits") Integer credits,
                           @FormParam("min_credits") Integer min_credits,
                           @FormParam("requirements") List<Long> requirements,
                           @FormParam("department") Long department_id,
                           @FormParam("secretary") Long secretary_id
                           ) {

        Secretary secretary = subjectDAO.get(Secretary.class, secretary_id);
        if (secretary == null)
            throw new WebApplicationException("Secretary not found", Response.Status.NOT_FOUND);

        Department department = subjectDAO.get(Department.class, department_id);
        if (department == null)
            throw new WebApplicationException("Department not found", Response.Status.NOT_FOUND);

        List<Subject> requirementsList = new ArrayList<>();

        for (Long l :
                requirements) {
            Subject s = subjectDAO.get(Subject.class, l);

            if(s != null)
                requirementsList.add(s);
        }

        Subject d = new Subject(
                name,
                code,
                credits,
                min_credits,
                requirementsList,
                department,
                secretary
        );
        return Response.ok(subjectDAO.persist(Subject.class, d)).build();
    }
}
