package ru.ylab.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.task.dto.HistoryItemDto;
import ru.ylab.task.model.HistoryItem;

@Mapper
public interface HistoryMapper {

    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    @Mapping(expression = "java(item.getId())", target = "id")
    @Mapping(expression = "java(item.getPlayerId())", target = "playerId")
    @Mapping(expression = "java(item.getAction())", target = "action")
    @Mapping(target = "time", expression = "java(item.getTime().toString())")
    HistoryItemDto historyItemToDto(HistoryItem item);
}
