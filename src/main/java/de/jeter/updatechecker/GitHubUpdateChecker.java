package de.jeter.updatechecker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUpdateChecker extends UpdateChecker {

    private final JavaPlugin plugin;
    private String url;
    private Result result = Result.NO_UPDATE;
    private String version;

    /**
     * Initiates and runs a new UpdateChecker gaining GitHub releases
     *
     * @param plugin     the plugin
     * @param repoOwner  the owner of the repository. For example "TheJeterLP"
     * @param repository the repository. For example "Spigot-Updatechecker"
     */
    public GitHubUpdateChecker(JavaPlugin plugin, String repoOwner, String repository) {
        this.plugin = plugin;
        this.url = "https://github.com/" + repoOwner + "/" + repository + "/releases/latest";
        super.init(plugin);
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
    public String getUpdateMessage() {
        return "Update found! Please consider downloading the newest version from " + url;
    }

    @Override
    protected void checkForUpdate() {
        try {
            URL githubURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) githubURL.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Accept", "application/json");
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject object = element.getAsJsonObject();
            element = object.get("tag_name");
            version = element.toString().replaceAll("\"", "").replaceFirst("v", "");

            plugin.getLogger().info("Version installed is " + plugin.getDescription().getVersion());
            plugin.getLogger().info("Latest version found online is " + version);

            if (shouldUpdate(version, plugin.getDescription().getVersion())) {
                result = Result.UPDATE_FOUND;
                plugin.getLogger().info(getUpdateMessage());
            } else {
                plugin.getLogger().info("No update found.");
                result = Result.NO_UPDATE;
            }
        } catch (Exception e) {
            result = Result.FAILED;
            e.printStackTrace();
        }
    }
}
