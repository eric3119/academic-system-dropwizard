package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Secretary;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/department")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    private GenericDAO dao;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(dao.findAll(Department.class, "br.ufal.ic.model.Department.findAll")).build();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Department findDepartment(@PathParam("id") LongParam id) {
        return dao.get(Department.class, id.get());
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(
            @FormParam("name") String name,
            @FormParam("id_secretary") Long idSecretary
        ) {

        Secretary s = dao.get(Secretary.class, idSecretary);
        if (s == null){
            throw new WebApplicationException("Secretary not found", Response.Status.NOT_FOUND);
        }
        Department d = new Department(name, s);
        dao.persist(Department.class, d);
        return Response.ok(d).build();
    }
}
