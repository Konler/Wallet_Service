package ru.ylab.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.task.dto.RegistrationAndAuthorizationDto;
import ru.ylab.task.dto.RegistrationAndAuthorizationResponse;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/register", produces = "application/json")
public class RegistrationController {

    private final PlayerService playerService;

    @Operation(summary = "Register a new player with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(
                    mediaType = "application/json", schema =
            @Schema(implementation =  RegistrationAndAuthorizationResponse.class))),
            @ApiResponse(responseCode = "409", description = "Login already exists")
    })

    @PostMapping
    public ResponseEntity<RegistrationAndAuthorizationResponse> register( HttpSession session, @RequestBody RegistrationAndAuthorizationDto registrationRequest) {
        try {
            Long id = playerService.registerPlayer(registrationRequest.getUsername(), registrationRequest.getPassword());
            session.setAttribute("id", id);
            return ResponseEntity.ok(new RegistrationAndAuthorizationResponse(true, id));
        } catch (LoginExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}

