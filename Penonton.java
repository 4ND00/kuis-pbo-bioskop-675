/** Penonton bioskop: turunan dari Aktor. */
public class Penonton extends Aktor {
    private static final long serialVersionUID = 1L;

    private int totalTiket;   // jumlah tiket yang sudah dibeli

    public Penonton(String id, String nama) {
        super(id, nama);
        this.totalTiket = 0;
    }

    public int getTotalTiket() { return totalTiket; }

    public void tambahTiket(int jumlah) {
        this.totalTiket += jumlah;
    }

    @Override
    public String getPeran() { return "Penonton"; }
}
