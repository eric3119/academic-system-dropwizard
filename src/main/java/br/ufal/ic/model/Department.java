package br.ufal.ic.model;

import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
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
}
