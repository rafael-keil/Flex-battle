package br.com.cwi.crescer.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "battle")
public class Battle {

    @Id
    private Integer token;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User opponent;

    @Column(name = "ownerPoints")
    private Integer ownerPoints;

    @Column(name = "opponentPoints")
    private Integer opponentPoints;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "computed")
    private Boolean computed = false;

    public Battle(Integer token, User owner) {
        this.token = token;
        this.owner = owner;
    }
}
