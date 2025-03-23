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

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.List;

import static cc.happyareabean.simplescoreboard.SimpleScoreBoard.MM;

@Getter
@RequiredArgsConstructor
public enum SimpleScoreBoardConfig {

    TITLE("title", "<gradient:#20BDFF:#A5FECB><b>SimpleScoreboard</b></gradient>"),
    LINES("lines", List.of(
            "",
            "<b><color:#eaff03>Profile",
            " <white>● <gray>Name: <white>%player_name%",
            " <white>● <gray>Rank: <white>%vault_rank%",
            " <white>● <gray>Ping: <white>%player_ping%",
            "",
            "<b><color:#eaff03>Server",
            " <white>● <gray>Lobby: <white>#1",
            " <white>● <gray>Players: <white>%bungee_total%",
            "",
            "<gradient:#20BDFF:#A5FECB>ʜᴀᴘᴘʏᴀʀᴇᴀʙᴇᴀɴ.ᴄᴄ"
    )),

    UPDATE_INTERVAL("update-interval", 20),
    ;

    private final String path;
    private final Object defaultValue;

    private YamlDocument getConfig() {
        return SimpleScoreBoard.INSTANCE.getScoreboardConfig();
    }

    public String toString() {
        return getConfig().getOptionalString(path).orElse(path);
    }

    public Component component() {
        return getConfig().getOptionalString(path)
                .map(MM::deserialize)
                .orElse(Component.text(path));
    }

    public Component componentPAPI(Player player) {
        return getConfig().getOptionalString(path)
                .map(s -> SimpleScoreBoard.INSTANCE.isPAPI() ? PlaceholderAPI.setPlaceholders(player, s) : s)
                .map(MM::deserialize)
                .orElse(Component.text(path));
    }

    public Component component(TagResolver... resolvers) {
        return getConfig().getOptionalString(path)
                .map(s -> MM.deserialize(s, resolvers))
                .orElse(Component.text(path));
    }

    public List<Component> componentList() {
        return getConfig().getOptionalStringList(path)
                .map(list -> list.stream().map(MM::deserialize).toList())
                .orElse(List.of(Component.text(path)));
    }

    public List<Component> componentListPAPI(Player player) {
        return getConfig().getOptionalStringList(path)
                .map(list -> list.stream()
                        .map(s -> SimpleScoreBoard.INSTANCE.isPAPI() ? PlaceholderAPI.setPlaceholders(player, s) : s)
                        .map(MM::deserialize).toList())
                .orElse(List.of(Component.text(path)));
    }

    public List<Component> componentList(TagResolver... resolvers) {
        return getConfig().getOptionalStringList(path)
                .map(list -> list.stream().map(s -> MM.deserialize(s, resolvers)).toList())
                .orElse(List.of(Component.text(path)));
    }

    public List<String> toStringList() {
        return getConfig().getOptionalStringList(path).orElse(List.of(path));
    }

    public boolean toBoolean() {
        return Boolean.parseBoolean(toString());
    }

    public int toInteger() {
        return Integer.parseInt(toString());
    }

    public double toDouble() {
        return Double.parseDouble(toString());
    }

}
