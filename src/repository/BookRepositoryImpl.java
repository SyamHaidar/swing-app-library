package repository;

import model.Book;
import util.DatabaseUtil;
import util.HelperUtil;
import util.JOptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public boolean dataExist(String id) {
        boolean data = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM book WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                data = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    @Override
    public String getAllData() {
        StringBuilder data = new StringBuilder();

        data.append(getTopBook());
        data.append("\nList data buku\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM book";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            while (res.next()) {
                String id = res.getString("id");
                String judul = res.getString("judul");

                data.append(String.format("· %s - %s\n", id, judul));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public void getData(String id) {
        StringBuilder data = new StringBuilder("Detail buku\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM book WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                String idBuku = res.getString("id");
                String judul = res.getString("judul");
                String penulis = res.getString("penulis");
                String kategori = res.getString("kategori");
                String jumlah = res.getString("jumlah");

                String output = "Kode buku : %s \nJudul : %s \nPenulis : %s \nKategori : %s \nJumlah : %s";
                data.append(String.format(output, idBuku, judul, penulis, kategori, jumlah));

                JOptionUtil.showMessage(data.toString());
            } else {
                data = new StringBuilder("Kode buku " + id + " tidak ditemukan");
                JOptionUtil.showMessage(data.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getTopBook() {
        StringBuilder data = new StringBuilder("Buku terfavorit\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT book.judul, transaction.bookId, COUNT(transaction.bookId) as totalTransaction " +
                    "FROM transaction " +
                    "LEFT JOIN book ON transaction.bookId=book.id " +
                    "GROUP BY transaction.bookId " +
                    "ORDER BY totalTransaction DESC " +
                    "LIMIT 3";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            int count = 0;
            while (res.next()) {
                String id = res.getString("bookId");
                String judul = res.getString("judul");
                int totalTransaction = res.getInt("totalTransaction");

                String output = "%d. %s - %s (%dx)\n";
                data.append(String.format(output, ++count, id, judul, totalTransaction));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public void add(Book book) {
        String id = HelperUtil.randomNumber();

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "INSERT INTO book (id,judul,penulis,kategori,jumlah) VALUE (?,?,?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            stm.setString(2, book.getJudul());
            stm.setString(3, book.getPenulis());
            stm.setString(4, book.getKategori());
            stm.setInt(5, book.getJumlah());

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                JOptionUtil.showMessage(
                        "Data berhasil disimpan\n==============================" +
                                "\nKode buku : " + id +
                                "\nJudul : " + book.getJudul() +
                                "\nPenulis : " + book.getPenulis() +
                                "\nKategori : " + book.getKategori() +
                                "\nJumlah : " + book.getJumlah()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit(String id, Book book) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM book WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                book.setJudul(res.getString("judul"));
                book.setPenulis(res.getString("penulis"));
                book.setKategori(res.getString("kategori"));
                book.setJumlah(Integer.parseInt(res.getString("jumlah")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(String id, Book book) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "UPDATE book SET judul=?, penulis=?, kategori=?, jumlah=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, book.getJudul());
            stm.setString(2, book.getPenulis());
            stm.setString(3, book.getKategori());
            stm.setInt(4, book.getJumlah());
            stm.setString(5, id);

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                JOptionUtil.showMessage(
                        "Data berhasil diupdate\n==============================" +
                                "\nKode buku : " + id +
                                "\nJudul buku : " + book.getJudul() +
                                "\nPenulis : " + book.getPenulis() +
                                "\nKategori : " + book.getKategori() +
                                "\nJumlah : " + book.getJumlah()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void remove(String id) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "DELETE FROM book WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);

            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionUtil.showMessage("Data buku " + id + " berhasil dihapus");
            } else {
                JOptionUtil.showMessage("Kode buku " + id + " tidak ditemukan");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
