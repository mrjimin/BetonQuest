package org.betonquest.betonquest.feature;

import org.betonquest.betonquest.api.config.ConfigAccessor;
import org.betonquest.betonquest.api.config.Localizations;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.service.compass.CompassManager;
import org.betonquest.betonquest.api.service.identifier.Identifiers;
import org.betonquest.betonquest.api.service.item.ItemManager;
import org.betonquest.betonquest.data.PlayerDataStorage;
import org.betonquest.betonquest.kernel.processor.feature.CancelerProcessor;
import org.bukkit.plugin.Plugin;

/**
 * Factory to create Backpack objects for profiles.
 */
public class BackpackFactory {

    /**
     * The logger factory.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    /**
     * The plugin configuration file.
     */
    private final ConfigAccessor config;

    /**
     * The player data storage.
     */
    private final PlayerDataStorage playerDataStorage;

    /**
     * The canceler processor.
     */
    private final CancelerProcessor cancelerProcessor;

    /**
     * The compass manager.
     */
    private final CompassManager compassManager;

    /**
     * The item manager.
     */
    private final ItemManager itemManager;

    /**
     * The identifier registry.
     */
    private final Identifiers identifiers;

    /**
     * The {@link Localizations} instance.
     */
    private final Localizations localizations;

    /**
     * Creates a {@link Backpack} factory.
     *
     * @param plugin            the plugin instance
     * @param loggerFactory     the logger factory
     * @param config            the plugin configuration file
     * @param playerDataStorage the player data storage
     * @param cancelerProcessor the canceler processor
     * @param compassManager    the compass manager
     * @param itemManager       the item manager
     * @param identifiers       the identifier factory
     * @param localizations     the {@link Localizations} instance
     */
    public BackpackFactory(final Plugin plugin, final BetonQuestLoggerFactory loggerFactory, final ConfigAccessor config,
                           final PlayerDataStorage playerDataStorage, final CancelerProcessor cancelerProcessor, final CompassManager compassManager,
                           final ItemManager itemManager, final Identifiers identifiers, final Localizations localizations) {
        this.plugin = plugin;
        this.loggerFactory = loggerFactory;
        this.config = config;
        this.playerDataStorage = playerDataStorage;
        this.cancelerProcessor = cancelerProcessor;
        this.compassManager = compassManager;
        this.itemManager = itemManager;
        this.identifiers = identifiers;
        this.localizations = localizations;
    }

    /**
     * Create a new Backpack with {@link Backpack.DisplayType#DEFAULT}.
     *
     * @param onlineProfile the profile to create the backpack for
     * @return the newly created backpack
     */
    public Backpack createBackpack(final OnlineProfile onlineProfile) {
        return createBackpack(onlineProfile, Backpack.DisplayType.DEFAULT);
    }

    /**
     * Create a new Backpack.
     *
     * @param onlineProfile the profile to create the backpack for
     * @param displayType   the display mode
     * @return the newly created backpack
     */
    public Backpack createBackpack(final OnlineProfile onlineProfile, final Backpack.DisplayType displayType) {
        return new Backpack(plugin, loggerFactory.create(Backpack.class), config, cancelerProcessor, compassManager, itemManager,
                identifiers, localizations, onlineProfile, playerDataStorage.get(onlineProfile), displayType);
    }
}
