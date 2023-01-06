package view;

import model.Book;
import service.BookService;
import util.JOptionUtil;

public class BookView {

    private final BookService bookService;

    public BookView(BookService bookService) {
        this.bookService = bookService;
    }

    public void menu() {
        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan - Buku",
                    "1. Data buku \n2. Tambah buku \n3. Edit buku \n4. Hapus buku \n5. Kembali"
            );

            switch (pilih) {
                case "1":
                    getAllBooks();
                    break;
                case "2":
                    addBook();
                    break;
                case "3":
                    editBook();
                    break;
                case "4":
                    removeBook();
                    break;
                case "5":
                    goHome();
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public void getAllBooks() {
        while (true) {
            String allData = bookService.getAllData();
            String getData = JOptionUtil.plainMessage(
                    "Perpustakaan - Data buku",
                    "" + allData + "\n0. Kembali \nMasukan kode buku untuk detail : "
            );

            if (getData.equals("0")) {
                menu();
            } else {
                try {
                    Integer.parseInt(getData);
                    bookService.getData(getData);
                } catch (NumberFormatException ex) {
                    JOptionUtil.showMessage("Masukan kode buku dengan benar");
                }
            }
        }
    }

    public void addBook() {
        Book book = new Book();
        while (true) {
            String judul = JOptionUtil.plainMessage("Perpustakaan - Tambah buku", "Judul buku :");
            if (!judul.isEmpty()) {
                book.setJudul(judul);
                break;
            } else {
                JOptionUtil.showMessage("Judul tidak boleh kosong");
            }
        }
        while (true) {
            String penulis = JOptionUtil.plainMessage("Perpustakaan - Tambah buku", "Penulis buku :");
            if (!penulis.isEmpty()) {
                book.setPenulis(penulis);
                break;
            } else {
                JOptionUtil.showMessage("Penulis tidak boleh kosong");
            }
        }
        while (true) {
            String kategori = JOptionUtil.plainMessage("Perpustakaan - Tambah buku", "Kategori buku :");
            if (!kategori.isEmpty()) {
                book.setPenulis(kategori);
                break;
            } else {
                JOptionUtil.showMessage("Kategori tidak boleh kosong");
            }
        }
        while (true) {
            String jumlah = JOptionUtil.plainMessage("Perpustakaan - Tambah buku", "Jumlah buku :");
            try {
                int jml = Integer.parseInt(jumlah);
                book.setJumlah(jml);
                break;
            } catch (Exception ex) {
                JOptionUtil.showMessage("Masukan jumlah buku dengan benar");
            }
        }

        bookService.add(book);
    }

    public void editBook() {
        Book book = new Book();

        String id = JOptionUtil.plainMessage("Perpustakaan - Edit buku", "0. Kembali \nMasukan kode buku yang akan diedit :");

        if (id.equals("0")) {
            menu();
        } else {
            try {
                Integer.parseInt(id);
                boolean dataExist = bookService.dataExist(id);
                if (dataExist) {
                    bookService.edit(id, book);
                    while (true) {
                        String judul = JOptionUtil.initialValue("Judul buku :", book.getJudul());
                        if (!judul.isEmpty()) {
                            book.setJudul(judul);
                            break;
                        } else {
                            JOptionUtil.showMessage("Judul tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String penulis = JOptionUtil.initialValue("Penulis buku :", book.getPenulis());
                        if (!penulis.isEmpty()) {
                            book.setPenulis(penulis);
                            break;
                        } else {
                            JOptionUtil.showMessage("Penulis tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String kategori = JOptionUtil.initialValue("Kategori buku :", book.getKategori());
                        if (!kategori.isEmpty()) {
                            book.setKategori(kategori);
                            break;
                        } else {
                            JOptionUtil.showMessage("Kategori tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String jumlah = JOptionUtil.initialValue("Jumlah buku : ", book.getJumlah().toString());
                        try {
                            int jml = Integer.parseInt(jumlah);
                            book.setJumlah(jml);
                            break;
                        } catch (Exception ex) {
                            JOptionUtil.showMessage("Masukan jumlah buku dengan benar");
                        }
                    }

                    bookService.update(id, book);
                } else {
                    JOptionUtil.showMessage("Kode buku " + id + " tidak ditemukan");
                }
            } catch (Exception ex) {
                JOptionUtil.showMessage("Masukan kode buku dengan benar");
            }
        }
    }

    public void removeBook() {
        String id = JOptionUtil.plainMessage(
                "Perpustakaan - Hapus buku",
                "0. Kembali \nMasukan kode buku untuk dihapus : "
        );

        if (id.equals("0")) {
            menu();
        } else {
            try {
                Integer.parseInt(id);
                boolean dataExist = bookService.dataExist(id);
                if (dataExist) {
                    int input = JOptionUtil.showConfirm("Kode buku : " + id + "\nHapus buku?");
                    if (input == 0) {
                        bookService.remove(id);
                    } else {
                        menu();
                    }
                } else {
                    JOptionUtil.showMessage("Kode buku " + id + " tidak ditemukan");
                }
            } catch (Exception ex) {
                JOptionUtil.showMessage("Masukan kode buku dengan benar");
            }
        }
    }

    public void goHome() {
        HomeView.mainMenu();
    }

}
