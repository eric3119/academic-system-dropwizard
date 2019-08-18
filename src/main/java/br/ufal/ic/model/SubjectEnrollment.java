package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.SubjectEnrollment.findAll",
                query = "SELECT se FROM SubjectEnrollment se"
        )
})
public class SubjectEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private ArrayList<AcademicOffer> academicOffers;

    @NonNull
    @ManyToOne
    private Subject subject;
    @NonNull
    @ManyToOne
    private Student student;
}
