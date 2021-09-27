package lexa.helpers;

import lexa.plantillas.AsuntosPropios;
import lexa.plantillas.HorasExtra;
import lexa.plantillas.Vacaciones;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lexa.listeners.GuildMessageListener.*;
import static lexa.utils.Globals.*;

public class ImputacionesHelper {

    private ImputacionesHelper() {

    }

    public static void comprobarComandosCanalImputaciones(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase(PREFIX + "add"))
            addImputacion(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "delete"))
            deleteImputacion(event, args);
        else if (args[0].equalsIgnoreCase(PREFIX + "save"))
            saveImputacion(event, args);
        else
            avisarComandoDesconocido(event);
    }

    public static void comprobarComandosCanalHistoricoImputaciones(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase(PREFIX + "delete"))
            deleteImputacion(event, args);
        else
            avisarComandoDesconocido(event);
    }

    //------------------ ADD IMPUTACION ------------------//

    public static void addImputacion(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("vacation")) {
                addVacaciones(event, args);
            } else if (args[1].equalsIgnoreCase("affair")) {
                addAsuntosPropios(event, args);
            } else if (args[1].equalsIgnoreCase("extra")) {
                addHorasExtra(event, args);
            } else {
                avisarComandoDesconocido(event);
            }
        } else {
            avisarComandoDesconocido(event);
        }
    }

    private static void addVacaciones(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 5) {
            try {
                LocalDate fechaInicio = LocalDate.parse(args[2], DATE_FORMATTER);
                LocalDate fechaFin = LocalDate.parse(args[3], DATE_FORMATTER);
                int diasLaborables = Integer.parseInt(args[4]);

                Vacaciones vacaciones = new Vacaciones(fechaInicio, fechaFin, diasLaborables);
                event.getMessage().getChannel().sendMessage(vacaciones.toString()).complete();
                eliminarUltimoComando(event);
            } catch (Exception e) {
                avisarDatosIncorrectos(event);
            }
        } else {
            avisarDatosIncorrectos(event);
        }
    }

    private static void addAsuntosPropios(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 7) {
            try {
                String descripcion = args[2];
                LocalDateTime fechaInicio = LocalDateTime.parse(args[3], DATE_TIME_FORMATTER);
                LocalDateTime fechaFin = LocalDateTime.parse(args[4], DATE_TIME_FORMATTER);
                String total = args[5];
                boolean devolver;

                if (args[6].equalsIgnoreCase("Si"))
                    devolver = true;
                else if (args[6].equalsIgnoreCase("No"))
                    devolver = false;
                else
                    throw new IllegalArgumentException();

                AsuntosPropios asuntosPropios = new AsuntosPropios(descripcion, fechaInicio, fechaFin, total, devolver);
                event.getMessage().getChannel().sendMessage(asuntosPropios.toString()).complete();
                eliminarUltimoComando(event);
            } catch (Exception e) {
                avisarDatosIncorrectos(event);
            }
        } else {
            avisarDatosIncorrectos(event);
        }
    }

    private static void addHorasExtra(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 5) {
            try {
                String motivo = args[2];
                LocalDate dia = LocalDate.parse(args[3], DATE_FORMATTER);
                String tiempo = args[4];

                HorasExtra horasExtra = new HorasExtra(motivo, dia, tiempo);
                event.getMessage().getChannel().sendMessage(horasExtra.toString()).complete();
                eliminarUltimoComando(event);
            } catch (Exception e) {
                avisarDatosIncorrectos(event);
            }
        } else {
            avisarDatosIncorrectos(event);
        }
    }

    //------------------ DELETE IMPUTACION ------------------//

    public static void deleteImputacion(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) {
                deleteTodasLasImputaciones(event);
            } else {
                deleteImputacionUnica(event, args);
            }
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void deleteTodasLasImputaciones(@NotNull GuildMessageReceivedEvent event) {
        List<Message> mensajes = getUltimosMensajes(event, false);
        if (mensajes.size() >= 2)
            event.getChannel().deleteMessages(mensajes).complete();
    }


    private static void deleteImputacionUnica(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message mensajeAEliminar = buscarImputacionPorID(event, args);

        if (mensajeAEliminar != null) {
            event.getChannel().deleteMessageById(mensajeAEliminar.getIdLong()).complete();
        } else {
            avisarImputacionIndicadaNoExiste(event);
        }
    }

    //------------------ SAVE IMPUTACION ------------------//

    public static void saveImputacion(@NotNull GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) {
                saveTodasLasImputaciones(event);
            } else {
                saveImputacionUnica(event, args);
            }
            eliminarUltimoComando(event);
        } else
            avisarComandoDesconocido(event);
    }

    private static void saveTodasLasImputaciones(@NotNull GuildMessageReceivedEvent event) {
        List<Message> mensajes = getUltimosMensajes(event, true);
        if (!mensajes.isEmpty()) {
            TextChannel canalImputacionesHistorico = CanalHelper.getCanalPorNombre(event, CANAL_HISTORICO_IMPUTACIONES);
            for (Message imputacionActual : mensajes) {
                canalImputacionesHistorico.sendMessage(imputacionActual).complete();
                event.getChannel().deleteMessages(mensajes).complete();
            }
        }
    }

    private static void saveImputacionUnica(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Message imputacionAGuardar = buscarImputacionPorID(event, args);

        if (imputacionAGuardar != null) {
            TextChannel canalImputacionesHistorico = CanalHelper.getCanalPorNombre(event, CANAL_HISTORICO_IMPUTACIONES);
            canalImputacionesHistorico.sendMessage(imputacionAGuardar).complete();
            event.getChannel().deleteMessageById(imputacionAGuardar.getIdLong()).complete();
        } else {
            avisarImputacionIndicadaNoExiste(event);
        }
    }

    //------------------ UTILS ------------------//

    @Nullable
    private static Message buscarImputacionPorID(@NotNull GuildMessageReceivedEvent event, String[] args) {
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

    private static void avisarImputacionIndicadaNoExiste(@NotNull GuildMessageReceivedEvent event) {
        event.getMessage().getChannel().sendMessage("La imputación indicada no existe.").complete();

        Message ultimoMensaje = event.getChannel().getHistory().retrievePast(1).complete().get(0);
        event.getChannel().deleteMessageById(ultimoMensaje.getIdLong()).completeAfter(1, TimeUnit.SECONDS);
    }
}
