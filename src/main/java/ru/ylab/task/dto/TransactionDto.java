package ru.ylab.task.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String type;
    private double amount;
    private String state;
    private Long playerId;


}
