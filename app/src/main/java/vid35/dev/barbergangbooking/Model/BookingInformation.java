package vid35.dev.barbergangbooking.Model;

public class BookingInformation {
    private String barberAlias, barberApellido, barberId, barberNombre, userAlias, userApellido, userId, userNombre;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String barberAlias, String barberApellido, String barberId, String barberNombre, String userAlias, String userApellido, String userId, String userNombre, Long slot) {
        this.barberAlias = barberAlias;
        this.barberApellido = barberApellido;
        this.barberId = barberId;
        this.barberNombre = barberNombre;
        this.userAlias = userAlias;
        this.userApellido = userApellido;
        this.userId = userId;
        this.userNombre = userNombre;
        this.slot = slot;
    }

    public String getBarberAlias() {
        return barberAlias;
    }

    public void setBarberAlias(String barberAlias) {
        this.barberAlias = barberAlias;
    }

    public String getBarberApellido() {
        return barberApellido;
    }

    public void setBarberApellido(String barberApellido) {
        this.barberApellido = barberApellido;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getBarberNombre() {
        return barberNombre;
    }

    public void setBarberNombre(String barberNombre) {
        this.barberNombre = barberNombre;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserApellido() {
        return userApellido;
    }

    public void setUserApellido(String userApellido) {
        this.userApellido = userApellido;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNombre() {
        return userNombre;
    }

    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
