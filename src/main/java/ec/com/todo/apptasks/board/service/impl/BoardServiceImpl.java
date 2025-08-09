package ec.com.todo.apptasks.board.service.impl;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.mapper.BoardMapper;
import ec.com.todo.apptasks.board.mapper.BoardMapperImpl;
import ec.com.todo.apptasks.board.repository.BoardRepository;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper mapper;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
        this.mapper = new BoardMapperImpl();
    }

    @Override
    public void save(CreateBoardDTO bDTO) {
        boardRepository.save(mapper.toEntity(bDTO));
    }

    @Override
    public List<BoardDTO> getAll() {
        return boardRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void update(UpdateBoardDTO bDTO) {
        boardRepository.findById(bDTO.getId())
                .ifPresentOrElse(
                        board -> {

                            mapper.updateEntity(board, bDTO);
                            board.setLastModifiedAt(LocalDate.now());
                            boardRepository.save(board);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Board", bDTO.getId());
                        }
                );
    }

    @Override
    public void delete(DeleteBoardDTO bDTO) {
        boardRepository.findById(bDTO.getId())
                .ifPresentOrElse(
                        board -> {
                            board.setIsActive(false);
                            board.setLastModifiedAt(LocalDate.now());
                            boardRepository.save(board);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Board", bDTO.getId());
                        }
                );

    }


}
