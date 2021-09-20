package lexa.helpers;

import lexa.Launcher;
import net.dv8tion.jda.api.entities.TextChannel;

public class CanalHelper {

    private CanalHelper() {

    }

    public static TextChannel getCanalPorNombre(String nombreDelCanal) {
        return Launcher.jda.getTextChannelsByName(nombreDelCanal, true).get(0);
    }
}
