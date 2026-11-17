public class RegulerStudio extends JadwalFilm {
    private static final long serialVersionUID = 1L;

    public RegulerStudio(String kode, String judul, int kuota) { super(kode, judul, kuota); }

    @Override public String getKategori()    { return "Reguler"; }
    @Override public double getHargaTiket()  { return 50000; }
}
