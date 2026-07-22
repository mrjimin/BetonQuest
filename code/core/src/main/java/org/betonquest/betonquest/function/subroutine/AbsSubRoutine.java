package org.betonquest.betonquest.function.subroutine;

import org.betonquest.betonquest.api.function.FunctionAssignment;
import org.betonquest.betonquest.api.function.FunctionProvider;
import org.betonquest.betonquest.lib.function.assignment.NumberSourceAssignment;

import java.util.Map;

/**
 * Represents the abs function.
 */
public class AbsSubRoutine extends AbstractSubRoutine {

    /**
     * Creates a new AbsSubRoutine.
     */
    public AbsSubRoutine() {
        super(false, param("x"));
    }

    @Override
    public FunctionAssignment evaluateSubroutine(final FunctionProvider functionProvider, final Map<String, FunctionAssignment> arguments) {
        final Number number = arguments.get("x").asNumber();
        if (number instanceof final Long longValue) {
            return new NumberSourceAssignment(Math.abs(longValue));
        }
        return new NumberSourceAssignment(Math.abs(number.doubleValue()));
    }
}
