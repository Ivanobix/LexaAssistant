package lexa.listeners;

import lexa.helpers.ImputacionesHelper;
import lexa.helpers.TareasHelper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lexa.utils.Globals.*;

public class GuildMessageListener extends ListenerAdapter {

    public static void avisarComandoDesconocido(@NotNull GuildMessageReceivedEvent event) {
        event.getMessage().getChannel().sendMessage("Comando desconocido.").complete();
        List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
        event.getChannel().deleteMessages(messages).completeAfter(1, TimeUnit.SECONDS);
    }

    public static void avisarDatosIncorrectos(@NotNull GuildMessageReceivedEvent event) {
        event.getMessage().getChannel().sendMessage("Los datos introducidos no son válidos.").complete();
        List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
        event.getChannel().deleteMessages(messages).completeAfter(1, TimeUnit.SECONDS);
    }

    public static void eliminarUltimoComando(@NotNull GuildMessageReceivedEvent event) {
        List<Message> messages = event.getChannel().getHistory().retrievePast(5).complete();
        Message mensajeAEliminar = null;
        for (Message mensajeActual : messages) {
            if (mensajeActual.getContentRaw().toLowerCase().startsWith("!")) {
                mensajeAEliminar = mensajeActual;
                break;
            }
        }
        if (mensajeAEliminar != null) {
            event.getChannel().deleteMessageById(mensajeAEliminar.getIdLong()).complete();
        }
    }

    public static void eliminarTodosLosMensajes(@NotNull GuildMessageReceivedEvent event) {
        List<Message> messages = event.getChannel().getHistory().retrievePast(100).complete();
        event.getChannel().deleteMessages(messages).complete();
    }

    public static List<Message> getUltimosMensajes(@NotNull GuildMessageReceivedEvent event, boolean ignorarComandos) {
        List<Message> mensajes = event.getChannel().getHistory().retrievePast(100).complete();
        List<Message> mensajesADevolver = new ArrayList<>();
        if (ignorarComandos) {
            for (Message mensajeActual : mensajes) {
                if (!mensajeActual.getContentRaw().startsWith("!"))
                    mensajesADevolver.add(mensajeActual);
            }
        } else {
            mensajesADevolver = mensajes;
        }
        return mensajesADevolver;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;

        String[] args = event.getMessage().getContentRaw().split("  ");
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(PREFIX + "clear")) {
                eliminarTodosLosMensajes(event);
            } else {
                String canal = event.getMessage().getChannel().getName();
                switch (canal) {
                    case CANAL_TAREAS:
                        TareasHelper.comprobarComandosCanalTareas(event, args);
                        break;
                    case CANAL_HISTORICO_TAREAS:
                        TareasHelper.comprobarComandosCanalHistoricoTareas(event, args);
                        break;
                    case CANAL_IMPUTACIONES:
                        ImputacionesHelper.comprobarComandosCanalImputaciones(event, args);
                        break;
                    case CANAL_HISTORICO_IMPUTACIONES:
                        ImputacionesHelper.comprobarComandosCanalHistoricoImputaciones(event, args);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
