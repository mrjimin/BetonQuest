package org.betonquest.betonquest.id.function;

import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.identifier.DefaultReadableIdentifier;
import org.betonquest.betonquest.api.identifier.FunctionIdentifier;

/**
 * The default implementation for {@link FunctionIdentifier}s.
 */
public class DefaultFunctionIdentifier extends DefaultReadableIdentifier implements FunctionIdentifier {

    /**
     * The section in the configuration where functions are defined.
     */
    public static final String FUNCTION_SECTION = "functions";

    /**
     * Creates a new identifier.
     *
     * @param pack       the package this identifier belongs to
     * @param identifier the identifier without the package name
     */
    protected DefaultFunctionIdentifier(final QuestPackage pack, final String identifier) {
        super(pack, identifier, FUNCTION_SECTION);
    }
}
