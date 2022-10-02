package de.jeter.updatechecker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpigotUpdateChecker extends UpdateChecker {

    private final JavaPlugin plugin;
    private final int id;
    private Result result = Result.NO_UPDATE;
    private String version;
    private static final String VERSIONS = "/versions/latest";
    private static final String API_RESOURCE = "https://api.spiget.org/v2/resources/";
    private final String USER_AGENT;

    public SpigotUpdateChecker(JavaPlugin plugin, int id) {
        super.init(plugin);
        this.plugin = plugin;
        this.id = id;
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
        try {
            plugin.getLogger().info("Checking for update...");
            URL url = new URL(API_RESOURCE + id + VERSIONS);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = JsonParser.parseReader(reader);
            plugin.getLogger().info(element.toString());
            JsonObject object = element.getAsJsonObject();
            element = object.get("name");
            version = element.toString().replaceAll("\"", "");

            plugin.getLogger().info("Version installed is " + plugin.getDescription().getVersion());
            plugin.getLogger().info("Latest version found online is " + version);

            if (shouldUpdate(version, plugin.getDescription().getVersion())) {
                result = Result.UPDATE_FOUND;
                plugin.getLogger().info("Update found! Please consider installing the latest version from SpigotMC!");
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
