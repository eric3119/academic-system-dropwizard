package br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Student {
    @NonNull private String name;
    @NonNull private String code;
    private Department department;
    private Secretary secretary;
    private boolean graduating;
    private Integer credits;
}
