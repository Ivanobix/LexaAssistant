package lexa.plantillas;

import java.time.LocalDate;

import static lexa.utils.Globals.DATE_FORMATTER;
import static lexa.utils.Utils.obtenerNuevoId;

public class HorasExtra {
    private String idHorasExtra;
    private String motivo;
    private LocalDate dia;
    private String tiempo;

    public HorasExtra(String idHorasExtra, String motivo, LocalDate dia, String tiempo) {
        this.idHorasExtra = idHorasExtra;
        this.motivo = motivo;
        this.dia = dia;
        this.tiempo = tiempo;
    }

    public HorasExtra(String motivo, LocalDate dia, String tiempo) {
        idHorasExtra = obtenerNuevoId();
        this.motivo = motivo;
        this.dia = dia;
        this.tiempo = tiempo;
    }

    public String getIdHorasExtra() {
        return idHorasExtra;
    }

    public void setIdHorasExtra(String idHorasExtra) {
        this.idHorasExtra = idHorasExtra;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        String diaFormateado = dia.format(DATE_FORMATTER);

        return ":diamonds: ***" + idHorasExtra + " - " + motivo + "***\n" +
                "          :small_blue_diamond: *Día:*                         → " + diaFormateado + "\n" +
                "          :small_blue_diamond: *Tiempo:*                 → " + tiempo + "\n";
    }
}
