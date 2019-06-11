package br.ufal.ic.model;

import javax.persistence.*;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String code;
    @OneToOne
    private Department department;
}
