package ec.com.todo.apptasks.phase.service;

import ec.com.todo.apptasks.board.dto.request.UpdateBoardDTO;
import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;

import java.util.List;

public interface PhaseService {
    void save(CreatePhaseDTO pDTO);
    List<PhaseDTO> getAll();
    void update(UpdatePhaseDTO pDTO);
    void delete(DeletePhaseDTO pDTO);
}
