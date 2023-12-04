package ru.ylab.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.model.transaction.Transaction;

import java.util.List;

/**
 * The interface Transaction repository.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPlayerId(Long playerId);

}
