package org.betonquest.betonquest.api.function;

import org.betonquest.betonquest.api.QuestException;

import java.util.List;
import java.util.Map;

/**
 * Represents a function definition that can assign values to variables.
 *
 * @since 3.1.0
 */
@FunctionalInterface
public interface FunctionDefinition {

    /**
     * Assigns the given values to variables of this function.
     * Also completes the assignment with the default values if necessary.
     *
     * @param assignments the assignments to map
     * @return the assigned values
     * @throws QuestException if the assignment fails due to insufficient arguments
     * @since 3.1.0
     */
    Map<String, FunctionAssignment> assign(List<FunctionAssignment> assignments) throws QuestException;
}
