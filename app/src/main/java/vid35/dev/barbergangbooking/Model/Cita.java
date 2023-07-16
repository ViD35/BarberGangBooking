package vid35.dev.barbergangbooking.Model;

public class Cita {
    private String barberId,fechaCita;
    private Long slot;

    public Cita() {
    }

    public Cita(String barberId, String fechaCita, Long slot) {
        this.barberId = barberId;
        this.fechaCita = fechaCita;
        this.slot = slot;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
