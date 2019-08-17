package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@NamedQueries(
        @NamedQuery(
                name = "findAll",
                query = "SELECT ce from CourseEnrollment ce"
        )
)
public class CourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @OneToOne
    private Course course;
    @NonNull
    @ManyToOne
    private Student student;
    @NonNull
    private boolean active;
}
