public class PremiereStudio extends JadwalFilm {
    private static final long serialVersionUID = 1L;
    private static final double BIAYA_LAYANAN = 5000;

    public PremiereStudio(String kode, String judul, int kuota) { super(kode, judul, kuota); }

    @Override public String getKategori()    { return "Premiere"; }
    @Override public double getHargaTiket()  { return 75000; }

    @Override
    public double hitungTotal(int jumlah) {          // override: ada biaya layanan premium
        return super.hitungTotal(jumlah) + BIAYA_LAYANAN;
    }
}
