package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.CourseEnrollment;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("enrollcourse")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class CourseEnrollmentResource {
    private GenericDAO<CourseEnrollment> courseEnrollmentGenericDAO;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(courseEnrollmentGenericDAO.findAll()).build();
    }
}
