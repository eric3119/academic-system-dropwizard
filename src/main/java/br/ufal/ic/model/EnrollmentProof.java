package br.ufal.ic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EnrollmentProof {
    private Student student;
    private List<Subject> subjects;
}
