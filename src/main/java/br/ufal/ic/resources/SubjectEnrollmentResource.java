package br.ufal.ic.resources;

import br.ufal.ic.DAO.GenericDAO;
import br.ufal.ic.model.*;
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
                Subject subjectToEnroll = dao.get(Subject.class, subjectId);

                if (subjectToEnroll == null){
                    throw new WebApplicationException("Subject not found", Response.Status.NOT_FOUND);
                }

                //////      TEST REQUIREMENTS      /////////////

                if (subjectToEnroll.getMin_credits() > student.getCredits())
                    throw new WebApplicationException("No enough credits", Response.Status.FORBIDDEN);

                SecretaryType subjectSecretaryType = subjectToEnroll.getSecretary().getSecretaryType()
                        , studentSecretaryType = student.getSecretary().getSecretaryType();

                if(subjectSecretaryType != studentSecretaryType){
                    if(subjectSecretaryType == SecretaryType.Graduation &&
                        studentSecretaryType == SecretaryType.PostGraduation)
                        throw new WebApplicationException("Postgraduate student cannot enroll graduation subject", Response.Status.FORBIDDEN);
                    if(subjectSecretaryType == SecretaryType.PostGraduation &&
                            studentSecretaryType == SecretaryType.Graduation &&
                            student.getCredits() < 170)
                        throw new WebApplicationException("Required minimum 170 credits", Response.Status.FORBIDDEN);
                }

                // find student enrolments
                List<Subject> studentSubjects = new ArrayList<>();
                
                List<SubjectEnrollment> studentEnrollments = dao.findAll(SubjectEnrollment.class)
                        .stream()
                        .filter(subject1 -> subject1.getStudent().equals(student))
                        .collect(Collectors.toList());

                studentEnrollments.forEach(
                        studentEnrollment -> {
                            if (studentEnrollment.getSubject().equals(subjectToEnroll))
                                throw new WebApplicationException("Already enrolled", Response.Status.FORBIDDEN);

                            studentSubjects.add(studentEnrollment.getSubject());
                        }
                );

                for (Subject s:
                        subjectToEnroll.getRequirements()) {
                    if(!studentSubjects.contains(s)){
                        throw new WebApplicationException("Subject requirements not satisfied", Response.Status.FORBIDDEN);
                    }
                }

                SubjectEnrollment saved = dao
                        .persist(SubjectEnrollment.class, new SubjectEnrollment(subjectToEnroll, student));

                assert(saved != null);
                System.out.println("saved >>>>> "+saved);
                return Response.ok(saved).build();
            }
        }
    }

}
