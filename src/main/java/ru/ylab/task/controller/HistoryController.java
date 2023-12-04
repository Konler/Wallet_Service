package ru.ylab.task.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.task.dto.HistoryItemDto;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.service.PlayerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final PlayerService playerService;

    @Operation(summary = "Retrieve the history of a player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoryItemDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<HistoryItemDto>> getHistory(HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        if (id != null) {
            List<HistoryItemDto> historyItemDtoList = playerService.getAllHistory(id);
            return ResponseEntity.ok(historyItemDtoList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
