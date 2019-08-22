package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Student;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("enrollsubject")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectEnrollmentResource {
    private GenericDAO dao;

    @GET
    @UnitOfWork
    public Response enrollStudent(@QueryParam("student") Long studentId){

        if(studentId != null){
            System.out.println(studentId);
            Student student = dao.get(Student.class, studentId);

            student.getDepartment();

            return Response.ok().build();
        }else
            return Response.ok(dao.findAll("br.ufal.ic.model.SubjectEnrollment.findAll")).build();
    }

}
