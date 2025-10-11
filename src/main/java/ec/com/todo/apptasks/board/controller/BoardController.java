package ec.com.todo.apptasks.board.controller;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BoardDTO>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<BoardDTO> createBoard(@Valid @RequestBody CreateBoardDTO boardDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.save(boardDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<BoardDTO> updateBoard(@Valid @RequestBody UpdateBoardDTO boardDTO) {
        return ResponseEntity.ok(boardService.update(boardDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteBoard(@Valid @RequestBody DeleteBoardDTO boardDTO) {
        boardService.delete(boardDTO);
        return ResponseEntity.accepted().build();
    }

}
