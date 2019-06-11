package br.ufal.ic.model;

import javax.persistence.*;

@Entity
public class SubjectEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private ArrayList<AcademicOffer> academicOffers;

    @ManyToOne
    private Subject subject;
    @ManyToOne
    private Student student;
}
