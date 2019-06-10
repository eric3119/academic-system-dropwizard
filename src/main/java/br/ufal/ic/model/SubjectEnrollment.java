package br.ufal.ic.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class SubjectEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ArrayList<AcademicOffer> academicOffers;
}
