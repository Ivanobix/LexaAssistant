package lexa.utils;

import java.util.UUID;

public class Utils {

    private Utils() {
    }

    public static String obtenerNuevoId() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 4);
    }
}
