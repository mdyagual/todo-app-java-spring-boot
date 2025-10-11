package ec.com.todo.apptasks.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.todo.apptasks.board.dto.request.CreateBoardDTO;
import ec.com.todo.apptasks.board.dto.request.DeleteBoardDTO;
import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.board.dto.response.BoardDTO;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.GlobalExceptionHandler;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BoardService boardService;

    private BoardController boardController;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        boardController = new BoardController(boardService);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //Success scenarios

    @Test
    void getAllBoards_shouldReturnContent() throws Exception {
        //0.
        List<BoardDTO> boards = new ArrayList<>();
        boards.add(new BoardDTO(1L, "Board 1", LocalDate.now(), LocalDate.now(),true,new ArrayList<>()));
        boards.add(new BoardDTO(2L, "Board 2", LocalDate.now(), LocalDate.now(),true,new ArrayList<>()));


        //1.
        Mockito.when(boardService.getAll()).thenReturn(boards);

        //2. y //3.
        mockMvc.perform(get("/api/boards/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        //4.
        Mockito.verify(boardService).getAll();

    }

    @Test
    void createBoard_shouldSuccess() throws Exception {
        //0.
        CreateBoardDTO createBoardDTO = new CreateBoardDTO("Board", 1L);
        BoardDTO boardDTO = new BoardDTO(1L, "Board", LocalDate.now(), LocalDate.now(),true,new ArrayList<>());

        //1.
        Mockito.when(boardService.save(Mockito.any(CreateBoardDTO.class))).thenReturn(boardDTO);

        //2. y //3.
        mockMvc.perform(post("/api/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createBoardDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(createBoardDTO.getTitle()));

        //4.
        Mockito.verify(boardService).save(Mockito.any(CreateBoardDTO.class));


    }

    @Test
    void updateBoard_shouldSuccess() throws Exception {
        //0.
        UpdateBoardDTO updateBoardDTO = new UpdateBoardDTO(1L, "BoardChanged");
        BoardDTO boardDTO = new BoardDTO(1L, "BoardChanged", LocalDate.now(), LocalDate.now(),true,new ArrayList<>());

        //1.
        Mockito.when(boardService.update(Mockito.any(UpdateBoardDTO.class))).thenReturn(boardDTO);

        //2. y //3
        mockMvc.perform(post("/api/boards/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBoardDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateBoardDTO.getTitle()));

        //4.
        Mockito.verify(boardService).update(Mockito.any(UpdateBoardDTO.class));

    }

    @Test
    void deleteBoard_shouldSuccess() throws Exception {
        //0.
        DeleteBoardDTO deleteBoardDTO = new DeleteBoardDTO(1L);

        //1.
        Mockito.doNothing().when(boardService).delete(Mockito.any(DeleteBoardDTO.class));

        //2. y //3.
        mockMvc.perform(post("/api/boards/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteBoardDTO)))
                .andExpect(status().isAccepted());

        //4.
        Mockito.verify(boardService).delete(Mockito.any(DeleteBoardDTO.class));


    }

    //Failure scenarios
    @Test
    void getBoards_ShouldReturnEmpty() throws Exception{
        //1.
        Mockito.when(boardService.getAll()).thenReturn(new ArrayList<>());

        //2. y //3
        mockMvc.perform(get("/api/boards/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        //4.
        Mockito.verify(boardService).getAll();

    }

    @Test
    void saveBoard_duplicatedFailure() throws Exception{
        //0.
        CreateBoardDTO createBoardDTO = new CreateBoardDTO("Board", 1L);

        //1.
        Mockito.when(boardService.save(Mockito.any(CreateBoardDTO.class))).thenThrow(new DuplicateResourceException("Board", List.of(createBoardDTO.getTitle(), createBoardDTO.getUserId().toString())));

        //2. y //3.
        mockMvc.perform(post("/api/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createBoardDTO)))
                .andExpect(status().isConflict());

        //4.
        Mockito.verify(boardService).save(Mockito.any(CreateBoardDTO.class));
    }

    @Test
    void updateBoard_NotFoundFailure() throws Exception{
        //0.
        UpdateBoardDTO updateBoardDTO = new UpdateBoardDTO(1L, "BoardChanged");

        //1.
        Mockito.when(boardService.update(Mockito.any(UpdateBoardDTO.class))).thenThrow(new ResourceNotFoundException("Board", updateBoardDTO.getId()));

        //2. y //3.
        mockMvc.perform(post("/api/boards/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBoardDTO)))
                .andExpect(status().isNotFound());

        //4.
        Mockito.verify(boardService).update(Mockito.any(UpdateBoardDTO.class));

    }

    @Test
    void deleteBoard_NotFoundFailure() throws Exception{
        //0.
        DeleteBoardDTO deleteBoardDTO = new DeleteBoardDTO(1L);

        //1.
        Mockito.doThrow(new ResourceNotFoundException("User", deleteBoardDTO.getId())).when(boardService).delete(Mockito.any(DeleteBoardDTO.class));

        //2. y //3.
        mockMvc.perform(post("/api/boards/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteBoardDTO)))
                .andExpect(status().isNotFound());

        //4.
        Mockito.verify(boardService).delete(Mockito.any(DeleteBoardDTO.class));

    }
}