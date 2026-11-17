public class EkonomisStudio extends JadwalFilm {
    private static final long serialVersionUID = 1L;

    public EkonomisStudio(String kode, String judul, int kuota) { super(kode, judul, kuota); }

    @Override public String getKategori()    { return "Ekonomis"; }
    @Override public double getHargaTiket()  { return 35000; }
}
