package ru.ylab.task.model.transaction;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * The type Transaction.
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(schema = "my_schema", name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private double amount;

    @Enumerated(EnumType.STRING)
    private State state;

    private Long playerId;

    public Transaction(Long id, Long playerId, TransactionType type, double amount) {
        if (id != null) {
            this.id = id;
        }
        this.type = type;
        this.amount = amount;
        this.playerId = playerId;
        this.state = State.UNACTIVATED;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(id, that.id) && type == that.type && state == that.state && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, state, playerId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", state=" + state +
                ", playerId=" + playerId +
                '}';
    }
}
