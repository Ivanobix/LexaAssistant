package lexa.plantillas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lexa.utils.Globals.DATE_TIME_FORMATTER;
import static lexa.utils.Utils.obtenerNuevoId;

public class Tarea {

    private String idTarea;
    private String link;
    private String proyecto;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCompletada;

    public Tarea(String idTarea, String link, String proyecto, String descripcion, LocalDateTime fechaCreacion, LocalDateTime fechaCompletada) {
        this.idTarea = idTarea;
        this.link = link;
        this.proyecto = proyecto;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaCompletada = fechaCompletada;
    }

    public Tarea(String link, String proyecto, String descripcion) {
        idTarea = obtenerNuevoId();
        this.link = link;
        this.proyecto = proyecto;
        this.descripcion = descripcion;

        fechaCreacion = LocalDateTime.now();
        fechaCompletada = LocalDateTime.MIN;
    }

    public Tarea(String proyecto, String descripcion) {
        idTarea = obtenerNuevoId();
        link = "";
        this.proyecto = proyecto;
        this.descripcion = descripcion;

        fechaCreacion = LocalDateTime.now();
        fechaCompletada = LocalDateTime.MIN;
    }

    public Tarea(String descripcion) {
        idTarea = obtenerNuevoId();
        link = "";
        proyecto = "";
        this.descripcion = descripcion;

        fechaCreacion = LocalDateTime.now();
        fechaCompletada = LocalDateTime.MIN;
    }

    public static Tarea parseMensajeATarea(String mensajeAParsear) {
        String[] lineas = mensajeAParsear.split("\n");
        String id = lineas[0].substring(13, 18);
        String descripcion = lineas[0].substring(20, lineas[0].length() - 3);

        String link = "";
        if (lineas[1].split("→ ").length == 2)
            link = lineas[1].split("→ ")[1];

        String proyecto = "";
        if (lineas[2].split("→ ").length == 2)
            proyecto = lineas[2].split("→ ")[1];

        LocalDateTime creacion = LocalDateTime.parse(lineas[3].split("→ ")[1], DATE_TIME_FORMATTER);

        LocalDateTime completada = LocalDateTime.MIN;
        String[] completadaStr = lineas[4].split("→ ");
        if (completadaStr.length == 2 && completadaStr[1] != null && !completadaStr[1].equals(""))
            completada = LocalDateTime.parse(completadaStr[1], DATE_TIME_FORMATTER);

        return new Tarea(id, link, proyecto, descripcion, creacion, completada);
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    @Override
    public String toString() {
        String fechaCreacionFormateada = fechaCreacion.format(DATE_TIME_FORMATTER);
        String fechaCompletadaFormateada = "";
        if (fechaCompletada != LocalDateTime.MIN)
            fechaCompletadaFormateada = fechaCompletada.format(DATE_TIME_FORMATTER);

        return ":cyclone: ***" + idTarea + " - " + descripcion + "***\n" +
                "          :small_blue_diamond: *Link:*                         → " + link + "\n" +
                "          :small_blue_diamond: *Proyecto:*                → " + proyecto + "\n" +
                "          :small_blue_diamond: *Añadida:*                 → " + fechaCreacionFormateada + "\n" +
                "          :small_blue_diamond: *Completada:*          → " + fechaCompletadaFormateada + "\n";
    }
}
