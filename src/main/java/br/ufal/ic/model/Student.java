package br.ufal.ic.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull private String name;
    @NonNull private String code;

    @ManyToOne
    private Department department;
    @OneToOne
    private Secretary secretary;
    private Integer credits;
    @OneToMany
    private List<SubjectEnrollment> subjectEnrollments;
    @OneToMany
    private CourseEnrollment courseEnrollment;

    public Long getId() {
        return id;
    }
}
