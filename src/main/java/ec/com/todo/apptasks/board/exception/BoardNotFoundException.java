package ec.com.todo.apptasks.board.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(String msg) {
        super(msg);
    }
}
