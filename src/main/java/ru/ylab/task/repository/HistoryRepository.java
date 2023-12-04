package ru.ylab.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ylab.task.model.HistoryItem;

import java.util.List;


public interface HistoryRepository extends JpaRepository<HistoryItem, Long> {

    List<HistoryItem> getAllByPlayerId(Long playerId);


}
