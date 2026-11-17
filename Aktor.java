import java.io.Serializable;

/** Kelas induk abstrak untuk semua aktor (pengguna) di dalam sistem. */
public abstract class Aktor implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;     // encapsulation: field private
    private String nama;

    public Aktor(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId()   { return id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    /** Polymorphism: tiap turunan menyebutkan perannya sendiri. */
    public abstract String getPeran();

    @Override
    public String toString() {
        return getPeran() + " [" + id + "] " + nama;
    }
}
