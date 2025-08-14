package ec.com.todo.apptasks.phase.mapper;

import ec.com.todo.apptasks.phase.dto.request.CreatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.request.UpdatePhaseDTO;
import ec.com.todo.apptasks.phase.dto.response.PhaseDTO;
import ec.com.todo.apptasks.phase.entity.Phase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PhaseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "board", ignore = true)
    Phase toEntity(CreatePhaseDTO phaseDTO);

    @Mapping(target = "tasks",
            expression = "java(phase.getTasks() == null ? java.util.Collections.emptyList() : phase.getTasks().stream().map(t -> t.getId()).toList())")
    PhaseDTO toDTO(Phase phase);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "board", ignore = true)
    void updateEntity(@MappingTarget Phase phase, UpdatePhaseDTO dto);
}
