import java.io.*;
import java.util.*;

/**
 * Kelas sistem utama.
 * Digit 6 : tema Bioskop
 * Digit 7 : parsing format "Titik Dua & Pipe"  ->  ID:ACT-01;KD:OBJ-A;JML:3;|ID:...
 * Digit 5 : serialisasi ke data_master.bak + laporan.txt berformat CSV
 */
public class SistemBioskop implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nama;
    private final HashMap<String, Penonton> daftarPenonton = new LinkedHashMap<>();
    private final ArrayList<JadwalFilm> daftarJadwal = new ArrayList<>();
    private final ArrayList<Transaksi> logTransaksi = new ArrayList<>();
    private int jumlahGagal = 0;

    public SistemBioskop(String nama) {
        this.nama = nama;
    }

    // ---------------------------------------------------------------- data master
    public void tambahPenonton(String id, String nama) {
        daftarPenonton.put(id, new Penonton(id, nama));
    }

    public void tambahJadwalFilm(String kode, String judul, int kuota) {
        String j = judul.toLowerCase();
        JadwalFilm jadwal;
        if (j.contains("premiere"))      jadwal = new PremiereStudio(kode, judul, kuota);
        else if (j.contains("reguler"))  jadwal = new RegulerStudio(kode, judul, kuota);
        else                             jadwal = new EkonomisStudio(kode, judul, kuota);
        daftarJadwal.add(jadwal);
    }

    private JadwalFilm cariJadwal(String kode) throws JadwalTidakDitemukanException {
        for (JadwalFilm jf : daftarJadwal) {
            if (jf.getKode().equals(kode)) return jf;
        }
        throw new JadwalTidakDitemukanException(kode);
    }

    // ---------------------------------------------------------------- transaksi
    private void prosesTransaksi(String idPenonton, String kodeJadwal, int jumlah)
            throws PenontonTidakDitemukanException, JadwalTidakDitemukanException, KuotaTidakCukupException {
        Penonton p = daftarPenonton.get(idPenonton);
        if (p == null) throw new PenontonTidakDitemukanException(idPenonton);

        JadwalFilm jf = cariJadwal(kodeJadwal);
        jf.kurangiKuota(jumlah);          // bisa melempar KuotaTidakCukupException
        p.tambahTiket(jumlah);

        Transaksi t = new Transaksi(p, jf, jumlah);
        logTransaksi.add(t);
        System.out.println("[OK]    " + t);
    }

    /**
     * Membaca file berformat:  ID:ACT-01;KD:OBJ-A;JML:3;|ID:ACT-02;KD:OBJ-C;JML:2;|...
     *   - pemisah antar transaksi : '|'
     *   - pemisah antar field     : ';'
     *   - pemisah key dan value   : ':'
     * Error pada satu transaksi tidak menghentikan transaksi lain.
     */
    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        StringBuilder isi = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                isi.append(baris.trim());
            }
        }

        String[] daftarRecord = isi.toString().split("\\|");
        int nomor = 0;
        for (String record : daftarRecord) {
            record = record.trim();
            if (record.isEmpty()) continue;
            nomor++;
            try {
                Map<String, String> field = parseRecord(record);
                int jumlah;
                try {
                    jumlah = Integer.parseInt(field.get("JML"));
                } catch (NumberFormatException e) {
                    throw new FormatTransaksiException("JML bukan angka -> '" + field.get("JML") + "'");
                }
                if (jumlah <= 0) throw new FormatTransaksiException("JML harus lebih dari 0");

                prosesTransaksi(field.get("ID"), field.get("KD"), jumlah);

            } catch (FormatTransaksiException | PenontonTidakDitemukanException
                     | JadwalTidakDitemukanException | KuotaTidakCukupException e) {
                jumlahGagal++;
                System.out.println("[GAGAL] Transaksi #" + nomor + " -> " + e.getMessage());
            }
        }
    }

    /** Memecah satu record "ID:ACT-01;KD:OBJ-A;JML:3;" menjadi pasangan key-value. */
    private Map<String, String> parseRecord(String record) throws FormatTransaksiException {
        Map<String, String> hasil = new HashMap<>();
        for (String bagian : record.split(";")) {
            bagian = bagian.trim();
            if (bagian.isEmpty()) continue;
            String[] kv = bagian.split(":", 2);
            if (kv.length != 2) throw new FormatTransaksiException("bagian '" + bagian + "' tidak memiliki ':'");
            hasil.put(kv[0].trim(), kv[1].trim());
        }
        for (String wajib : new String[]{"ID", "KD", "JML"}) {
            if (!hasil.containsKey(wajib)) throw new FormatTransaksiException("field " + wajib + " tidak ada");
        }
        return hasil;
    }

    // ---------------------------------------------------------------- laporan akhir
    public void simpanLaporanAkhir() {
        System.out.println("--- MENYIMPAN LAPORAN AKHIR ---");

        // 1) Object Serialization -> data_master.bak  (digit ketiga = 5)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_master.bak"))) {
            oos.writeObject(this);
            System.out.println("[OK] Serialisasi berhasil -> data_master.bak");
        } catch (IOException e) {
            System.out.println("[ERROR] Serialisasi gagal: " + e.getMessage());
        }

        // 2) Laporan Ekspor CSV -> laporan.txt  (iterasi ArrayList transaksi)
        try (FileWriter fw = new FileWriter("laporan.txt")) {
            for (int i = 0; i < logTransaksi.size(); i++) {
                Transaksi t = logTransaksi.get(i);
                fw.write(t.getPenonton().getId() + "," + t.getJadwal().getKode() + "," + t.getJumlah() + "\n");
            }
            System.out.println("[OK] Laporan CSV berhasil -> laporan.txt (" + logTransaksi.size() + " baris)");
        } catch (IOException e) {
            System.out.println("[ERROR] Penulisan laporan.txt gagal: " + e.getMessage());
        }

        System.out.println("Transaksi sukses: " + logTransaksi.size() + ", gagal: " + jumlahGagal);
        System.out.println("--- Sisa kuota akhir " + nama + " ---");
        for (JadwalFilm jf : daftarJadwal) System.out.println("  " + jf);
    }
}
