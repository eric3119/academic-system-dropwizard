package br.ufal.ic.resources;

import br.ufal.ic.DAO.DepartmentDAO;
import br.ufal.ic.model.Department;
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

    private DepartmentDAO departmentDAO;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(departmentDAO.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Department findDepartment(@PathParam("id") LongParam id) {
        return departmentDAO.findById(id.get());
    }

    @POST
    @Path("/create")
    @UnitOfWork
    public Response create(@FormParam("name") String name){//, @FormParam("secretary") Secretary secretary) {
        //TODO create secretary department
        Department d = new Department(name);
        departmentDAO.create(d);
        return Response.ok(d).build();
    }
}
