package org.betonquest.betonquest.id.function;

import org.betonquest.betonquest.api.QuestException;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.config.quest.QuestPackageManager;
import org.betonquest.betonquest.api.identifier.FunctionIdentifier;
import org.betonquest.betonquest.api.identifier.factory.DefaultIdentifierFactory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A factory for {@link FunctionIdentifier}s.
 */
public class FunctionIdentifierFactory extends DefaultIdentifierFactory<FunctionIdentifier> {

    /**
     * Creates a new identifier factory.
     *
     * @param packManager the quest package manager to resolve relative paths
     */
    public FunctionIdentifierFactory(final QuestPackageManager packManager) {
        super(packManager, "Function");
    }

    @Override
    public FunctionIdentifier parseIdentifier(@Nullable final QuestPackage source, final String input) throws QuestException {
        final Map.Entry<QuestPackage, String> entry = parse(source, input);
        final DefaultFunctionIdentifier identifier = new DefaultFunctionIdentifier(entry.getKey(), entry.getValue());
        return requireInstruction(identifier, DefaultFunctionIdentifier.FUNCTION_SECTION);
    }
}
