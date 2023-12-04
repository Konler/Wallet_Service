package ru.ylab.task.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.task.dto.HistoryItemDto;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.exception.NotFoundException;
import ru.ylab.task.mapper.HistoryMapper;
import ru.ylab.task.model.HistoryItem;
import ru.ylab.task.model.Player;
import ru.ylab.task.repository.HistoryRepository;
import ru.ylab.task.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private HistoryMapper historyMapper;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("testLogin", Arrays.hashCode("testPassword".toCharArray()));
    }

    @Test
    public void testAddActionToHistoryWhenPlayerDoesNotExistThenActionNotAddedToHistory() {
        playerService.addActionToHistory(player.getId(), "testAction");
        verify(historyRepository, times(0)).save(any(HistoryItem.class));
    }

    @Test
    public void testAddActionToHistoryWhenPlayerIdIsNullThenActionNotAddedToHistory() {
        playerService.addActionToHistory(null, "testAction");

        verify(historyRepository, times(0)).save(any(HistoryItem.class));
    }

    @Test
    public void testRegisterPlayerWhenLoginTakenThenThrowException() {
        when(playerRepository.existsByLogin(player.getLogin())).thenReturn(true);

        assertThrows(LoginExistsException.class, () -> playerService.registerPlayer(player.getLogin(), "testPassword"));
    }

    @Test
    public void testAuthorizationPlayerWhenPlayerFoundThenReturnPlayer() throws NotFoundException {
        when(playerRepository.findByLoginAndPassword(player.getLogin(), player.getPassword())).thenReturn(Optional.of(player));

        Player authorizedPlayer = playerService.authorizationPlayer(player.getLogin(), "testPassword");

        assertEquals(player, authorizedPlayer);
    }

    @Test
    public void testAuthorizationPlayerWhenPlayerNotFoundThenThrowNotFoundException() {
        when(playerRepository.findByLoginAndPassword(player.getLogin(), player.getPassword())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> playerService.authorizationPlayer(player.getLogin(), "testPassword"));
    }

    @Test
    public void testGetAllHistoryWhenPlayerExistsThenReturnHistoryItems() {
        HistoryItem historyItem = new HistoryItem(player, "testAction", LocalDateTime.now());
        HistoryItemDto historyItemDto = new HistoryItemDto(historyItem.getId(), player.getId(), historyItem.getAction(), historyItem.getTime().toString());

        when(historyRepository.getAllByPlayerId(player.getId())).thenReturn(List.of(historyItem));
        when(historyMapper.historyItemToDto(historyItem)).thenReturn(historyItemDto);

        List<HistoryItemDto> historyItems = playerService.getAllHistory(player.getId());

        assertEquals(1, historyItems.size());
        assertEquals(historyItemDto, historyItems.get(0));
    }

    @Test
    public void testGetAllHistoryWhenPlayerDoesNotExistThenReturnEmptyList() {
        when(historyRepository.getAllByPlayerId(player.getId())).thenReturn(Collections.emptyList());

        List<HistoryItemDto> historyItems = playerService.getAllHistory(player.getId());

        assertEquals(0, historyItems.size());
    }

    @Test
    public void testGetAllHistoryWhenPlayerIdIsNullThenReturnEmptyList() {
        List<HistoryItemDto> historyItems = playerService.getAllHistory(null);

        assertEquals(0, historyItems.size());
    }
}