package org.betonquest.betonquest.kernel.component;

import org.betonquest.betonquest.api.config.quest.QuestPackageManager;
import org.betonquest.betonquest.api.dependency.DependencyProvider;
import org.betonquest.betonquest.api.identifier.FunctionIdentifier;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.service.function.Functions;
import org.betonquest.betonquest.api.service.identifier.Identifiers;
import org.betonquest.betonquest.id.function.FunctionIdentifierFactory;
import org.betonquest.betonquest.kernel.ProcessorDataLoader;
import org.betonquest.betonquest.kernel.processor.feature.FunctionProcessor;
import org.betonquest.betonquest.lib.dependency.component.AbstractCoreComponent;

import java.util.Set;

/**
 * The implementation of {@link AbstractCoreComponent} for {@link Functions}.
 */
public class FunctionsComponent extends AbstractCoreComponent {

    /**
     * Create a new FunctionsComponent.
     */
    public FunctionsComponent() {
        super();
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(QuestPackageManager.class, BetonQuestLoggerFactory.class,
                Identifiers.class, ProcessorDataLoader.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(FunctionIdentifierFactory.class, FunctionProcessor.class);
    }

    @Override
    protected void load(final DependencyProvider dependencyProvider) {
        final QuestPackageManager questPackageManager = getDependency(QuestPackageManager.class);
        final BetonQuestLoggerFactory loggerFactory = getDependency(BetonQuestLoggerFactory.class);
        final ProcessorDataLoader processorDataLoader = getDependency(ProcessorDataLoader.class);
        final Identifiers identifiers = getDependency(Identifiers.class);

        final FunctionIdentifierFactory identifierFactory = new FunctionIdentifierFactory(questPackageManager);
        identifiers.register(FunctionIdentifier.class, identifierFactory);
        final FunctionProcessor functionProcessor = new FunctionProcessor(loggerFactory.create(FunctionProcessor.class), identifierFactory);
        processorDataLoader.addProcessor(functionProcessor);

        dependencyProvider.take(FunctionIdentifierFactory.class, identifierFactory);
        dependencyProvider.take(FunctionProcessor.class, functionProcessor);
    }
}
