package lexa.helpers;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CanalHelper {

    private CanalHelper() {

    }

    public static TextChannel getCanalPorNombre(@NotNull GuildMessageReceivedEvent event, String nombreDelCanal) {
        return event.getJDA().getTextChannelsByName(nombreDelCanal, true).get(0);
    }
}
