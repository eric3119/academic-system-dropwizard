package br.ufal.ic.model;

import java.util.ArrayList;

class Subject {
    private String name;
    private String code;
    private Integer credits;
    private Integer min_credits; //default 0
    private ArrayList<Subject> requirements;
    private Department department;
    private Secretary secretary;
    private boolean graduation;
}
