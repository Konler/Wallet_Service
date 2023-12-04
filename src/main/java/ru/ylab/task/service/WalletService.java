package ru.ylab.task.service;

import ru.ylab.task.dto.TransactionDto;
import ru.ylab.task.exception.DbException;

import java.util.List;


/**
 * The interface Wallet service.
 */
public interface WalletService {

    boolean activateTransaction(TransactionDto transactionDto);


    /**
     * Find transaction history list.
     *
     * @param playerId the player id
     * @return the list
     */
    List<TransactionDto> findTransactionHistory(Long playerId);
}
