package ec.com.todo.apptasks.board.service;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.entity.Board;

import java.util.List;

public interface BoardService {
    BoardDTO save(CreateBoardDTO bDTO);
    List<BoardDTO> getAll();
    void delete(DeleteBoardDTO bDTO);
    BoardDTO update(UpdateBoardDTO bDTO);
    Board getReferenceById(Long id);
    Board getBoardOrThrow(Long id);
}
