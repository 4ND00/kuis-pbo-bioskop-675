import java.io.Serializable;

/** Satu pembelian tiket. Association: menyimpan referensi ke Penonton dan JadwalFilm. */
public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Penonton penonton;
    private final JadwalFilm jadwal;
    private final int jumlah;
    private final double totalHarga;

    public Transaksi(Penonton penonton, JadwalFilm jadwal, int jumlah) {
        this.penonton = penonton;
        this.jadwal = jadwal;
        this.jumlah = jumlah;
        this.totalHarga = jadwal.hitungTotal(jumlah);   // polymorphism
    }

    public Penonton getPenonton() { return penonton; }
    public JadwalFilm getJadwal() { return jadwal; }
    public int getJumlah()        { return jumlah; }
    public double getTotalHarga() { return totalHarga; }

    @Override
    public String toString() {
        return penonton.getNama() + " beli " + jumlah + " tiket " + jadwal.getKode()
                + " (Rp" + String.format("%,.0f", totalHarga) + ")";
    }
}
