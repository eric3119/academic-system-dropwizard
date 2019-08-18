package br.ufal.ic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Entity
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.AcademicOffer.findAll",
                query = "SELECT ao FROM AcademicOffer ao"
        )
})
public class AcademicOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String semester;
    @OneToOne
    private Professor professor;
    @OneToOne
    private Subject subject;

    public Long getId() {
        return id;
    }
}
