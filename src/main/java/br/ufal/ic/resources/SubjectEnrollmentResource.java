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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

                //////      TEST REQUIREMENTS      /////////////

                if (subject.getMin_credits() > student.getCredits())
                    throw new WebApplicationException("No enough credits", Response.Status.FORBIDDEN);

                // find student enrolments
                List<Subject> studentSubjects = new ArrayList<>();
                
                List<SubjectEnrollment> studentEnrollments = dao.findAll(SubjectEnrollment.class)
                        .stream()
                        .filter(subject1 -> subject1.getStudent().equals(student))
                        .collect(Collectors.toList());

                studentEnrollments.forEach(subjectEnrollment -> studentSubjects.add(subjectEnrollment.getSubject()));
                System.out.println(studentSubjects);

                for (Subject s:
                        subject.getRequirements()) {
                    if(!studentSubjects.contains(s)){
                        throw new WebApplicationException("Subject requirements not satisfied", Response.Status.FORBIDDEN);
                    }
                }

                SubjectEnrollment saved = dao
                        .persist(SubjectEnrollment.class, new SubjectEnrollment(subject, student));

                assert(saved != null);
                System.out.println("saved >>>>> "+saved);
                return Response.ok(saved).build();
            }
        }
    }

}
