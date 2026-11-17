# Kuis Praktikum PBO - Sistem Bioskop (Kombinasi 675)

- **Digit 6**: Sistem Bioskop (`MainBioskop` tidak diubah)
- **Digit 7**: Format input `Titik Dua & Pipe` -> `ID:ACT-01;KD:OBJ-A;JML:3;|ID:ACT-02;...`
- **Digit 5**: Serialisasi ke `data_master.bak` + `laporan.txt` format CSV

## Cara menjalankan
```
javac *.java
java MainBioskop
```
File `input_transaksi.txt` harus berada di folder yang sama saat program dijalankan.

## Struktur class
| Class | Peran |
|---|---|
| `Aktor` (abstract) -> `Penonton` | Inheritance + encapsulation |
| `JadwalFilm` (abstract) -> `PremiereStudio`, `RegulerStudio`, `EkonomisStudio` | Polymorphism (harga & biaya berbeda) |
| `Transaksi` | Association ke `Penonton` dan `JadwalFilm` |
| `SistemBioskop` | Sistem utama: parsing file, transaksi, laporan akhir |
| `KuotaTidakCukupException`, `PenontonTidakDitemukanException`, `JadwalTidakDitemukanException`, `FormatTransaksiException` | Custom exception |
