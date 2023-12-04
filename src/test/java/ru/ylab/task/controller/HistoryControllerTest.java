package ru.ylab.task.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.ylab.task.dto.HistoryItemDto;
import ru.ylab.task.service.PlayerService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(HistoryController.class)
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    public void testGetHistoryWhenHistoryRetrievedThenReturnHistory() throws Exception {
        Long playerId = 1L;
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", playerId);

        List<HistoryItemDto> historyItemDtoList = Arrays.asList(
                new HistoryItemDto(1L, playerId, "action1", "time1"),
                new HistoryItemDto(2L, playerId, "action2", "time2")
        );

        when(playerService.getAllHistory(playerId)).thenReturn(historyItemDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/history").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].playerId").value(playerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].action").value("action1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].time").value("time1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].playerId").value(playerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].action").value("action2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].time").value("time2"));
    }

    @Test
    public void testGetHistoryWhenPlayerNotAuthorizedThenReturnUnauthorized() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/history").session(session))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}