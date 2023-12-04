package ru.ylab.task.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players", schema = "my_schema")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private double balance;
    int password;

    public Player(String login, int password) {
        this.login = login;
        this.password = password;
        this.balance = 0;
    }


}
