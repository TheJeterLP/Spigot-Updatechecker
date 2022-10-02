import de.jeter.updatechecker.SpigotUpdateChecker;
import de.jeter.updatechecker.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        UpdateChecker checker = new SpigotUpdateChecker(this, 00000);
    }

}
