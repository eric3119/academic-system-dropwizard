package br.ufal.ic.model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class EnrollmentProof {
    private Student student;
    private List<Subject> subjects;

    public String studentName(){
        return student.getName();
    }

    public String studentCode(){
        return student.getCode();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
