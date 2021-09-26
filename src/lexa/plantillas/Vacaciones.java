package lexa.plantillas;

import java.time.LocalDate;

import static lexa.utils.Globals.DATE_FORMATTER;
import static lexa.utils.Utils.obtenerNuevoId;

public class Vacaciones {
    private String idVacaciones;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int diasLaborables;

    public Vacaciones(String idVacaciones, LocalDate fechaInicio, LocalDate fechaFin, int diasLaborables) {
        this.idVacaciones = idVacaciones;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasLaborables = diasLaborables;
    }

    public Vacaciones(LocalDate fechaInicio, LocalDate fechaFin, int diasLaborables) {
        idVacaciones = obtenerNuevoId();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasLaborables = diasLaborables;
    }

    public String getIdVacaciones() {
        return idVacaciones;
    }

    public void setIdVacaciones(String idVacaciones) {
        this.idVacaciones = idVacaciones;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getDiasLaborables() {
        return diasLaborables;
    }

    public void setDiasLaborables(int diasLaborables) {
        this.diasLaborables = diasLaborables;
    }

    @Override
    public String toString() {
        String fechaInicioFormateada = fechaInicio.format(DATE_FORMATTER);
        String fechaFinFormateada = fechaFin.format(DATE_FORMATTER);

        return ":trident: ***" + idVacaciones + " - Vacaciones: ***\n" +
                "          :small_blue_diamond: *Inicio:*                                         → " + fechaInicioFormateada + "\n" +
                "          :small_blue_diamond: *Fin:*                                              → " + fechaFinFormateada + "\n" +
                "          :small_blue_diamond: *Días Laborables Totales:*        → " + diasLaborables + "\n";
    }
}
