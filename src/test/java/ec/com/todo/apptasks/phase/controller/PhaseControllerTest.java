package ec.com.todo.apptasks.phase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.todo.apptasks.board.exception.NumberOfPhasesException;
import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.entity.PhaseName;
import ec.com.todo.apptasks.phase.service.PhaseService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.GlobalExceptionHandler;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class PhaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PhaseService phaseService;

    private PhaseController phaseController;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        phaseController = new PhaseController(phaseService);
        mockMvc = MockMvcBuilders.standaloneSetup(phaseController).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //Success scenarios

    @Test
    void getAllPhases_shouldReturnContent() throws Exception{
        List<PhaseDTO> phases = new ArrayList<>();
        phases.add(new PhaseDTO(1L,"TO DO", LocalDate.now(),LocalDate.now(), false, new ArrayList<>()));
        phases.add(new PhaseDTO(2L,"IN PROGRESS", LocalDate.now(),LocalDate.now(), false, new ArrayList<>()));

        Mockito.when(phaseService.getAll()).thenReturn(phases);

        mockMvc.perform(get("/api/phases/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        Mockito.verify(phaseService).getAll();
    }

    @Test
    void createPhase_shouldSuccess() throws Exception {
        CreatePhaseDTO createPhaseDTO = new CreatePhaseDTO(PhaseName.TO_DO,2L);
        PhaseDTO phaseDTO = new PhaseDTO(4L, "TO DO", LocalDate.now(), LocalDate.now(), false, new ArrayList<>());

        Mockito.when(phaseService.save(Mockito.any(CreatePhaseDTO.class))).thenReturn(phaseDTO);

        mockMvc.perform(post("/api/phases/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createPhaseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createPhaseDTO.getName().toValue()));

        Mockito.verify(phaseService).save(Mockito.any(CreatePhaseDTO.class));

    }

    @Test
    void updatePhase_shouldSuccess() throws Exception {
        UpdatePhaseDTO updatePhaseDTO = new UpdatePhaseDTO(4L, PhaseName.DONE);
        PhaseDTO phaseDTO = new PhaseDTO(1L, "DONE", LocalDate.now(), LocalDate.now(),false, new ArrayList<>());

        Mockito.when(phaseService.update(Mockito.any(UpdatePhaseDTO.class))).thenReturn(phaseDTO);

        mockMvc.perform(post("/api/phases/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePhaseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatePhaseDTO.getName().toValue()));

        Mockito.verify(phaseService).update(Mockito.any(UpdatePhaseDTO.class));

    }

    @Test
    void deletePhase_shouldSuccess() throws Exception{
        DeletePhaseDTO deletePhaseDTO = new DeletePhaseDTO(1L);

        Mockito.doNothing().when(phaseService).delete(Mockito.any(DeletePhaseDTO.class));

        mockMvc.perform(post("/api/phases/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deletePhaseDTO)))
                .andExpect(status().isAccepted());

        Mockito.verify(phaseService).delete(Mockito.any(DeletePhaseDTO.class));
    }

    //Failure scenarios
    @Test
    void getPhases_shouldReturnEmpty() throws Exception{
        Mockito.when(phaseService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/phases/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        Mockito.verify(phaseService).getAll();

    }

    @Test
    void savePhase_duplicatedFailure() throws Exception{
        CreatePhaseDTO createPhaseDTO = new CreatePhaseDTO(PhaseName.TO_DO,2L);

        Mockito.when(phaseService.save(Mockito.any(CreatePhaseDTO.class))).thenThrow(new DuplicateResourceException("Phase", List.of(createPhaseDTO.getName().name(), createPhaseDTO.getBoardId().toString())));

        mockMvc.perform(post("/api/phases/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createPhaseDTO)))
                .andExpect(status().isConflict());

        Mockito.verify(phaseService).save(Mockito.any(CreatePhaseDTO.class));


    }

    @Test
    void savePhase_NoOfPhasesFailure() throws Exception{
        CreatePhaseDTO createPhaseDTO = new CreatePhaseDTO(PhaseName.TO_DO,2L);

        Mockito.when(phaseService.save(Mockito.any(CreatePhaseDTO.class))).thenThrow(new NumberOfPhasesException(5));

        mockMvc.perform(post("/api/phases/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createPhaseDTO)))
                .andExpect(status().isBadRequest());

        Mockito.verify(phaseService).save(Mockito.any(CreatePhaseDTO.class));
    }

    @Test
    void updatePhase_NotFoundFailure() throws Exception{
        UpdatePhaseDTO updatePhaseDTO = new UpdatePhaseDTO(4L, PhaseName.DONE);

        Mockito.when(phaseService.update(Mockito.any(UpdatePhaseDTO.class))).thenThrow(new ResourceNotFoundException("Phase", updatePhaseDTO.getId()));

        mockMvc.perform(post("/api/phases/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePhaseDTO)))
                .andExpect(status().isNotFound());

        Mockito.verify(phaseService).update(Mockito.any(UpdatePhaseDTO.class));
    }

    @Test
    void deletePhase_NotFoundFailure() throws Exception{
        DeletePhaseDTO deletePhaseDTO = new DeletePhaseDTO(1L);

        Mockito.doThrow(new ResourceNotFoundException("Phase", deletePhaseDTO.getId())).when(phaseService).delete(Mockito.any(DeletePhaseDTO.class));

        mockMvc.perform(post("/api/phases/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deletePhaseDTO)))
                .andExpect(status().isNotFound());

        Mockito.verify(phaseService).delete(Mockito.any(DeletePhaseDTO.class));
    }
}