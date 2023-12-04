package ru.ylab.task.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.ylab.task.dto.RegistrationAndAuthorizationDto;
import ru.ylab.task.dto.RegistrationAndAuthorizationResponse;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.service.PlayerService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PlayerService playerService;

    @Test
    public void testRegisterWhenPlayerRegistrationIsSuccessfulThenReturn200AndSuccessResponse() throws Exception {
        // Arrange
        Long playerId = 1L;
        when(playerService.registerPlayer(anyString(), anyString())).thenReturn(playerId);
        RegistrationAndAuthorizationDto registrationRequest = new RegistrationAndAuthorizationDto("username", "password");

        // Act & Assert
        mockMvc.perform(post("/register")
                        .sessionAttr("id", playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new RegistrationAndAuthorizationResponse(true, playerId))));
    }

    @Test
    public void testRegisterWhenLoginAlreadyExistsThenReturn409() throws Exception {
        // Arrange
        doThrow(LoginExistsException.class).when(playerService).registerPlayer(anyString(), anyString());
        RegistrationAndAuthorizationDto registrationRequest = new RegistrationAndAuthorizationDto("username", "password");

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict());
    }
}
