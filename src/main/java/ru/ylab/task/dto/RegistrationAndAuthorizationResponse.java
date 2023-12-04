package ru.ylab.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAndAuthorizationResponse {

    private boolean isSuccessful;
    private Long playerId;


}
