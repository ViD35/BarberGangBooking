package vid35.dev.barbergangbooking.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {
    private String alias, nombre, apellido, telefono, citaId;

    public User() {
    }

    public User(String citaId,String alias, String nombre, String apellido, String telefono) {
        this.alias = alias;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.citaId = citaId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCitaId() {
        return citaId;
    }

    public void setCitaId(String citaId) {
        this.citaId = citaId;
    }

    public static Map<String,Object> mapData(User usuario){
        Map<String,Object> datos = new HashMap<>();
        datos.put("alias",usuario.getAlias());
        datos.put("nombre",usuario.getNombre());
        datos.put("apellido",usuario.getApellido());
        return datos;
    }
}
