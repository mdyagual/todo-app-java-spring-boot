package ec.com.todo.apptasks.board.service.impl;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.board.mapper.BoardMapper;
import ec.com.todo.apptasks.board.repository.BoardRepository;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.entity.User;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {
    /*Testing preparation
    * 1. Elements that the service (in this case) needs to work
    * 2. Mock all of them except the service
    * 3. Set up the before each with the serviceImpl with all the elems that needs
    * */
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardMapper mapper;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardServiceImpl(boardRepository,
                userService,
                mapper,
                entityManager);
    }

    /* Testing steps:
        - 0. Input data (if needed, optional output data too)
        - 1. Establish all mock behavior needed
        - 2. Call the method to test
        - 3. Verify the results
        - 4. Verify all interactions (the when's) with mocks
    */

    //Success cases
    @Test
    void saveBoardSuccess() {
        //0
        CreateBoardDTO bDTO = new CreateBoardDTO("Board Test", 1L);
        Board mappedBoard = new Board();
        mappedBoard.setTitle(bDTO.getTitle());

        User userRef = new User(); //*To do the reference
        userRef.setId(bDTO.getUserId());

        //1
        Mockito.when(boardRepository.existsByTitleAndUserId(bDTO.getTitle(), bDTO.getUserId()))
                .thenReturn(false);

        Mockito.when(mapper.toEntity(Mockito.any(CreateBoardDTO.class)))
                .thenReturn(mappedBoard);

        Mockito.when(userService.getReferenceById(bDTO.getUserId()))
                .thenReturn(userRef);

        Mockito.when(boardRepository.save(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> {
                    Board boardToSave = invocationOnMock.getArgument(0);
                    boardToSave.setId(1L);
                    //*PrePersist stuff must be done here
                    boardToSave.setCreatedAt(LocalDate.now());
                    boardToSave.setLastModifiedAt(LocalDate.now());
                    boardToSave.setIsActive(true);
                    return boardToSave;
                });

        Mockito.when(mapper.toDTO(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> {
                    Board savedBoard = invocationOnMock.getArgument(0);
                    return new BoardDTO(savedBoard.getId(),
                            savedBoard.getTitle(),
                            savedBoard.getCreatedAt(),
                            savedBoard.getLastModifiedAt(),
                            savedBoard.getIsActive(),
                            new ArrayList<>());
                });


        //2
        BoardDTO result = boardService.save(bDTO);

        //3
        assertAll("Board saved",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Board Test", result.getTitle()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNotNull(result.getLastModifiedAt()),
                () -> assertTrue(result.getIsActive())
        );

        //4
        Mockito.verify(boardRepository).existsByTitleAndUserId(bDTO.getTitle(), bDTO.getUserId());
        Mockito.verify(mapper).toEntity(bDTO);
        Mockito.verify(userService).getReferenceById(bDTO.getUserId());
        Mockito.verify(boardRepository).save(mappedBoard);
        Mockito.verify(mapper).toDTO(Mockito.any(Board.class));

    }

    @Test
    void getAllSuccess() {
        //0
        List<Board> boards = new ArrayList<>();
        boards.add(new Board(1L, "Board 1", LocalDate.now(), LocalDate.now(), true, new User(), new ArrayList<>()));
        boards.add(new Board(2L, "Board 2", LocalDate.now(), LocalDate.now(), true, new User(), new ArrayList<>()));
        boards.add(new Board(3L, "Board 3", LocalDate.now(), LocalDate.now(), true, new User(), new ArrayList<>()));

        //1
        Mockito.when(boardRepository.findAll()).thenReturn(boards);

        Mockito.when(mapper.toDTO(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> {
                    Board b = invocationOnMock.getArgument(0);
                    return new BoardDTO(b.getId(),
                            b.getTitle(),
                            b.getCreatedAt(),
                            b.getLastModifiedAt(),
                            b.getIsActive(),
                            new ArrayList<>());
                });

        //2
        List<BoardDTO> result = boardService.getAll();

        //3
        assertAll("All boards",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size())
        );

        //4
        Mockito.verify(boardRepository).findAll();
        Mockito.verify(mapper, Mockito.times(3)).toDTO(Mockito.any(Board.class));
    }

    @Test
    void updateBoardSuccess() {
        //0
        UpdateBoardDTO bDTO = new UpdateBoardDTO(1L, "Updated Board" );
        Board existingBoard = new Board(1L, "Old Board", LocalDate.now().minusDays(5), LocalDate.now().minusDays(5), true, new User(), new ArrayList<>());
        LocalDate previousLastModifiedAt = existingBoard.getLastModifiedAt();

        //1
        Mockito.when(boardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(existingBoard));

        Mockito.doAnswer(invocationOnMock -> {
            Board boardToUpdate = invocationOnMock.getArgument(0);
            boardToUpdate.setTitle(bDTO.getTitle());
            boardToUpdate.setLastModifiedAt(LocalDate.now());
            return null;
        }).when(mapper).updateEntity(Mockito.any(Board.class), Mockito.any(UpdateBoardDTO.class));


        Mockito.when(boardRepository.save(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.toDTO(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> {
                    Board updatedBoard = invocationOnMock.getArgument(0);
                    return new BoardDTO(updatedBoard.getId(),
                            updatedBoard.getTitle(),
                            updatedBoard.getCreatedAt(),
                            updatedBoard.getLastModifiedAt(),
                            updatedBoard.getIsActive(),
                            new ArrayList<>()
                    );
                });

        //2
        BoardDTO result = boardService.update(bDTO);


        //3
        assertAll("Board updated",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(bDTO.getId(), result.getId(), "The IDs should match"),
                () -> assertEquals(bDTO.getTitle(), result.getTitle(), "The titles should match"),
                () -> assertTrue(result.getLastModifiedAt().isAfter(previousLastModifiedAt), "The last modified date should be updated"),
                () -> assertTrue(result.getIsActive(), "The board should remain active")
        );

        //4
        Mockito.verify(boardRepository).findById(bDTO.getId());
        Mockito.verify(mapper).updateEntity(existingBoard, bDTO);
        Mockito.verify(boardRepository).save(existingBoard);
        Mockito.verify(mapper).toDTO(existingBoard);

    }

    @Test
    void deleteBoardSuccess() {
        //0
        DeleteBoardDTO bDTO = new DeleteBoardDTO(1L);
        Board existingBoard = new Board(1L, "Old Board", LocalDate.now().minusDays(5), LocalDate.now().minusDays(5), true, new User(), new ArrayList<>());

        //1
        Mockito.when(boardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(existingBoard));
        Mockito.when(boardRepository.save(Mockito.any(Board.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //2
        assertDoesNotThrow(() -> boardService.delete(bDTO));

        //3
        assertFalse(existingBoard.getIsActive(), "The board should be marked as inactive");

        //4
        Mockito.verify(boardRepository).findById(bDTO.getId());
        Mockito.verify(boardRepository).save(existingBoard);

    }

    @Test
    void saveBoardDuplicateFailure(){
        //0
        CreateBoardDTO bDTO = new CreateBoardDTO("Board Test", 1L);

        //1
        Mockito.when(boardRepository.existsByTitleAndUserId(bDTO.getTitle(), bDTO.getUserId()))
                .thenReturn(true);
        //2
        Exception exception = assertThrows(DuplicateResourceException.class, () -> boardService.save(bDTO));

        //3
        assertInstanceOf(DuplicateResourceException.class, exception);

        //4
        Mockito.verify(boardRepository).existsByTitleAndUserId(bDTO.getTitle(), bDTO.getUserId());
        Mockito.verifyNoMoreInteractions(boardRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void updateBoardNotFoundFailure() {
        //0
        UpdateBoardDTO bDTO = new UpdateBoardDTO(1L, "Updated Board");

        //1
        Mockito.when(boardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> boardService.update(bDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(boardRepository).findById(bDTO.getId());
        Mockito.verifyNoMoreInteractions(boardRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void deleteBoardNotFoundFailure() {
        //0
        DeleteBoardDTO bDTO = new DeleteBoardDTO(1L);

        //1
        Mockito.when(boardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> boardService.delete(bDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(boardRepository).findById(bDTO.getId());
        Mockito.verifyNoMoreInteractions(boardRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(userService);

    }
}