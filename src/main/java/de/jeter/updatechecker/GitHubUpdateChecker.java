package de.jeter.updatechecker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GitHubUpdateChecker extends UpdateChecker {

    private final JavaPlugin plugin;
    private URL url;
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
        try {
            this.url = new URL("https://github.com/" + repoOwner + "/" + repository + "/releases/latest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.url = null;
        }
        this.USER_AGENT = plugin.getName() + " UpdateChecker";
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
        return null;
    }

    @Override
    protected void checkForUpdate() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.addRequestProperty("Accept", "application/vnd.github+json");
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = JsonParser.parseReader(reader);
            JsonObject object = element.getAsJsonObject();
            System.out.println(object.toString());
        } catch (Exception e) {
        }
    }
}
