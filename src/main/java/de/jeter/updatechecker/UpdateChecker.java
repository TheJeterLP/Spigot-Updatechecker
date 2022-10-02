package de.jeter.updatechecker;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class UpdateChecker {

    /**
     * Get the result of the update.
     *
     * @return result of the update.
     * @see Result
     */
    public abstract Result getResult();

    /**
     * Get the latest version from remote.
     *
     * @return latest version.
     */
    public abstract String getLatestRemoteVersion();

    /**
     * Gets the message to print out when a new update is available
     *
     * @return message
     */
    public abstract String getUpdateMessage();

    /**
     * Runs the actual checker itself.
     */
    protected abstract void checkForUpdate();

    /**
     * Checks if plugin should be updated
     *
     * @param newVersion remote version
     * @param oldVersion current version
     */
    protected boolean shouldUpdate(String newVersion, String oldVersion) {
        try {
            float oldV = Float.parseFloat(oldVersion.replaceAll("\\.", "").replaceAll("v", ".").replaceAll("-SNAPSHOT", ""));
            float newV = Float.parseFloat(newVersion.replaceAll("\\.", "").replaceAll("v", ".").replaceAll("-SNAPSHOT", ""));
            return oldV < newV;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return !newVersion.equalsIgnoreCase(oldVersion);
        }
    }

    /**
     * Initializes the updatechecker
     *
     * @param plugin
     */
    protected void init(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::checkForUpdate);
    }

}
