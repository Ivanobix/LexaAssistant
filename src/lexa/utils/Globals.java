package lexa.utils;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static lexa.utils.Utils.obtenerConfiguraciones;

public class Globals {

    //GENERAL
    public static final String PREFIX = "!";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //CANALES
    public static final String CANAL_TAREAS = "tareas";
    public static final String CANAL_HISTORICO_TAREAS = "historico-tareas";
    public static final String CANAL_IMPUTACIONES = "imputaciones";
    public static final String CANAL_HISTORICO_IMPUTACIONES = "historico-imputaciones";
    public static final String CANAL_HERRAMIENTAS = "herramientas";
    public static final String CANAL_CONFIGURACIONES = "configuraciones";

    //CONFIGURACIONES
    public static final Map<String, String> CONFIGURACIONES = obtenerConfiguraciones();

    private Globals() {
    }
}
