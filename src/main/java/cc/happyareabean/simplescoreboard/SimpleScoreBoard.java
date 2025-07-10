/*
 * Copyright (c) 2025 HappyAreaBean
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cc.happyareabean.simplescoreboard;

import cc.happyareabean.simplescoreboard.commands.ReloadCommand;
import cc.happyareabean.simplescoreboard.listener.PlayerListener;
import cc.happyareabean.simplescoreboard.manager.ScoreboardManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

import java.io.File;
import java.io.IOException;

@Getter
public final class SimpleScoreBoard extends JavaPlugin {

    public static MiniMessage MM = MiniMessage.miniMessage();

    public static SimpleScoreBoard INSTANCE;
    private ScoreboardManager scoreboardManager;
    private YamlDocument scoreboardConfig;
    private Lamp<BukkitCommandActor> lamp;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        try {
            scoreboardConfig = YamlDocument.create(new File(getDataFolder(), "scoreboard.yml"),
                    getResource("scoreboard.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            getSLF4JLogger().error("Could not load scoreboard.yml", e);
        }

        getSLF4JLogger().info("Initializing Scoreboard...");
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();

        if(isPAPI()) getSLF4JLogger().info("PlaceholderAPI found!");

        getSLF4JLogger().info("Registering PlayerListener...");
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getSLF4JLogger().info("Initializing commands...");
        lamp = BukkitLamp.builder(this).build();
        lamp.register(new ReloadCommand());

        getSLF4JLogger().info("SimpleScoreboard has been enabled! :)");
        getSLF4JLogger().info("by HappyAreaBean");

        new Metrics(this, 26449);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        scoreboardManager.getUpdateTask().cancel();
    }

    public boolean isPAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}
