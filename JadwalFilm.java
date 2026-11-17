import java.io.Serializable;

/** Kelas induk abstrak untuk jadwal film (objek layanan yang punya kuota kursi). */
public abstract class JadwalFilm implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String kode;
    private final String judul;
    private final int kuotaAwal;
    private int sisaKuota;

    public JadwalFilm(String kode, String judul, int kuota) {
        this.kode = kode;
        this.judul = judul;
        this.kuotaAwal = kuota;
        this.sisaKuota = kuota;
    }

    public String getKode()      { return kode; }
    public String getJudul()     { return judul; }
    public int getKuotaAwal()    { return kuotaAwal; }
    public int getSisaKuota()    { return sisaKuota; }

    /** Polymorphism: harga dan kategori ditentukan oleh kelas turunan. */
    public abstract String getKategori();
    public abstract double getHargaTiket();

    /** Bisa di-override oleh turunan (contoh: Premiere punya biaya layanan). */
    public double hitungTotal(int jumlah) {
        return getHargaTiket() * jumlah;
    }

    /** Mengurangi kuota; melempar custom exception bila kuota tidak cukup. */
    public void kurangiKuota(int jumlah) throws KuotaTidakCukupException {
        if (jumlah > sisaKuota) {
            throw new KuotaTidakCukupException(kode, jumlah, sisaKuota);
        }
        sisaKuota -= jumlah;
    }

    @Override
    public String toString() {
        return "[" + kode + "] " + judul + " (" + getKategori() + ", sisa " + sisaKuota + "/" + kuotaAwal + ")";
    }
}
