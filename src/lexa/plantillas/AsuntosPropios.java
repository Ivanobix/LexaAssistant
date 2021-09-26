package lexa.plantillas;

import java.time.LocalDateTime;

import static lexa.utils.Globals.DATE_TIME_FORMATTER;
import static lexa.utils.Utils.obtenerNuevoId;

public class AsuntosPropios {
    private String idAsuntosPropios;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String total;
    private boolean recuperar;

    public AsuntosPropios(String idAsuntosPropios, LocalDateTime fechaInicio, LocalDateTime fechaFin, String total, boolean recuperar) {
        this.idAsuntosPropios = idAsuntosPropios;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.total = total;
        this.recuperar = recuperar;
    }

    public AsuntosPropios(LocalDateTime fechaInicio, LocalDateTime fechaFin, String total, boolean recuperar) {
        idAsuntosPropios = obtenerNuevoId();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.total = total;
        this.recuperar = recuperar;
    }

    public String getIdAsuntosPropios() {
        return idAsuntosPropios;
    }

    public void setIdAsuntosPropios(String idAsuntosPropios) {
        this.idAsuntosPropios = idAsuntosPropios;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isRecuperar() {
        return recuperar;
    }

    public void setRecuperar(boolean recuperar) {
        this.recuperar = recuperar;
    }

    @Override
    public String toString() {
        String fechaInicioFormateada = fechaInicio.format(DATE_TIME_FORMATTER);
        String fechaFinFormateada = fechaFin.format(DATE_TIME_FORMATTER);
        String recuperarFormateado = recuperar ? "●●●" : "○○○";

        return ":fleur_de_lis: ***" + idAsuntosPropios + " - Asuntos Propios: ***\n" +
                "          :small_blue_diamond: *Inicio:*                → " + fechaInicioFormateada + "\n" +
                "          :small_blue_diamond: *Fin:*                     → " + fechaFinFormateada + "\n" +
                "          :small_blue_diamond: *Total:*                 → " + total + "\n" +
                "          :small_blue_diamond: *Recuperar:*        → " + recuperarFormateado + "\n";
    }
}
