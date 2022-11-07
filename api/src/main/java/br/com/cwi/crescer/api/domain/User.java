package br.com.cwi.crescer.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_db")
public class User {

    @Id
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "ranking")
    private Integer ranking;

    public User(String username, String email, String name, Integer ranking) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.ranking = ranking;
    }
}
