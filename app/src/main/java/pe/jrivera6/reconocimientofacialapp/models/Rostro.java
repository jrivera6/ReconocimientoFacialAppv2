package pe.jrivera6.reconocimientofacialapp.models;

public class Rostro {

    private Long id;
    private String genero_rostro;
    private String estado_rostro;
    private Long id_captura;

    public Rostro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenero_rostro() {
        return genero_rostro;
    }

    public void setGenero_rostro(String genero_rostro) {
        this.genero_rostro = genero_rostro;
    }

    public String getEstado_rostro() {
        return estado_rostro;
    }

    public void setEstado_rostro(String estado_rostro) {
        this.estado_rostro = estado_rostro;
    }

    public Long getId_captura() {
        return id_captura;
    }

    public void setId_captura(Long id_captura) {
        this.id_captura = id_captura;
    }

    @Override
    public String toString() {
        return "Rostro{" +
                "id=" + id +
                ", genero_rostro='" + genero_rostro + '\'' +
                ", estado_rostro='" + estado_rostro + '\'' +
                ", id_captura=" + id_captura +
                '}';
    }
}
