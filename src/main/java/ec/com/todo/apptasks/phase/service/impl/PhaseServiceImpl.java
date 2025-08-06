package ec.com.todo.apptasks.phase.service.impl;

import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.exception.PhaseNotFoundException;
import ec.com.todo.apptasks.phase.mapper.PhaseMapper;
import ec.com.todo.apptasks.phase.mapper.PhaseMapperImpl;
import ec.com.todo.apptasks.phase.repository.PhaseRepository;
import ec.com.todo.apptasks.phase.service.PhaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService {
    private final PhaseRepository phaseRepository;
    private final PhaseMapper mapper;

    public PhaseServiceImpl(PhaseRepository phaseRepository){
        this.phaseRepository = phaseRepository;
        this.mapper = new PhaseMapperImpl();
    }
    @Override
    public void save(CreatePhaseDTO pDTO) {
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
                            throw new PhaseNotFoundException("Phase not found with id: " + pDTO.getId());
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
                            throw new PhaseNotFoundException("Phase not found with id: " + pDTO.getId());
                        }
                );
    }


}
