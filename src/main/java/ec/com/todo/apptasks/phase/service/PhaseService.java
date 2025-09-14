package ec.com.todo.apptasks.phase.service;

import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.entity.Phase;

import java.util.List;

public interface PhaseService {
    PhaseDTO save(CreatePhaseDTO pDTO);
    List<PhaseDTO> getAll();
    PhaseDTO update(UpdatePhaseDTO pDTO);
    void delete(DeletePhaseDTO pDTO);
    Phase getReferenceById(Long id);
    Phase getPhaseOrThrow(Long id);

}
