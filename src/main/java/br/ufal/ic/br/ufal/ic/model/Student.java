package br.ufal.ic.br.ufal.ic.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Student {
    @NonNull
    private String name;
    @NonNull
    private String code;
}
