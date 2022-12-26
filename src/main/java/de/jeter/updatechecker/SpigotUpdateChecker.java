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

public class SpigotUpdateChecker extends UpdateChecker {

    private static final String VERSIONS = "/versions/latest";
    private static final String API_RESOURCE = "https://api.spiget.org/v2/resources/";
    private final JavaPlugin plugin;
    private final int id;
    private final String USER_AGENT;
    private final String url;
    private Result result = Result.NO_UPDATE;
    private String version;

    public SpigotUpdateChecker(JavaPlugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.USER_AGENT = plugin.getName() + " UpdateChecker";
        this.url = "https://www.spigotmc.org/resources/" + id + "/updates";
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
        return "Update found! Please consider installing the latest version from " + url;
    }

    @Override
    public URL getDownloadLink() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
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

            JsonElement element = null;

            try {
                element = JsonParser.parseReader(reader);
            } catch (NoSuchMethodError e) {
                element = new JsonParser().parse(reader);
            }

            JsonObject object = element.getAsJsonObject();
            element = object.get("name");
            version = element.toString().replaceAll("\"", "");

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
