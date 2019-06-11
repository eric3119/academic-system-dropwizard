package br.ufal.ic.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String code;
    private Integer credits;
    private Integer min_credits; //default 0
    private ArrayList<Subject> requirements;
    private Department department;
    private Secretary secretary;
    private boolean graduation;
    private Professor professor;
}
