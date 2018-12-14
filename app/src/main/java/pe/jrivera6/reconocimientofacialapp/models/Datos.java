package pe.jrivera6.reconocimientofacialapp.models;

public class Datos {
    private String estado_rostro;

    private Integer cantidad_rostros;

    public String getEstado_rostro() {
        return estado_rostro;
    }

    public void setEstado_rostro(String estado_rostro) {
        this.estado_rostro = estado_rostro;
    }

    public Integer getCantidad_rostros() {
        return cantidad_rostros;
    }

    public void setCantidad_rostros(Integer cantidad_rostros) {
        this.cantidad_rostros = cantidad_rostros;
    }

    @Override
    public String toString() {
        return "Datos{" +
                "estado_rostro='" + estado_rostro + '\'' +
                ", cantidad_rostros=" + cantidad_rostros +
                '}';
    }
}
