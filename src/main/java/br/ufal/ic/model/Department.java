package br.ufal.ic.model;

import javax.persistence.*;

@Entity
@Table
class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @OneToOne
    private Secretary secretary;
}
