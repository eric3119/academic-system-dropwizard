package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.ManyToOne;

@RequiredArgsConstructor
public class Student {

    @NonNull private String name;
    @NonNull private String code;

    @ManyToOne
    private Department department;

    private Secretary secretary;
    private boolean graduating;
    private Integer credits;
    private Enrolment enrolment;
}
