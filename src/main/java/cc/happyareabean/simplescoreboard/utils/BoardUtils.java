package cc.happyareabean.simplescoreboard.utils;

import cc.happyareabean.simplescoreboard.SimpleScoreBoardConfig;

public class BoardUtils {

    public static boolean isLinesScoreSameSize() {
        if (!SimpleScoreBoardConfig.NUMBER_FORMATTING_ENABLE.toBoolean())
            return true;

        return SimpleScoreBoardConfig.LINES.toStringList().size() ==
                SimpleScoreBoardConfig.NUMBER_FORMATTING_LINES.toStringList().size();
    }

}
