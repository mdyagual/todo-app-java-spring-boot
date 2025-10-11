package ec.com.todo.apptasks.phase.service.impl;

import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.entity.Phase;
import ec.com.todo.apptasks.phase.entity.PhaseName;
import ec.com.todo.apptasks.phase.mapper.PhaseMapper;
import ec.com.todo.apptasks.phase.repository.PhaseRepository;
import ec.com.todo.apptasks.phase.service.PhaseService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PhaseServiceImplTest {
    /*Testing preparation
     * 1. Elements that the service (in this case) needs to work
     * 2. Mock all of them except the service
     * 3. Set up the before each with the serviceImpl with all the elems that needs
     * */

    @Mock
    private PhaseRepository phaseRepository;

    @Mock
    private PhaseMapper mapper;

    @Mock
    private BoardService boardService;

    @Mock
    private EntityManager entityManager;

    private PhaseService phaseService;

    @BeforeEach
    void setUp() {
        phaseService = new PhaseServiceImpl(phaseRepository,
                mapper,
                boardService,
                entityManager);
    }

    /* Testing steps:
        - 0. Input data (if needed, optional output data too)
        - 1. Establish all mock behavior needed
        - 2. Call the method to test
        - 3. Verify the results
        - 4. Verify all interactions (the when's) with mocks
    */

    @Test
    void savePhaseSuccess() {
        //0
        CreatePhaseDTO pDTO = new CreatePhaseDTO(PhaseName.TO_DO, 1L);
        Phase mappedPhase = new Phase();
        mappedPhase.setName(pDTO.getName());

        Board boardRef = new Board();
        boardRef.setId(pDTO.getBoardId());
        boardRef.setPhases(new ArrayList<>(){{add(new Phase());}}); //1 phase already in board

        //1
        Mockito.when(phaseRepository.existsByNameAndBoardId(pDTO.getName(), pDTO.getBoardId()))
                .thenReturn(false);

        Mockito.when(mapper.toEntity(Mockito.any(CreatePhaseDTO.class)))
                .thenReturn(mappedPhase);

        Mockito.when(boardService.getReferenceById(pDTO.getBoardId()))
                .thenReturn(boardRef);

        Mockito.when(phaseRepository.save(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> {
                    Phase phaseToSave = invocation.getArgument(0);
                    phaseToSave.setId(1L);
                    //*PrePersist stuff must be done here
                    phaseToSave.setCreatedAt(LocalDate.now());
                    phaseToSave.setLastModifiedAt(LocalDate.now());
                    phaseToSave.setIsActive(true);
                    return phaseToSave;
                });

        Mockito.when(mapper.toDTO(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> {
                    Phase savedPhase = invocation.getArgument(0);
                    return new PhaseDTO(savedPhase.getId(),
                            savedPhase.getName().name(),
                            savedPhase.getCreatedAt(),
                            savedPhase.getLastModifiedAt(),
                            savedPhase.getIsActive(),
                            new ArrayList<>());
                });

        //2
        PhaseDTO result = phaseService.save(pDTO);

        //3
        assertAll("Phase saved",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(pDTO.getName().name(), result.getName()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNotNull(result.getLastModifiedAt()),
                () -> assertFalse(result.getIsActive()),
                () -> assertNotNull(result.getTasks()),
                () -> assertTrue(result.getTasks().isEmpty())
        );

        //4
        Mockito.verify(phaseRepository).existsByNameAndBoardId(pDTO.getName(), pDTO.getBoardId());
        Mockito.verify(mapper).toEntity(pDTO);
        //it calls twice because it calls it once to set the board and once to return the saved phase. What a troll
        Mockito.verify(boardService, Mockito.times(2)).getReferenceById(pDTO.getBoardId());
        Mockito.verify(phaseRepository).save(mappedPhase);
        Mockito.verify(mapper).toDTO(Mockito.any(Phase.class));

    }

    @Test
    void getAllSuccess() {
        //0
        List<Phase> phases = new ArrayList<>();
        phases.add(new Phase(1L, PhaseName.TO_DO, LocalDate.now(), LocalDate.now(), true, new Board(), new ArrayList<>()));
        phases.add(new Phase(2L, PhaseName.IN_PROGRESS, LocalDate.now(), LocalDate.now(), true, new Board(), new ArrayList<>()));
        phases.add(new Phase(3L, PhaseName.DONE, LocalDate.now(), LocalDate.now(), true, new Board(), new ArrayList<>()));

        //1
        Mockito.when(phaseRepository.findAll())
                .thenReturn(phases);
        Mockito.when(mapper.toDTO(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> {
                    Phase phase = invocation.getArgument(0);
                    return new PhaseDTO(phase.getId(),
                            phase.getName().name(),
                            phase.getCreatedAt(),
                            phase.getLastModifiedAt(),
                            phase.getIsActive(),
                            new ArrayList<>());
                });

        //2
        List<PhaseDTO> result = phaseService.getAll();

        //3
        assertAll("All phases",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size())
        );

        //4
        Mockito.verify(phaseRepository).findAll();
        Mockito.verify(mapper, Mockito.times(3)).toDTO(Mockito.any(Phase.class));

    }

    @Test
    void updatePhaseSuccess() {
        //0
        UpdatePhaseDTO uDTO = new UpdatePhaseDTO(1L, PhaseName.DONE);
        Phase existingPhase = new Phase(1L, PhaseName.TO_DO, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), true, new Board(), new ArrayList<>());
        LocalDate previousLastModifiedAt = existingPhase.getLastModifiedAt();

        //1
        Mockito.when(phaseRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.of(existingPhase));

        Mockito.doAnswer(invocation -> {
            Phase phaseToUpdate = invocation.getArgument(0);
            phaseToUpdate.setName(uDTO.getName());
            phaseToUpdate.setLastModifiedAt(LocalDate.now());
            return null;

        }).when(mapper).updateEntity(Mockito.any(Phase.class), Mockito.any(UpdatePhaseDTO.class));

        Mockito.when(phaseRepository.save(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(mapper.toDTO(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> {
                    Phase updatedPhase = invocation.getArgument(0);
                    return new PhaseDTO(updatedPhase.getId(),
                            updatedPhase.getName().name(),
                            updatedPhase.getCreatedAt(),
                            updatedPhase.getLastModifiedAt(),
                            updatedPhase.getIsActive(),
                            new ArrayList<>()
                    );
                });

        //2
        PhaseDTO result = phaseService.update(uDTO);

        //3
        assertAll("Phase updated",
                () -> assertNotNull(result),
                () -> assertEquals(uDTO.getId(), result.getId()),
                () -> assertEquals(uDTO.getName().name(), result.getName()),
                () -> assertTrue(result.getLastModifiedAt().isAfter(previousLastModifiedAt)),
                () -> assertTrue(result.getIsActive())
        );

        //4
        Mockito.verify(phaseRepository).findById(uDTO.getId());
        Mockito.verify(mapper).updateEntity(existingPhase, uDTO);
        Mockito.verify(phaseRepository).save(existingPhase);
        Mockito.verify(mapper).toDTO(existingPhase);
    }

    @Test
    void deletePhaseSuccess() {
        //0
        DeletePhaseDTO dDTO = new DeletePhaseDTO(1L);
        Phase existingPhase = new Phase(1L, PhaseName.TO_DO, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), true, new Board(), new ArrayList<>());

        //1
        Mockito.when(phaseRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.of(existingPhase));

        Mockito.when(phaseRepository.save(Mockito.any(Phase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //2
        assertDoesNotThrow(() -> phaseService.delete(dDTO));

        //3
        assertFalse(existingPhase.getIsActive(),"Phase is deactivated");

        //4
        Mockito.verify(phaseRepository).findById(dDTO.getId());
        Mockito.verify(phaseRepository).save(existingPhase);
    }

    @Test
    void saveDuplicateFailure() {
        //0
        CreatePhaseDTO pDTO = new CreatePhaseDTO(PhaseName.TO_DO, 1L);

        //1
        Mockito.when(phaseRepository.existsByNameAndBoardId(pDTO.getName(), pDTO.getBoardId()))
                .thenReturn(true);

        //2
        Exception exception = assertThrows(DuplicateResourceException.class, () -> phaseService.save(pDTO));

        //3
        assertInstanceOf(DuplicateResourceException.class, exception);

        //4
        Mockito.verify(phaseRepository).existsByNameAndBoardId(pDTO.getName(), pDTO.getBoardId());
        Mockito.verifyNoMoreInteractions(phaseRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(boardService);
    }

    @Test
    void updatePhaseNotFoundFailure() {
        //0
        UpdatePhaseDTO uDTO = new UpdatePhaseDTO(1L, PhaseName.DONE);

        //1
        Mockito.when(phaseRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> phaseService.update(uDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(phaseRepository).findById(uDTO.getId());
        Mockito.verifyNoMoreInteractions(phaseRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(boardService);
    }

    @Test
    void deletePhaseNotFoundFailure() {
        //0
        DeletePhaseDTO dDTO = new DeletePhaseDTO(1L);

        //1
        Mockito.when(phaseRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> phaseService.delete(dDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(phaseRepository).findById(dDTO.getId());
        Mockito.verifyNoMoreInteractions(phaseRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(boardService);
    }

}