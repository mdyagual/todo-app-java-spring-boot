package ec.com.todo.apptasks.board.controller;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/all")
    public List<BoardDTO> getAllBoards() {
        return boardService.getAll();
    }

    @PostMapping("/create")
    public void createBoard(@Valid @RequestBody CreateBoardDTO boardDTO) {
        boardService.save(boardDTO);
    }

    @PostMapping("/update")
    public void updateBoard(@Valid @RequestBody UpdateBoardDTO boardDTO) {
        boardService.update(boardDTO);
    }

    @PostMapping("/delete")
    public void deleteBoard(@Valid @RequestBody DeleteBoardDTO boardDTO) {
        boardService.delete(boardDTO);
    }

}
