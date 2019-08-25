package br.ufal.ic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.Secretary.findAll",
                query = "SELECT s FROM Secretary s"
        )
})
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private SecretaryType secretaryType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Secretary)) {
            return false;
        }

        final Secretary that = (Secretary) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.secretaryType, that.secretaryType);
    }
}
