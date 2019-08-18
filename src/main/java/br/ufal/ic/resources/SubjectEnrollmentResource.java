package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.SubjectEnrollment;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("enrollsubject")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectEnrollmentResource {
    private GenericDAO subjectEnrollmentGenericDAO;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(subjectEnrollmentGenericDAO.findAll("br.ufal.ic.model.SubjectEnrollment.findAll")).build();
    }
}
