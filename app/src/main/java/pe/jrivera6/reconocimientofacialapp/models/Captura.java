package pe.jrivera6.reconocimientofacialapp.models;

import java.util.List;

public class Captura {

    private Long id;
    private String fecha_captura;
    private String nombre_captura;
    private Integer cantidad_rostros;

    private Long id_usuario;
    private List<Rostro> rostros;

    public Captura() {
    }

    public Captura(String fecha_captura, String nombre_captura, Integer cantidad_rostros, Long id_usuario, List<Rostro> rostros) {
        this.fecha_captura = fecha_captura;
        this.nombre_captura = nombre_captura;
        this.cantidad_rostros = cantidad_rostros;
        this.id_usuario = id_usuario;
        this.rostros = rostros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha_captura() {
        return fecha_captura;
    }

    public void setFecha_captura(String fecha_captura) {
        this.fecha_captura = fecha_captura;
    }

    public String getNombre_captura() {
        return nombre_captura;
    }

    public void setNombre_captura(String nombre_captura) {
        this.nombre_captura = nombre_captura;
    }

    public Integer getCantidad_rostros() {
        return cantidad_rostros;
    }

    public void setCantidad_rostros(Integer cantidad_rostros) {
        this.cantidad_rostros = cantidad_rostros;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<Rostro> getRostros() {
        return rostros;
    }

    public void setRostros(List<Rostro> rostros) {
        this.rostros = rostros;
    }

    @Override
    public String toString() {
        return "Captura{" +
                "id=" + id +
                ", fecha_captura='" + fecha_captura + '\'' +
                ", nombre_captura='" + nombre_captura + '\'' +
                ", cantidad_rostros=" + cantidad_rostros +
                ", id_usuario=" + id_usuario +
                ", rostros=" + rostros +
                '}';
    }
}
