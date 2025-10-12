package ec.com.todo.apptasks.phase.controller;

import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.DeletePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.service.PhaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PhaseDTO>> getAllPhases() {
        return ResponseEntity.ok(phaseService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<PhaseDTO> createPhase(@Valid @RequestBody CreatePhaseDTO phaseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phaseService.save(phaseDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<PhaseDTO> updatePhase(@Valid @RequestBody UpdatePhaseDTO phaseDTO) {
        return ResponseEntity.ok(phaseService.update(phaseDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePhase(@Valid @RequestBody DeletePhaseDTO phaseDTO) {
        phaseService.delete(phaseDTO);
        return ResponseEntity.accepted().build();
    }
}
