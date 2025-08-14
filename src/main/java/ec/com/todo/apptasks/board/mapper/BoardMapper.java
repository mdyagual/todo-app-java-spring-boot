package ec.com.todo.apptasks.board.mapper;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "phases", ignore = true)
    Board toEntity(CreateBoardDTO boardDTO);

    @Mapping(target = "phases",
            expression = "java(board.getPhases() == null ? java.util.Collections.emptyList() : board.getPhases().stream().map(p -> p.getId()).toList())")
    BoardDTO toDTO (Board board);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "phases", ignore = true)
    void updateEntity(@MappingTarget Board board, UpdateBoardDTO dto);

}
