package ru.ylab.task.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.task.dto.TransactionDto;
import ru.ylab.task.exception.DbException;
import ru.ylab.task.exception.ImpossibleTransactionException;
import ru.ylab.task.mapper.TransactionMapper;
import ru.ylab.task.model.transaction.State;
import ru.ylab.task.model.transaction.Transaction;
import ru.ylab.task.repository.PlayerRepository;
import ru.ylab.task.repository.TransactionRepository;
import ru.ylab.task.service.WalletService;

import java.util.List;

import static ru.ylab.task.model.transaction.TransactionType.DEBIT;

/**
 * The type Wallet service.
 */

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final TransactionRepository transactionRepository;

    private final PlayerRepository playerRepository;

    private final TransactionMapper mapper;


    @Transactional
    public boolean activateTransaction(TransactionDto transactionDto) {
        Transaction newTransaction = mapper.transactionDtoToTransaction(transactionDto);
        newTransaction.setState(State.UNACTIVATED);
        Transaction transaction = transactionRepository.save(newTransaction);
        if (DEBIT.equals(newTransaction.getType())) {
            transaction.setAmount(transaction.getAmount() * -1);
        }
        playerRepository.changePlayerBalanceById(transaction.getPlayerId(), transaction.getAmount());
        transaction.setState(State.SUCCESS);
        return true;

    }


    public List<TransactionDto> findTransactionHistory(Long playerId) {
        return transactionRepository.findAllByPlayerId(playerId).stream().map(mapper::transactionToTransactionDto).toList();
    }
}
