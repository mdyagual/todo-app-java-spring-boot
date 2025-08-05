package ec.com.todo.apptasks.phase.exception;

public class PhaseNotFoundException extends RuntimeException {
    public PhaseNotFoundException(String msg) {
        super(msg);
    }
}
