package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.AcademicOffer;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/offer")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class AcademicOfferResource {

    private GenericDAO<AcademicOffer> academicOfferDAO;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(academicOfferDAO.findAll("br.ufal.ic.model.AcademicOffer.findAll")).build();
    }
}
