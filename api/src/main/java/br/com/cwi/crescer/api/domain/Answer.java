package br.com.cwi.crescer.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "answer")
public class Answer {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private Challenge challenge;

    @Column(name = "property")
    private String property;

    @Column(name = "value")
    private String value;

    public Answer(String property, String value) {
        this.property = property;
        this.value = value;
    }
}
