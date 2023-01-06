package view;

import model.Member;
import model.Transaction;
import service.TransactionService;
import util.JOptionUtil;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionView {

    private final TransactionService transactionService;

    public TransactionView(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void menu() {
        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan - Transaksi",
                    "1. Data transaksi \n2. Peminjaman \n3. Pengembalian \n4. Kembali"
            );

            switch (pilih) {
                case "1":
                    getAllTransaction();
                    break;
                case "2":
                    addTransaction();
                    break;
                case "3":
                    editTransaction();
                    break;
                case "4":
                    goHome();
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public void getAllTransaction() {
        int page = 0;
        int totalPages = transactionService.getTotalPages();

        while (true) {
            String allData = transactionService.getAllData(page);

            String getData = JOptionUtil.plainMessage(
                    "Perpustakaan - Data transaksi",
                    "" + allData + "\n1. Selanjutnya \n2. Sebelumnya \n\n0. Kembali \nMasukan kode transaksi untuk detail : "
            );

            if (getData.equals("0")) {
                menu();
            } else if (getData.equals("1")) {
                if (page == 0 || (page > 0 && (page + 1) != totalPages)) {
                    page = page + 1;
                } else if ((page + 1) == totalPages) {
                    JOptionUtil.showMessage("Anda berada di halaman akhir");
                }
            } else if (getData.equals("2")) {
                if (page == 0) {
                    JOptionUtil.showMessage("Anda berada di halaman akhir");
                } else if ((page + 1) == totalPages || (page > 0 && (page + 1) != totalPages)) {
                    page = page - 1;
                }
            } else {
                try {
                    Integer.parseInt(getData);
                    transactionService.getData(getData);
                } catch (NumberFormatException ex) {
                    JOptionUtil.showMessage("Masukan kode transaksi dengan benar");
                }
            }
        }
    }

    public void addTransaction() {
        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan - Transaksi peminjaman",
                    "Jika peminjam bukan member \ndaftarkan terlebih dahulu \n\n1. Daftar member \n2. Lanjutkan peminjaman \n3. Kembali \n4. Awal"
            );

            switch (pilih) {
                case "1":
                    addTransactionRegister();
                    break;
                case "2":
                    addTransactionMember();
                    break;
                case "3":
                    menu();
                    break;
                case "4":
                    goHome();
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public void addTransactionRegister() {
        List<String> kodeBuku = new ArrayList<>();

        Member member = new Member();
        Transaction transaction = new Transaction();

        // register member baru
        while (true) {
            String nama = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Nama lengkap :");
            if (!nama.isEmpty()) {
                member.setNama(nama);
                break;
            } else {
                JOptionUtil.showMessage("Nama tidak boleh kosong");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Tgl Lahir : (e.g : 01/12/2000)");
            if (!tgl.isEmpty()) {
                member.setTglLahir(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl lahir tidak boleh kosong");
            }
        }
        while (true) {
            String alamat = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Alamat :");
            if (!alamat.isEmpty()) {
                member.setAlamat(alamat);
                break;
            } else {
                JOptionUtil.showMessage("Alamat tidak boleh kosong");
            }
        }
        while (true) {
            String kelamin = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Kelamin : \n1. pria \n2. wanita");
            try {
                Integer.parseInt(kelamin);
                if (kelamin.equals("1")) {
                    member.setKelamin("pria");
                    break;
                } else if (kelamin.equals("2")) {
                    member.setKelamin("wanita");
                    break;
                } else {
                    JOptionUtil.showMessage("Pilih jenis kelamin");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Pilih jenis kelamin");
            }
        }
        while (true) {
            String telepon = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Telepon :");
            try {
                Long.parseLong(telepon);
                member.setTelepon(telepon);
                break;
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan nomor telepon dengan benar");
            }
        }

        // setelah register selesai, lanjutkan proses peminjaman
        boolean addBook = true;
        while (addBook) {
            String bookId = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Kode buku : ");
            try {
                Integer.parseInt(bookId);
                boolean dataExist = transactionService.checkBookId(bookId);
                if (dataExist) {
                    boolean checkBookId = true;
                    while (checkBookId) {
                        if (kodeBuku.contains(bookId)) {
                            JOptionUtil.showMessage("Member tidak boleh meminjam buku yang sama");
                            checkBookId = false;
                        } else {
                            kodeBuku.add(bookId);
                            boolean addMoreBook = true;
                            while (addMoreBook) {
                                String pilih = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "1. Tambah buku yang dipinjam \n2. Selesai");
                                if (pilih.equals("1")) {
                                    addMoreBook = false;
                                } else if (pilih.equals("2")) {
                                    addMoreBook = false;
                                    addBook = false;
                                } else {
                                    JOptionUtil.showMessage("Menu tidak ada");
                                }
                            }
                            checkBookId = false;
                        }
                    }
                } else {
                    JOptionUtil.showMessage("Kode buku " + bookId + " tidak ditemukan");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan kode buku dengan benar");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Tgl pinjam : (e.g 01/12/2000)");
            if (!tgl.isEmpty()) {
                transaction.setTglPinjam(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl pinjam tidak boleh kosong");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Tgl estimasi : (e.g 01/12/2000)");
            if (!tgl.isEmpty()) {
                transaction.setTglEstimasi(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl estimasi tidak boleh kosong");
            }
        }

        transactionService.add(transaction, member, kodeBuku);
    }

    public void addTransactionMember() {
        List<String> kodeBuku = new ArrayList<>();

        Member member = new Member();
        Transaction transaction = new Transaction();

        while (true) {
            String memberId = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Kode member : ");
            try {
                Integer.parseInt(memberId);
                boolean data = transactionService.checkMemberId(memberId);
                if (data) {
                    transaction.setMemberId(memberId);
                    break;
                } else {
                    JOptionUtil.showMessage("Kode member " + memberId + " tidak ditemukan");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan kode member dengan benar");
            }
        }

        boolean addBook = true;
        while (addBook) {
            String bookId = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Kode buku : ");
            try {
                Integer.parseInt(bookId);
                boolean dataExist = transactionService.checkBookId(bookId);
                if (dataExist) {
                    boolean checkBookId = true;
                    while (checkBookId) {
                        if (kodeBuku.contains(bookId)) {
                            JOptionUtil.showMessage("Member tidak boleh meminjam buku yang sama");
                            checkBookId = false;
                        } else {
                            kodeBuku.add(bookId);
                            boolean addMoreBook = true;
                            while (addMoreBook) {
                                String pilih = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "1. Tambah buku yang dipinjam \n2. Selesai");
                                if (pilih.equals("1")) {
                                    addMoreBook = false;
                                } else if (pilih.equals("2")) {
                                    addMoreBook = false;
                                    addBook = false;
                                } else {
                                    JOptionUtil.showMessage("Menu tidak ada");
                                }
                            }
                            checkBookId = false;
                        }
                    }
                } else {
                    JOptionUtil.showMessage("Kode buku " + bookId + " tidak ditemukan");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan kode buku dengan benar");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Tgl pinjam : (e.g 01/12/2000)");
            if (!tgl.isEmpty()) {
                transaction.setTglPinjam(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl pinjam tidak boleh kosong");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah peminjaman", "Tgl estimasi : (e.g 01/12/2000)");
            if (!tgl.isEmpty()) {
                transaction.setTglEstimasi(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl estimasi tidak boleh kosong");
            }
        }

        transactionService.add(transaction, member, kodeBuku);
    }

    public void editTransaction() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction();

        String transactionId = JOptionUtil.plainMessage(
                "Perpustakaan - Pengembalian buku",
                "0. Kembali \nMasukan kode transaksi : "
        );

        if (transactionId.equals("0")) {
            menu();
        } else {
            boolean dataExist = transactionService.dataExist(transactionId);
            if (dataExist) {
                transaction.setTglKembali(dtf.format(now));
                transactionService.update(transactionId, transaction);
            } else {
                JOptionUtil.showMessage("Kode transaksi " + transactionId + " tidak ditemukan");
            }
        }
    }

    public void goHome() {
        HomeView.mainMenu();
    }

}
