package org.betonquest.betonquest.function.subroutine;

import org.betonquest.betonquest.api.function.FunctionAssignment;
import org.betonquest.betonquest.api.function.FunctionProvider;
import org.betonquest.betonquest.lib.function.assignment.NumberSourceAssignment;

import java.util.Map;

/**
 * Represents the floor function.
 */
public class FloorSubRoutine extends AbstractSubRoutine {

    /**
     * Creates a new FloorSubRoutine.
     */
    public FloorSubRoutine() {
        super(false, param("x"));
    }

    @Override
    public FunctionAssignment evaluateSubroutine(final FunctionProvider functionProvider, final Map<String, FunctionAssignment> arguments) {
        final Number number = arguments.get("x").asNumber();
        final double floor = Math.floor(number.doubleValue());
        return new NumberSourceAssignment(Math.round(floor));
    }
}
