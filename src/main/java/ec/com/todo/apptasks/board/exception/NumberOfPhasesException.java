package ec.com.todo.apptasks.board.exception;

import lombok.Getter;

@Getter
public class NumberOfPhasesException extends RuntimeException{
    private final Integer numPhases;
    private final static Integer MAX_PHASES = 5;
    public NumberOfPhasesException(Integer numPhases) {
        super(String.format("A board can only have %d phases - Current phases: %d", MAX_PHASES, numPhases));
        this.numPhases = numPhases;
    }


}
