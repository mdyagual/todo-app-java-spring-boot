package ec.com.todo.apptasks.board.service.impl;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.board.mapper.BoardMapper;
import ec.com.todo.apptasks.board.mapper.BoardMapperImpl;
import ec.com.todo.apptasks.board.repository.BoardRepository;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final EntityManager entityManager;
    private final BoardMapper mapper;

    public BoardServiceImpl(BoardRepository boardRepository, UserService userService, BoardMapper mapper, EntityManager entityManager) {
        this.boardRepository = boardRepository;
        this.userService = userService;
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Override
    public Board getReferenceById(Long id) {
        return entityManager.getReference(Board.class, id);
    }

    @Override
    public Board getBoardOrThrow(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found: " + id));
    }

    @Override
    public BoardDTO save(CreateBoardDTO bDTO) {
        if (boardRepository.existsByTitleAndUserId(bDTO.getTitle(),bDTO.getUserId())) {
            throw new DuplicateResourceException("Board", List.of("title", "userId"));
        }
        Board board = mapper.toEntity(bDTO);
        board.setUser(userService.getReferenceById(bDTO.getUserId()));
        return mapper.toDTO(boardRepository.save(board));

    }

    @Override
    public List<BoardDTO> getAll() {
        return boardRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public BoardDTO update(UpdateBoardDTO bDTO) {
        return boardRepository.findById(bDTO.getId())
                .map(board -> {
                        mapper.updateEntity(board, bDTO);
                        board.setLastModifiedAt(LocalDate.now());
                        return mapper.toDTO(boardRepository.save(board));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Board", bDTO.getId()));
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
