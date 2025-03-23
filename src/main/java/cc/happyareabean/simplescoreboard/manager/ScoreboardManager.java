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

package cc.happyareabean.simplescoreboard.manager;

import cc.happyareabean.simplescoreboard.SimpleScoreBoard;
import cc.happyareabean.simplescoreboard.SimpleScoreBoardConfig;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ScoreboardManager {

    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private final SimpleScoreBoard plugin;
    private BukkitTask updateTask;

    public void init() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {

                boards.forEach((uuid, board) -> {
                    var player = Bukkit.getPlayer(uuid);

                    if (player == null) {
                        removeScoreboard(uuid);
                        return;
                    }

                    board.updateTitle(SimpleScoreBoardConfig.TITLE.componentPAPI(player));
                    board.updateLines(SimpleScoreBoardConfig.LINES.componentListPAPI(player));
                });

            }
        }.runTaskTimerAsynchronously(plugin, 0, SimpleScoreBoardConfig.UPDATE_INTERVAL.toInteger());
    }

    public void cancel() {
        if (updateTask != null) updateTask.cancel();
    }

    public void recreate() {
        cancel();
        init();
    }

    public void createScoreboard(Player player) {
        if (boards.containsKey(player.getUniqueId())) return;
        boards.put(player.getUniqueId(), new FastBoard(player));
    }

    public void removeScoreboard(UUID uuid) {
        var board = getBoard(uuid);
        if (board != null) {
            boards.remove(uuid);
        }
    }

    public FastBoard getBoard(UUID uuid) {
        return boards.get(uuid);
    }

}
