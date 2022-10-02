package de.jeter.updatechecker;

import org.bukkit.plugin.java.JavaPlugin;

public class GitHubUpdateChecker extends UpdateChecker {

    private final JavaPlugin plugin;
    private final String repository;
    private Result result = Result.NO_UPDATE;
    private String version;
    private final String USER_AGENT;

    /**
     * Initiates and runs a new UpdateChecker gaining GitHub releases
     * @param plugin the plugin
     * @param repoOwner the owner of the repository. For example "TheJeterLP"
     * @param repository the repository. For example "Spigot-Updatechecker"
     */
    public GitHubUpdateChecker(JavaPlugin plugin, String repoOwner, String repository) {
        this.plugin = plugin;
        this.repository = repoOwner + "/" + repository;
        this.USER_AGENT = plugin.getName() + " UpdateChecker";
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public String getLatestRemoteVersion() {
        return version;
    }

    @Override
    protected void checkForUpdate() {

    }
}
