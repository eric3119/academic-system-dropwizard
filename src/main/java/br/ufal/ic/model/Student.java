package br.ufal.ic.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Entity
@NamedQueries({
    @NamedQuery(
        name = "br.ufal.ic.model.Student.findAll",
        query = "SELECT s FROM Student s"
    )
})
public class Student {

    public Student(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String name;
    @NonNull private String code;

    @ManyToOne
    private Department department;
    @OneToOne
    private Secretary secretary;
    @Column
    private Integer credits;

    public Long getId() {
        return id;
    }
}
