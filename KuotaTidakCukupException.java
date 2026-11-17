public class KuotaTidakCukupException extends Exception {
    public KuotaTidakCukupException(String kode, int diminta, int tersisa) {
        super("Kuota " + kode + " tidak cukup (diminta " + diminta + ", tersisa " + tersisa + ")");
    }
}
