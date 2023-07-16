package vid35.dev.barbergangbooking.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Admin implements Parcelable {
    private String alias, nombre, apellido, telefono, status;

    public Admin() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Admin(Parcel in) {
        alias = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        telefono = in.readString();
        status = in.readString();
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(telefono);
        dest.writeString(status);
    }
}
