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

package cc.happyareabean.simplescoreboard.commands;

import cc.happyareabean.simplescoreboard.SimpleScoreBoard;
import cc.happyareabean.simplescoreboard.SimpleScoreBoardConfig;
import cc.happyareabean.simplescoreboard.utils.BoardUtils;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.IOException;

import static cc.happyareabean.simplescoreboard.SimpleScoreBoard.MM;

@CommandPermission("simplescoreboard.admin")
@Command({"simplescoreboard", "ssb"})
public class ReloadCommand {

    @CommandPlaceholder
    public void defaultHandle(BukkitCommandActor actor) {
        actor.reply(MM.deserialize("<gradient:#20BDFF:#A5FECB><b>SimpleScoreBoard Help"));
        actor.reply("");
        actor.reply(" &f● &7/simplescoreboard reload [recreate] &8- &fReload config");
    }

    @Subcommand("reload")
    public void reload(BukkitCommandActor actor, @Optional Boolean recreate) {
        try {
            boolean success = SimpleScoreBoard.INSTANCE.getScoreboardConfig().reload();
            if (recreate != null) SimpleScoreBoard.INSTANCE.getScoreboardManager().recreate();

            if (success)
                actor.reply(MM.deserialize("<gradient:#20BDFF:#A5FECB>Successfully reloaded SimpleScoreBoard."));

            if (!BoardUtils.isLinesScoreSameSize()) {
                actor.reply(MM.deserialize("<gradient:#fc813f:#ef5f4f>⚠ <b>Warning:</b> You have custom number formatting enabled, but the lines and score do not have the same number of digits. " +
                        "The custom formatting will be ignored until this issues is fixed.</gradient>"));
                actor.reply(MM.deserialize("<color:#e8f742>⚠ Scoreboard Lines: <b><white>%s</white></b> <gray>|</gray> Score Lines: <b><white>%s</white></b>"
                        .formatted(SimpleScoreBoardConfig.LINES.toStringList().size(), SimpleScoreBoardConfig.NUMBER_FORMATTING_LINES.toStringList().size())));
                }
        } catch (IOException e) {
            actor.reply(MM.deserialize("<color:#e13b3b>Failed to reload SimpleScoreBoard! Check console for more information."));
            SimpleScoreBoard.INSTANCE.getSLF4JLogger().error("Failed to reload SimpleScoreBoard!", e);
        }
    }

}
