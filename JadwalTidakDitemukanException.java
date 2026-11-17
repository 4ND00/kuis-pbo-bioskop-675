public class JadwalTidakDitemukanException extends Exception {
    public JadwalTidakDitemukanException(String kode) {
        super("Jadwal film dengan kode " + kode + " tidak ditemukan");
    }
}
