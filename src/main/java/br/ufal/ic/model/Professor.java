package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
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
