package ec.com.todo.apptasks.phase.service.impl;

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
        Phase phase = mapper.toEntity(pDTO);
        phase.setBoard(boardService.getReferenceById(pDTO.getBoardId()));
        phaseRepository.save(mapper.toEntity(pDTO));
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


}
