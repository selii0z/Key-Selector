package io.github.selii0z.keyselector.client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.*;
import java.util.*;
import org.slf4j.LoggerFactory;

public final class Preferences {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FMLPaths.CONFIGDIR.get().resolve("keyselector.json");
    public int holdMillis = 300;
    public Map<String, Rule> keys = new HashMap<>();
    public static final class Rule {
        public String priority = "";
        public List<String> hidden = new ArrayList<>();
        public List<String> order = new ArrayList<>();
    }
    public static final Preferences INSTANCE = load();
    private static Preferences load() {
        try {
            if (Files.exists(FILE)) {
                Preferences p = GSON.fromJson(Files.readString(FILE), Preferences.class);
                if (p != null && p.keys != null) return p;
            }
        } catch (Exception e) { LoggerFactory.getLogger("KeySelector").warn("Cannot load preferences", e); }
        return new Preferences();
    }
    public Rule rule(String key) { return keys.computeIfAbsent(key, k -> new Rule()); }
    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Path tmp = FILE.resolveSibling("keyselector.json.tmp");
            Files.writeString(tmp, GSON.toJson(this));
            Files.move(tmp, FILE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) { LoggerFactory.getLogger("KeySelector").warn("Cannot save preferences", e); }
    }
}
