package ec.com.todo.apptasks.phase.service.impl;

import ec.com.todo.apptasks.board.exception.NumberOfPhasesException;
import ec.com.todo.apptasks.board.service.BoardService;
import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.entity.Phase;
import ec.com.todo.apptasks.phase.mapper.PhaseMapper;
import ec.com.todo.apptasks.phase.mapper.PhaseMapperImpl;
import ec.com.todo.apptasks.phase.repository.PhaseRepository;
import ec.com.todo.apptasks.phase.service.PhaseService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService {
    private final PhaseRepository phaseRepository;
    private final BoardService boardService;
    private final EntityManager entityManager;
    private final PhaseMapper mapper;

    public PhaseServiceImpl(PhaseRepository phaseRepository, BoardService boardService, EntityManager entityManager) {
        this.phaseRepository = phaseRepository;
        this.boardService = boardService;
        this.entityManager = entityManager;
        this.mapper = new PhaseMapperImpl();
    }


    @Override
    public Phase getReferenceById(Long id) {
        return entityManager.getReference(Phase.class, id);
    }

    @Override
    public Phase getPhaseOrThrow(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase", id));
    }

    @Override
    public void save(CreatePhaseDTO pDTO) {
        if (getNumberOfPhases(pDTO.getBoardId()) == 4) {
            throw new NumberOfPhasesException(boardService.getReferenceById(pDTO.getBoardId()).getPhases().size());
        }

        if (phaseRepository.existsByNameAndBoardId(pDTO.getName().name(), pDTO.getBoardId())) {
            throw new DuplicateResourceException("Phase", List.of("name", "boardId"));
        }

        Phase phase = mapper.toEntity(pDTO);

        phase.setBoard(boardService.getReferenceById(pDTO.getBoardId()));
        phaseRepository.save(phase);
    }

    @Override
    public List<PhaseDTO> getAll() {
        return phaseRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void update(UpdatePhaseDTO pDTO) {
        phaseRepository.findById(pDTO.getId())
                .ifPresentOrElse(
                        phase -> {
                            mapper.updateEntity(phase, pDTO);
                            phase.setLastModifiedAt(LocalDate.now());
                            phaseRepository.save(phase);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Phase", pDTO.getId());
                        }
                );
    }

    @Override
    public void delete(DeletePhaseDTO pDTO) {

        phaseRepository.findById(pDTO.getId())
                .ifPresentOrElse(
                        phase -> {
                            phase.setIsActive(false);
                            phase.setLastModifiedAt(LocalDate.now());
                            phaseRepository.save(phase);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Phase", pDTO.getId());
                        }
                );
    }

    private Integer getNumberOfPhases(Long boardId) {
        return boardService.getReferenceById(boardId).getPhases().size();
    }

}
