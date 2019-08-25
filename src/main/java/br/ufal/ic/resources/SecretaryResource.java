package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.AcademicOffer;
import br.ufal.ic.model.Secretary;
import br.ufal.ic.model.Subject;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("secretary")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SecretaryResource {
    private GenericDAO dao;

    @GET
    @UnitOfWork
    public Response findAll(){
        return Response.ok(dao.findAll(Secretary.class)).build();
    }

    @GET
    @Path("/{id}/{semester}")
    @UnitOfWork
    public Response secretarySemester(@PathParam("id") LongParam id, @PathParam("semester") LongParam semester) {
        Secretary secretary = dao.get(Secretary.class, id.get());

        if(secretary == null)
            throw new WebApplicationException("Secretary not found", Response.Status.NOT_FOUND);

        AcademicOffer academicOffer = dao.get(AcademicOffer.class, semester.get());

        if (academicOffer == null)
            throw new WebApplicationException("Academic Offer not found", Response.Status.NOT_FOUND);

        List<Subject> subjectList = dao.findAll(Subject.class);

        subjectList = subjectList.stream().filter(
                subject -> subject.getSecretary().getId().equals(id.get())&&
                            subject.getSecretary().getSecretaryType().equals(secretary.getSecretaryType()))
                .collect(Collectors.toList());

        return Response.ok(subjectList).build();
    }

}
