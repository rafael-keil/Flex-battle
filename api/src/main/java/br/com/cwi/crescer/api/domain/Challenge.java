package br.com.cwi.crescer.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "challenge")
public class Challenge {

    @Id
    private UUID id = UUID.randomUUID();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Answer> answerList = new ArrayList<>();

    @Column(name = "objects")
    private Integer objects;

    @Column(name = "hint")
    private String hint;

    public Challenge(List<Answer> answerList, Integer objects, String hint) {
        this.answerList = answerList;
        this.objects = objects;
        this.hint = hint;
    }
}