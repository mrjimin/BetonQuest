package org.betonquest.betonquest.function.subroutine;

import org.betonquest.betonquest.api.function.FunctionAssignment;
import org.betonquest.betonquest.api.function.FunctionProvider;
import org.betonquest.betonquest.lib.function.assignment.NumberSourceAssignment;

import java.util.Map;

/**
 * Represents the sum function.
 */
public class SumSubRoutine extends AbstractSubRoutine {

    /**
     * Creates a new SumSubRoutine.
     */
    public SumSubRoutine() {
        super(true, param("a", new NumberSourceAssignment(0)));
    }

    @Override
    public FunctionAssignment evaluateSubroutine(final FunctionProvider functionProvider, final Map<String, FunctionAssignment> arguments) {
        final double summation = arguments.values().stream().map(FunctionAssignment::asNumber).mapToDouble(Number::doubleValue).sum();
        if (arguments.values().stream().noneMatch(a -> a.asNumber() instanceof Double)) {
            return new NumberSourceAssignment(Math.round(summation));
        }
        return new NumberSourceAssignment(summation);
    }
}
