package ru.ylab.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history", schema = "my_schema")
public class HistoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id")
    private Long playerId;

    private String action;

    private LocalDateTime time;

    public HistoryItem(Player player, String action, LocalDateTime time) {
        this.playerId = player.getId();
        this.action = action;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Action = '" + action + '\'' +
                ", time = " + time.format(DateTimeFormatter.ofPattern("d MMM uuuu hh:mm"));
    }
}

