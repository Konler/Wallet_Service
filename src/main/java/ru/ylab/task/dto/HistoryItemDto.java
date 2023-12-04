package ru.ylab.task.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryItemDto {

    private Long id;
    private Long playerId;

    private String action;

    private String time;


}
