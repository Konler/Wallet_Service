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
import org.springframework.web.bind.annotation.*;
import ru.ylab.task.dto.RegistrationAndAuthorizationResponse;
import ru.ylab.task.dto.TransactionDto;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.service.WalletService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final WalletService walletService;

    @Operation(summary = "Activate a transaction for the player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction activated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity activateTransaction( HttpSession session, @RequestBody TransactionDto dto) {
        Long id = (Long) session.getAttribute("id");
        if (id != null) {
            dto.setPlayerId(id);
            walletService.activateTransaction(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get transaction history of this player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction history retrieved successfully",
                    content = @Content(
                            mediaType = "application/json", schema =
                    @Schema(implementation =  TransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        if (id != null) {
            List<TransactionDto> transactionDtoList = walletService.findTransactionHistory(id);
            return ResponseEntity.ok(transactionDtoList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
