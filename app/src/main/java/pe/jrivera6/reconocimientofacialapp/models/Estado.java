package pe.jrivera6.reconocimientofacialapp.models;

import java.util.List;

public class Estado {

    private Long id;
    private String estado_rostro;
    private List<Rostro> rostros;

    public Estado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado_rostro() {
        return estado_rostro;
    }

    public void setEstado_rostro(String estado_rostro) {
        this.estado_rostro = estado_rostro;
    }

    public List<Rostro> getRostros() {
        return rostros;
    }

    public void setRostros(List<Rostro> rostros) {
        this.rostros = rostros;
    }

    @Override
    public String toString() {
        return "Estado{" +
                "id=" + id +
                ", estado_rostro='" + estado_rostro + '\'' +
                ", rostros=" + rostros +
                '}';
    }
}
