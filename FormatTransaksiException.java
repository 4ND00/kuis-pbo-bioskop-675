public class FormatTransaksiException extends Exception {
    public FormatTransaksiException(String pesan) {
        super("Format transaksi salah: " + pesan);
    }
}
