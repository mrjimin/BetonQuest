package org.betonquest.betonquest.function.subroutine;

import org.betonquest.betonquest.api.function.FunctionAssignment;
import org.betonquest.betonquest.api.function.FunctionProvider;
import org.betonquest.betonquest.lib.function.assignment.NumberSourceAssignment;

import java.util.Map;

/**
 * Represents the log function.
 */
public class LogSubRoutine extends AbstractSubRoutine {

    /**
     * Creates a new LogSubRoutine.
     */
    public LogSubRoutine() {
        super(false, param("basis"), param("value"));
    }

    @Override
    public FunctionAssignment evaluateSubroutine(final FunctionProvider functionProvider, final Map<String, FunctionAssignment> arguments) {
        final double basis = arguments.get("basis").asNumber().doubleValue();
        final double value = arguments.get("value").asNumber().doubleValue();
        final double result = Math.log(value) / Math.log(basis);
        return new NumberSourceAssignment(result);
    }
}
