package br.ufal.ic.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@Getter
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.Professor.findAll",
                query = "SELECT p FROM Professor p"
        )
})
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String code;
    @OneToOne
    @NonNull
    private Department department;
}
