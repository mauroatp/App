package isp.com.nooranv1;

/**
 * Created by Mauro on 11/11/2017.
 */

public class Usuario {

    public String nombre;
    public String email;
    public String foto;
    public String password;

    public Usuario(String nombre, String email, String foto, String password) {
        this.nombre = nombre;
        this.email = email;
        this.foto = foto;
        this.password = password;
    }

    public Usuario(){}

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
