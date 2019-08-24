package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.Department;
import br.ufal.ic.model.Student;
import br.ufal.ic.model.Subject;
import br.ufal.ic.model.SubjectEnrollment;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("enrollsubject")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectEnrollmentResource {
    private GenericDAO dao;

    @GET
    @UnitOfWork
    public Response enrollStudent(
            @QueryParam("id_student") Long studentId,
            @QueryParam("id_subject") Long subjectId
        ){

        Student student;
        if(studentId == null) {// return all students
            return Response.ok(dao.findAll(Student.class)).build();
        }else{
            student = dao.get(Student.class, studentId);
            if (student == null){
                throw new WebApplicationException("Student not found", Response.Status.NOT_FOUND);
            }

            if(subjectId == null) {// return department subjects
                Department department = student.getDepartment();

                List<Subject> subjectList = dao.findAll(Subject.class);

                return Response.ok(subjectList.stream().filter(s -> (s.getDepartment().equals(department)))).build();
            }else{// enroll student
                Subject subject = dao.get(Subject.class, subjectId);

                if (subject == null){
                    throw new WebApplicationException("Subject not found", Response.Status.NOT_FOUND);
                }

                if (subject.getMin_credits() > student.getCredits())
                    throw new WebApplicationException("No enough credits", Response.Status.FORBIDDEN);

                SubjectEnrollment subjectEnrollment = new SubjectEnrollment(subject, student);
                return Response.ok(dao.persist(SubjectEnrollment.class, subjectEnrollment)).build();
            }
        }
    }

}
