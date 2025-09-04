package ec.com.todo.apptasks.board.service.impl;

import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.board.mapper.BoardMapper;
import ec.com.todo.apptasks.board.repository.BoardRepository;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.user.entity.User;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

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

    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}