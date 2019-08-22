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
                name = "br.ufal.ic.model.Secretary.findAll",
                query = "SELECT s FROM Secretary s"
        )
})
public class Secretary {

    public Secretary(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private SecretaryType secretaryType;
}
