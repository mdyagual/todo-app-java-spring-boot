package ec.com.todo.apptasks.board.service;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;

import java.util.List;

public interface BoardService {
    void save(CreateBoardDTO bDTO);
    List<BoardDTO> getAll();
    void delete(DeleteBoardDTO bDTO);
    void update(UpdateBoardDTO bDTO);
}
