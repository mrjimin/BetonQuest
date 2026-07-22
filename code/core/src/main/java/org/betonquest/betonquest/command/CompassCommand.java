package org.betonquest.betonquest.command;

import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.profile.ProfileProvider;
import org.betonquest.betonquest.feature.Backpack.DisplayType;
import org.betonquest.betonquest.feature.BackpackFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The /compass command. It opens the list of quests.
 */
@SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
public class CompassCommand implements CommandExecutor {

    /**
     * The profile provider instance.
     */
    private final ProfileProvider profileProvider;

    /**
     * Factory to create backpacks.
     */
    private final BackpackFactory backpackFactory;

    /**
     * Creates a new executor for the /compass command.
     *
     * @param profileProvider the profile provider instance
     * @param backpackFactory the factory to create backpacks
     */
    public CompassCommand(final ProfileProvider profileProvider, final BackpackFactory backpackFactory) {
        this.profileProvider = profileProvider;
        this.backpackFactory = backpackFactory;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if ("compass".equalsIgnoreCase(cmd.getName())) {
            if (sender instanceof Player) {
                final OnlineProfile onlineProfile = profileProvider.getProfile((Player) sender);
                backpackFactory.createBackpack(onlineProfile, DisplayType.COMPASS);
            }
            return true;
        }
        return false;
    }
}
