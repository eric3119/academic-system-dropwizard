package br.ufal.ic.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.Department.findAll",
                query = "SELECT s FROM Department s"
        )
})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @OneToOne
    private Secretary secretary = null;

    public Department(){}
    public Department(String name, Secretary secretary) {
        this.name = name;
        this.secretary = secretary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }

        final Department that = (Department) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.secretary, that.secretary);
    }
}
