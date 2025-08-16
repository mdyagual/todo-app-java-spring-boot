package ec.com.todo.apptasks.phase.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@ToString
@Getter
public enum PhaseName {
    TO_DO("TO DO"),
    IN_PROGRESS("IN PROGRESS"),
    REVIEW("REVIEW"),
    BLOCKED("BLOCKED"),
    DONE("DONE");

    private final String name;

    PhaseName(String name) {
        this.name = name;
    }

    @JsonCreator
    public static PhaseName fromValue(String value) {
        return Arrays.stream(PhaseName.values())
                .filter(e -> e.getName().equalsIgnoreCase(value)) // match by display name
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid phase: " + value));
    }

    @JsonValue
    public String toValue() {
        return name; // this makes Jackson serialize using the "pretty name"
    }
}
