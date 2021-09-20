package lexa.helpers;

import lexa.plantillas.Tarea;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lexa.listeners.GuildMessageListener.*;
import static lexa.utils.Constantes.*;
import static org.jinq.orm.stream.JinqStream.from;

public class TareasHelper {

    //------------------ ADD TAREA ------------------//

    private TareasHelper() {

    }

    public static void addTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            addTareaRapida(event, args[1]);
        } else if (args.length == 3) {
            addTareaProyecto(event, args);
        } else if (args.length == 4) {
            addTareaCompleta(event, args);
        } else {
            avisarComandoDesconocido(event);
        }
    }

    private static void addTareaRapida(@NotNull GuildMessageReceivedEvent event, String arg) {
        Tarea tarea = new Tarea(arg); //Add descripcion
        event.getMessage().getChannel().sendMessage(tarea.toString()).complete();
        eliminarUltimoComando(event);
    }

    private static void addTareaProyecto(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Tarea tarea = new Tarea(args[1], args[2]); //Add proyecto descripcion
        event.getMessage().getChannel().sendMessage(tarea.toString()).complete();
        eliminarUltimoComando(event);
    }

    private static void addTareaCompleta(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Tarea tarea = new Tarea(args[1], args[2], args[3]); //Add link proyecto descripcion
        event.getMessage().getChannel().sendMessage(tarea.toString()).complete();
        eliminarUltimoComando(event);
    }

    //------------------ DELETE TAREA ------------------//

    public static void deleteTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) {
                deleteTodosLosMensajes(event);
            } else {
                deleteMensajeUnico(event, args);
            }
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void deleteTodosLosMensajes(@NotNull GuildMessageReceivedEvent event) {
        List<Message> mensajes = getUltimosMensajes(event, false);
        if (mensajes.size() >= 2)
            event.getChannel().deleteMessages(mensajes).complete();
    }


    private static void deleteMensajeUnico(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message mensajeAEliminar = buscarTareaPorID(event, args);

        if (mensajeAEliminar != null) {
            event.getChannel().deleteMessageById(mensajeAEliminar.getIdLong()).complete();
        } else {
            avisarTareaIndicadaNoExiste(event);
        }
    }

    //------------------ CLOSE TAREA ------------------//

    public static void closeTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            closeTareaUnica(event, args);
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void closeTareaUnica(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message tareaACerrar = buscarTareaPorID(event, args);

        if (tareaACerrar != null) {
            String contenidoTareaCerrada = tareaACerrar.getContentRaw() + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            event.getChannel().editMessageById(tareaACerrar.getIdLong(), contenidoTareaCerrada).complete();
        } else {
            avisarTareaIndicadaNoExiste(event);
        }
    }

    //------------------ SAVE TAREA ------------------//

    public static void saveTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) {
                saveTodasLasTareas(event);
            } else {
                saveTareaUnica(event, args);
            }
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void saveTodasLasTareas(@NotNull GuildMessageReceivedEvent event) {
        List<Message> mensajes = getUltimosMensajes(event, true);
        if (!mensajes.isEmpty()) {
            TextChannel canalTareasHistorico = CanalHelper.getCanalPorNombre(event, CANAL_HISTORICO_TAREAS);
            for (Message tareaActual : mensajes) {
                canalTareasHistorico.sendMessage(tareaActual).complete();
                event.getChannel().deleteMessages(mensajes).complete();
            }
        }
    }

    private static void saveTareaUnica(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message tareaAGuardar = buscarTareaPorID(event, args);

        if (tareaAGuardar != null) {
            TextChannel canalTareasHistorico = CanalHelper.getCanalPorNombre(event, CANAL_HISTORICO_TAREAS);
            canalTareasHistorico.sendMessage(tareaAGuardar).complete();
            event.getChannel().deleteMessageById(tareaAGuardar.getIdLong()).complete();
        } else {
            avisarTareaIndicadaNoExiste(event);
        }
    }

    //------------------ OPEN TAREA ------------------//

    public static void openTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            openTareaUnica(event, args);
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void openTareaUnica(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message tareaAReabrir = buscarTareaPorID(event, args);

        if (tareaAReabrir != null) {
            TextChannel canalTareas = CanalHelper.getCanalPorNombre(event, CANAL_TAREAS);
            canalTareas.sendMessage(tareaAReabrir).complete();
            event.getChannel().deleteMessageById(tareaAReabrir.getIdLong()).complete();
        } else {
            avisarTareaIndicadaNoExiste(event);
        }
    }

    //------------------ ORDER BY TAREA ------------------//

    public static void orderByTarea(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("start")) {
                orderByStart(event);
            } else if (args[1].equalsIgnoreCase("end")) {
                orderByEnd(event);
            } else {
                avisarComandoDesconocido(event);
            }
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void orderByStart(@NotNull GuildMessageReceivedEvent event) {
        List<Tarea> tareasAOrdenar = parseTodosLosMensajesATareas(event);
        List<Tarea> tareasAOrdenarAux = from(tareasAOrdenar).sortedBy(Tarea::getFechaCreacion).toList();

        for (Tarea tareaAEnviar : tareasAOrdenarAux) {
            event.getChannel().sendMessage(tareaAEnviar.toString()).complete();
        }
    }

    private static void orderByEnd(@NotNull GuildMessageReceivedEvent event) {
        List<Tarea> tareasAOrdenar = parseTodosLosMensajesATareas(event);
        List<Tarea> tareasAOrdenarAux = from(tareasAOrdenar).sortedBy(Tarea::getFechaCompletada).toList();

        for (Tarea tareaAEnviar : tareasAOrdenarAux) {
            event.getChannel().sendMessage(tareaAEnviar.toString()).complete();
        }
    }

    private static List<Tarea> parseTodosLosMensajesATareas(@NotNull GuildMessageReceivedEvent event) {
        List<Message> ultimosMensajes = getUltimosMensajes(event, true);
        event.getChannel().deleteMessages(ultimosMensajes).complete();

        List<Tarea> tareasParseadas = new ArrayList<>();
        for (Message tareaAParsear : ultimosMensajes) {
            tareasParseadas.add(Tarea.parseMensajeATarea(tareaAParsear.getContentRaw()));
        }
        return tareasParseadas;
    }

    //------------------ UTILS ------------------//

    public static void comprobarComandosCanalTareas(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase(PREFIX + "add"))
            addTarea(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "delete"))
            deleteTarea(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "close"))
            closeTarea(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "save"))
            saveTarea(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "orderBy"))
            orderByTarea(event, args);
        else
            avisarComandoDesconocido(event);
    }

    public static void comprobarComandosCanalHistoricoTareas(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase(PREFIX + "delete"))
            deleteTarea(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "open"))
            openTarea(event, args);
        else
            avisarComandoDesconocido(event);
    }

    @Nullable
    private static Message buscarTareaPorID(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message mensajeEncontrado = null;
        if (!args[1].equalsIgnoreCase("all")) {
            List<Message> ultimosMensajes = getUltimosMensajes(event, true);

            for (Message mensajeActual : ultimosMensajes) {
                String contenidoMensaje = mensajeActual.getContentRaw().toLowerCase();
                if (contenidoMensaje.contains(args[1].toLowerCase())) {
                    mensajeEncontrado = mensajeActual;
                    break;
                }
            }
        }
        return mensajeEncontrado;
    }

    private static void avisarTareaIndicadaNoExiste(@NotNull GuildMessageReceivedEvent event) {
        event.getMessage().getChannel().sendMessage("La tarea indicada no existe.").complete();

        Message ultimoMensaje = event.getChannel().getHistory().retrievePast(1).complete().get(0);
        event.getChannel().deleteMessageById(ultimoMensaje.getIdLong()).completeAfter(1, TimeUnit.SECONDS);
    }
}
