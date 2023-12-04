package ru.ylab.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.task.dto.HistoryItemDto;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.exception.NotFoundException;
import ru.ylab.task.mapper.HistoryMapper;
import ru.ylab.task.model.HistoryItem;
import ru.ylab.task.model.Player;
import ru.ylab.task.repository.HistoryRepository;
import ru.ylab.task.repository.PlayerRepository;
import ru.ylab.task.service.PlayerService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The type Player service.
 */

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;


    public Long registerPlayer(String login, String password) throws LoginExistsException {
        if (playerRepository.existsByLogin(login)) {
            throw new LoginExistsException();
        }
        Player player = new Player(login, Arrays.hashCode(password.toCharArray()));
        return playerRepository.save(player).getId();
    }

    @Override
    public Player authorizationPlayer(String login, String password) throws NotFoundException {
        Optional<Player> player = playerRepository.findByLoginAndPassword(login,Arrays.hashCode(password.toCharArray()));
        if (player.isPresent()) {
            return player.get();
        }
        throw new NotFoundException();
    }

    /**
     * Метод возвращает лист всех действий пользователя
     *
     * @param playerId the player id
     * @return
     */
    public List<HistoryItemDto> getAllHistory(Long playerId) {
        return historyRepository.getAllByPlayerId(playerId).stream().map(historyMapper::historyItemToDto).toList();
    }

    /**
     * Добавление объекта "история" в репозиторий истории(в мапу) с рандомным id самого объекта HistoryItem,id игрока,действие игрока
     * и время операции
     *
     * @param playerId the player id
     * @param action   the action
     */
    public void addActionToHistory(Long playerId, String action) {
        if (playerId != null) {
            Player player = playerRepository.findById(playerId).get();
            historyRepository.save(new HistoryItem(player, action, LocalDateTime.now()));
        }
    }


}
