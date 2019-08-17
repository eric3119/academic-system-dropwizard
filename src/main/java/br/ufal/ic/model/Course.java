package br.ufal.ic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Entity
@NamedQueries({
        @NamedQuery(
                name = "findAll",
                query = "SELECT c FROM Course c"
        )
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @OneToOne
    private Secretary secretary;
    
    public long getId(){
        return id;
    }
}
