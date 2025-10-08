package Gestor;

public class ControlVeterinario {
    private String fecha;
    private String tipoControl;
    private String observaciones;

    public ControlVeterinario() {}

    public ControlVeterinario(String fecha, String tipoControl, String observaciones) {
        this.fecha = fecha;
        this.tipoControl = tipoControl;
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoControl() {
        return tipoControl;
    }

    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Control [Fecha: " + fecha + ", Tipo: " + tipoControl + ", Obs: " + observaciones + "]";
    }
}
