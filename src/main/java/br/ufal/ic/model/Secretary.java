package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(
                name = "findAll",
                query = "SELECT s FROM Secretary s"
        )
})
public class Secretary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private SecretaryType secretaryType;
}
