package br.ufal.ic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "br.ufal.ic.model.Subject.findAll",
                query = "SELECT s FROM Subject s"
        )
})
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull private String name;
    @NonNull private String code;
    @NonNull private Integer credits;
    @NonNull private Integer min_credits;
    @OneToMany
    private List<Subject> requirements;
    @NonNull
    @ManyToOne
    private Department department;
    @NonNull
    @OneToOne
    private Secretary secretary;

    public Subject(@NonNull String name, @NonNull String code, @NonNull Integer credits, @NonNull Integer min_credits, List<Subject> requirements, @NonNull Department department, @NonNull Secretary secretary) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.min_credits = min_credits;
        this.requirements = requirements;
        this.department = department;
        this.secretary = secretary;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }

        final Subject that = (Subject) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.credits, that.credits) &&
                Objects.equals(this.min_credits, that.min_credits) &&
                Objects.equals(this.requirements, that.requirements) &&
                Objects.equals(this.department, that.department) &&
                Objects.equals(this.secretary, that.secretary);
    }
}
