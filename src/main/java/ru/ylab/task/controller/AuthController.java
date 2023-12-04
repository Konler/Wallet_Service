package ru.ylab.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.task.dto.RegistrationAndAuthorizationDto;
import ru.ylab.task.dto.RegistrationAndAuthorizationResponse;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.exception.NotFoundException;
import ru.ylab.task.model.Player;
import ru.ylab.task.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final PlayerService playerService;

    @Operation(summary = "Authenticate a player with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json", schema =
            @Schema(implementation = RegistrationAndAuthorizationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "409", description = "Login already exists")
    })
    @PostMapping
    public ResponseEntity<RegistrationAndAuthorizationResponse> authenticate(HttpSession session,
            @Param(value = "Registration and authorization request data")
            @RequestBody RegistrationAndAuthorizationDto registrationRequest) {
        try {
            Player player = playerService.authorizationPlayer(registrationRequest.getUsername(), registrationRequest.getPassword());
            session.setAttribute("id", player.getId());
            return ResponseEntity.ok(new RegistrationAndAuthorizationResponse(true, player.getId()));
        } catch (LoginExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
