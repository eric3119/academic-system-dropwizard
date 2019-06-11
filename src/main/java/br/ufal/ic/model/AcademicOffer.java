package br.ufal.ic.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class AcademicOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String semester;
    @OneToOne
    private Professor professor;
    @OneToOne
    private Subject subject;
}
