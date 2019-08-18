package br.ufal.ic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
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
    @JsonProperty
    public Long getId() {
        return id;
    }
    @JsonProperty
    public String getName() {
        return name;
    }
    @JsonProperty
    public String getCode() {
        return code;
    }
}
