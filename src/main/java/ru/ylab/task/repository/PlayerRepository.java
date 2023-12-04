package ru.ylab.task.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.exception.ImpossibleTransactionException;
import ru.ylab.task.exception.LoginExistsException;
import ru.ylab.task.exception.NotFoundException;
import ru.ylab.task.model.Player;

import java.util.Optional;

/**
 * The interface Player repository.
 */

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Modifying
    @Query("UPDATE Player p SET p.balance = p.balance + :amount WHERE p.id = :id")
    void changePlayerBalanceById(@Param("id") Long id, @Param("amount") double amount);

    boolean existsByLogin(String login);

    Optional<Player> findById(Long id);

    Optional<Player> findByLoginAndPassword(String login, int password);

}
