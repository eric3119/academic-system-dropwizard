package br.ufal.ic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
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

//    private ArrayList<AcademicOffer> academicOffers;//TODO subject academic offer

    @NonNull
    @ManyToOne
    private Subject subject;
    @NonNull
    @ManyToOne
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectEnrollment)) {
            return false;
        }

        final SubjectEnrollment that = (SubjectEnrollment) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.subject, that.subject) &&
                Objects.equals(this.student, that.student);
    }
}
