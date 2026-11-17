public class PenontonTidakDitemukanException extends Exception {
    public PenontonTidakDitemukanException(String id) {
        super("Penonton dengan ID " + id + " tidak terdaftar");
    }
}
