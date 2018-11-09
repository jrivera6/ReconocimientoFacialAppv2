package pe.jrivera6.reconocimientofacialapp.models;

import com.orm.dsl.Table;

@Table
public class User {

    private Long id;
    private String email;
    private String nombre;
    private String tokken;

    public User() {
    }

    public User(String email, String nombre, String tokken) {
        this.email = email;
        this.nombre = nombre;
        this.tokken = tokken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTokken() {
        return tokken;
    }

    public void setTokken(String tokken) {
        this.tokken = tokken;
    }
}
