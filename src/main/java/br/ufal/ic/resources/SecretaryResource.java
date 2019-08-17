package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Secretary;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("secretary")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SecretaryResource {
    private GenericDAO<Secretary> secretaryGenericDAO;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(secretaryGenericDAO.findAll()).build();
    }
}
