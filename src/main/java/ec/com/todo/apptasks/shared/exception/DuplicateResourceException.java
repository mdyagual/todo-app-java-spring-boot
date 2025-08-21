package ec.com.todo.apptasks.shared.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DuplicateResourceException extends RuntimeException {
    private final String resourceName;
    private final List<String> fields;


    public DuplicateResourceException(String resourceName, List<String> fields) {
        super(String.format("%s with one of this field value(s): [%s] already exists", resourceName, String.join(", ",fields)));
        this.resourceName = resourceName;
        this.fields = new ArrayList<>(fields);
    }




}
