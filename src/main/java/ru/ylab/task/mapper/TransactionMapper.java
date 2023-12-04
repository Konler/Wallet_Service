package ru.ylab.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.task.dto.TransactionDto;
import ru.ylab.task.model.transaction.Transaction;
import ru.ylab.task.model.transaction.TransactionType;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "playerId", expression = "java(transactionDto.getPlayerId())")
    @Mapping(target = "amount", expression = "java(transactionDto.getAmount())")
    @Mapping(target = "type", expression = "java(TransactionMapper.convertTransactionType(transactionDto.getType()))")
    Transaction transactionDtoToTransaction(TransactionDto transactionDto);

    @Mapping(target = "playerId",  expression = "java(transaction.getPlayerId())")
    @Mapping(target = "amount",  expression = "java(transaction.getAmount())")
    @Mapping(target = "type", expression = "java(String.valueOf(transaction.getType()))")
    @Mapping(target = "state", expression = "java(String.valueOf(transaction.getState()))")
    TransactionDto transactionToTransactionDto(Transaction transaction);

    static TransactionType convertTransactionType(String type) {
        return TransactionType.valueOf(type);
    }

}
