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
        name = "br.ufal.ic.model.Student.findAll",
        query = "SELECT s FROM Student s"
    )
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String name;
    @NonNull private String code;

    @NonNull
    @ManyToOne
    private Department department;
    @NonNull
    @OneToOne
    private Secretary secretary;
    @NonNull
    @Column
    private Integer credits;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }

        final Student that = (Student) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.department, that.department) &&
                Objects.equals(this.secretary, that.secretary) &&
                Objects.equals(this.credits, that.credits);
    }
}
