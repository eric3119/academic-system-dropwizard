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

@Path("enrollstudent")
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SubjectEnrollmentResource {
    private GenericDAO dao;

    @POST
    @Path("/{semester}")
    @UnitOfWork
    public Response enrollStudent(
            @PathParam("semester") String semester,
            @FormParam("id_student") Long studentId,
            @FormParam("id_subject") Long subjectId
        ){
        Student student;
        if(studentId == null)
            throw new WebApplicationException("Form student empty", Response.Status.FORBIDDEN);
        if (subjectId == null)
            throw new WebApplicationException("Form subject empty", Response.Status.FORBIDDEN);

        student = dao.get(Student.class, studentId);

        if (student == null)
            throw new WebApplicationException("Student not found", Response.Status.NOT_FOUND);

        ///// Enroll student
        Subject subjectToEnroll = dao.get(Subject.class, subjectId);

        if (subjectToEnroll == null)
            throw new WebApplicationException("Subject not found", Response.Status.NOT_FOUND);

        // find academic offer

        List<AcademicOffer> academicOfferList = dao.findAll(AcademicOffer.class);
        List<Subject> subjectList = new ArrayList<>();

        academicOfferList = academicOfferList.stream()
                .filter(
                    academicOffer -> {
                        if (academicOffer.getSemester().equals(semester)){
                            subjectList.add(academicOffer.getSubject());
                            return true;
                        }else
                            return false;
                }
        ).collect(Collectors.toList());

        //////      TEST REQUIREMENTS      /////////////

        if (!subjectList.contains(subjectToEnroll))
            throw new WebApplicationException("Subject not offer", Response.Status.FORBIDDEN);

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
                    if (studentEnrollment.getAcademicOffer().getSubject().equals(subjectToEnroll))
                        throw new WebApplicationException("Already enrolled", Response.Status.FORBIDDEN);

                    studentSubjects.add(studentEnrollment.getAcademicOffer().getSubject());
                }
        );

        for (Subject s:
                subjectToEnroll.getRequirements()) {
            if(!studentSubjects.contains(s)){
                throw new WebApplicationException("Subject requirements not satisfied", Response.Status.FORBIDDEN);
            }
        }

        AcademicOffer academicOffer = null;

        for (AcademicOffer ao :
                academicOfferList) {
            if (ao.getSubject().equals(subjectToEnroll)) {
                academicOffer = ao;
                break;
            }
        }

        return Response
                .ok(dao.persist(SubjectEnrollment.class, new SubjectEnrollment(student, academicOffer)))
                .build();
    }

}
