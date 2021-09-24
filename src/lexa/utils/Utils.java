package lexa.utils;

import lexa.Launcher;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static lexa.utils.Globals.CANAL_CONFIGURACIONES;

public class Utils {

    private Utils() {
    }

    public static String obtenerNuevoId() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 4);
    }

    public static Map<String, String> obtenerConfiguraciones() {
        HashMap<String, String> configuraciones = new HashMap<>();

        Message message = Launcher.getJda().getTextChannelsByName(CANAL_CONFIGURACIONES, true).get(0).getHistory().retrievePast(1).complete().get(0);
        String[] lineas = message.getContentRaw().split("\n");
        for (String linea : lineas) {
            String[] configuracion = linea.split(" → ");
            configuraciones.put(configuracion[0], configuracion[1]);
        }

        return configuraciones;
    }
}
