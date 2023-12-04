package ru.ylab.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ylab.task.dto.RegistrationAndAuthorizationDto;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.exception.NotFoundException;
import ru.ylab.task.model.Player;
import ru.ylab.task.service.PlayerService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        session = new MockHttpSession();
    }

    @Test
    public void testAuthenticateWhenPlayerIsAuthenticatedThenReturnOkWithSuccessTrueAndPlayerId() throws Exception {
        RegistrationAndAuthorizationDto registrationRequest = new RegistrationAndAuthorizationDto("username", "password");
        Player player = new Player("username", "password".hashCode());
        player.setId(1L);

        Mockito.when(playerService.authorizationPlayer(registrationRequest.getUsername(), registrationRequest.getPassword())).thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.playerId").value(player.getId()));
    }

    @Test
    public void testAuthenticateWhenPlayerNotFoundThenReturnNotFound() throws Exception {
        RegistrationAndAuthorizationDto registrationRequest = new RegistrationAndAuthorizationDto("username", "password");

        Mockito.when(playerService.authorizationPlayer(registrationRequest.getUsername(), registrationRequest.getPassword())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAuthenticateWhenLoginExistsThenReturnConflict() throws Exception {
        RegistrationAndAuthorizationDto registrationRequest = new RegistrationAndAuthorizationDto("username", "password");

        Mockito.when(playerService.authorizationPlayer(registrationRequest.getUsername(), registrationRequest.getPassword())).thenThrow(LoginExistsException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isConflict());
    }
}