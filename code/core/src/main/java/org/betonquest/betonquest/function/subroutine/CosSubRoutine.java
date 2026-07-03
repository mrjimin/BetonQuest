package org.betonquest.betonquest.function.subroutine;

import org.betonquest.betonquest.api.function.FunctionAssignment;
import org.betonquest.betonquest.api.function.FunctionProvider;
import org.betonquest.betonquest.lib.function.assignment.NumberSourceAssignment;

import java.util.Map;

/**
 * Represents the cos function.
 */
public class CosSubRoutine extends AbstractSubRoutine {

    /**
     * Creates a new CosSubRoutine.
     */
    public CosSubRoutine() {
        super(false, param("x"));
    }

    @Override
    public FunctionAssignment evaluateSubroutine(final FunctionProvider functionProvider, final Map<String, FunctionAssignment> arguments) {
        final Number number = arguments.get("x").asNumber();
        return new NumberSourceAssignment(Math.cos(number.doubleValue()));
    }
}
