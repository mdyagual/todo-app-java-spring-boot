package ec.com.todo.apptasks.phase.controller;

import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.service.PhaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {
    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping("/all")
    public List<PhaseDTO> getAllPhases() {
        return phaseService.getAll();
    }

    @PostMapping("/create")
    public void createPhase(@Valid @RequestBody CreatePhaseDTO phaseDTO) {
        phaseService.save(phaseDTO);
    }

    @PostMapping("/update")
    public void updatePhase(@Valid @RequestBody UpdatePhaseDTO phaseDTO) {
        phaseService.update(phaseDTO);
    }

    @PostMapping("/delete")
    public void deletePhase(@Valid @RequestBody DeletePhaseDTO phaseDTO) {
        phaseService.delete(phaseDTO);
    }
}
