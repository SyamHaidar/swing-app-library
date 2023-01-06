package repository;

import model.Member;
import model.Transaction;
import util.DatabaseUtil;
import util.HelperUtil;
import util.JOptionUtil;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public boolean dataExist(String id) {
        boolean data = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM transaction WHERE transactionId=?";
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
    public int getTotalPages() {
        int limit = 10;
        int count = 0;
        int totalPages = 0;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sqlTotalRow = "SELECT COUNT(distinct transactionId) AS totalRow FROM transaction GROUP BY transactionId";
            Statement stmTotalRow = conn.prepareStatement(sqlTotalRow);
            ResultSet resTotalRow = stmTotalRow.executeQuery(sqlTotalRow);

            while (resTotalRow.next()) {
                int totalRow = resTotalRow.getInt("totalRow");
                count += totalRow;
            }
            totalPages = (int) Math.ceil(count / limit);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return totalPages + 1;
    }

    @Override
    public String getAllData(Integer page) {
        int limit = 10;
        int currentPage = page + 1;
        int offset = limit * page;

        String pages = currentPage + " of " + getTotalPages();
        StringBuilder data = new StringBuilder("List data transaksi (" + pages + ")\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM transaction " +
                    "LEFT JOIN member ON transaction.memberId=member.id " +
                    "GROUP BY transaction.transactionId " +
                    "ORDER BY STR_TO_DATE(transaction.tglPinjam, '%d/%m/%Y') DESC " +
                    "LIMIT " + limit +
                    " OFFSET " + offset;
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            while (res.next()) {
                String tglPinjam = res.getString("tglPinjam");
                String transactionId = res.getString("transactionId");
                String nama = res.getString("nama");

                data.append(String.format("· %s : %s - %s\n", tglPinjam, transactionId, nama));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public void getData(String id) {
        StringBuilder data = new StringBuilder("Detail transaksi\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            boolean dataExist = dataExist(id);

            if (dataExist) {
                String sqlBook = "SELECT * FROM transaction " +
                        "LEFT JOIN book ON transaction.bookId=book.id " +
                        "WHERE transaction.transactionId=?";
                PreparedStatement stmBook = conn.prepareStatement(sqlBook);
                stmBook.setString(1, id);
                ResultSet resBook = stmBook.executeQuery();

                StringBuilder book = new StringBuilder();
                while (resBook.next()) {
                    String idBuku = resBook.getString("bookId");
                    String judul = resBook.getString("judul");

                    book.append("· ").append(idBuku).append(" - ").append(judul).append("\n");
                }

                String sql = "SELECT *, COUNT(transaction.bookId) AS totalBook FROM transaction " +
                        "LEFT JOIN book ON transaction.bookId=book.id " +
                        "LEFT JOIN member ON transaction.memberId=member.id " +
                        "WHERE transaction.transactionId=?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, id);
                ResultSet res = stm.executeQuery();

                String status;
                if (res.next()) {
                    String transactionId = res.getString("transactionId");
                    String memberId = res.getString("memberId");
                    String nama = res.getString("nama");
                    String tglPinjam = res.getString("tglPinjam");
                    String tglKembali = res.getString("tglKembali");
                    String tglEstimasi = res.getString("tglEstimasi");
                    String totalDenda = res.getString("totalDenda");
                    int totalBook = res.getInt("totalBook");

                    if (totalDenda != null) {
                        status = "DIKEMBALIKAN";
                        String output = "Transaksi : %s \nPeminjam : %s - %s \n\nBuku yang dipinjam (%d) : \n%s \nTanggal : \n· Pinjam : %s \n· Estimasi : %s \n· Kembali : %s \n\nTotal Denda : Rp %s \nStatus : %s";
                        data.append(String.format(output, transactionId, memberId, nama, totalBook, book, tglPinjam, tglEstimasi, tglKembali, totalDenda, status));
                    } else {
                        status = "DIPINJAM";
                        String output = "Transaksi : %s \nPeminjam : %s - %s \n\nBuku yang dipinjam (%d) : \n%s \nTanggal : \n· Pinjam : %s \n· Estimasi : %s \n\nStatus : %s";
                        data.append(String.format(output, transactionId, memberId, nama, totalBook, book, tglPinjam, tglEstimasi, status));
                    }

                    JOptionUtil.showMessage(data.toString());
                }
            } else {
                data = new StringBuilder("Kode transaksi " + id + " tidak ditemukan");
                JOptionUtil.showMessage(data.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add(Transaction transaction, Member member, List<String> kodeBuku) {
        StringBuilder book = new StringBuilder();
        int stock = 0;
        int jumlahBuku = 0;

        try (Connection conn = DatabaseUtil.configDB()) {
            // Buku
            for (String bookId : kodeBuku) {
                // get data buku
                String sqlGetBook = "SELECT * FROM book WHERE id=?";
                PreparedStatement stmGetBook = conn.prepareStatement(sqlGetBook);
                stmGetBook.setString(1, bookId);
                ResultSet res = stmGetBook.executeQuery();

                if (res.next()) {
                    ++jumlahBuku;
                    String id = res.getString("id");
                    String judul = res.getString("judul");
                    stock = res.getInt("jumlah");

                    book.append("· ").append(id).append(" - ").append(judul).append("\n");
                }

                // update jumlah buku
                int totalStock = stock - 1;
                String sqlUpdateBook = "UPDATE book SET jumlah=? WHERE id=?";
                PreparedStatement stmUpdateBook = conn.prepareStatement(sqlUpdateBook);
                stmUpdateBook.setInt(1, totalStock);
                stmUpdateBook.setString(2, bookId);
                // exceute update book
                stmUpdateBook.executeUpdate();
            }

            // Member
            String memberId = (transaction.getMemberId() != null) ? transaction.getMemberId() : HelperUtil.randomNumber();
            // insert member baru
            String sqlMember = "INSERT INTO member (id,nama,tglLahir,alamat,kelamin,telepon) VALUE (?,?,?,?,?,?)";
            PreparedStatement stmMember = conn.prepareStatement(sqlMember);
            stmMember.setString(1, memberId);
            stmMember.setString(2, member.getNama());
            stmMember.setString(3, member.getTglLahir());
            stmMember.setString(4, member.getAlamat());
            stmMember.setString(5, member.getKelamin());
            stmMember.setString(6, member.getTelepon());

            // execute member baru
            if (member.getNama() != null) {
                stmMember.executeUpdate();
            } else {
                String sqlGetMember = "SELECT * FROM member WHERE id=?";
                PreparedStatement stmGetMember = conn.prepareStatement(sqlGetMember);
                stmGetMember.setString(1, memberId);
                ResultSet resMember = stmGetMember.executeQuery();

                if (resMember.next()) {
                    member.setNama(resMember.getString("nama"));
                }
            }

            // Transaksi
            String transactionId = HelperUtil.randomNumber();
            // insert transaksi baru
            for (String bookId : kodeBuku) {
                // insert data transaksi sebanyak buku yang dipinjam
                String sqlTransaction = "INSERT INTO transaction (transactionId,bookId,memberId,tglPinjam,tglEstimasi) VALUE (?,?,?,?,?)";
                PreparedStatement stmTransaction = conn.prepareStatement(sqlTransaction);
                stmTransaction.setString(1, transactionId);
                stmTransaction.setString(2, bookId);
                stmTransaction.setString(3, memberId);
                stmTransaction.setString(4, transaction.getTglPinjam());
                stmTransaction.setString(5, transaction.getTglEstimasi());
                // execute insert book
                stmTransaction.executeUpdate();
            }

            JOptionUtil.showMessage(
                    "Peminjaman berhasil\n==============================" +
                            "\nKode transaksi : " + transactionId +
                            "\nMember : " + memberId + " - " + member.getNama() +
                            "\n\nBuku yang dipinjam (" + jumlahBuku + ") : \n" + book +
                            "\nTanggal : " +
                            "\n· Pinjam : " + transaction.getTglPinjam() +
                            "\n· Estimasi : " + transaction.getTglEstimasi()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(String id, Transaction transaction) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        String idBuku;
        String tglEstimasi = "";
        String tglKembali = "";
        int jumlahBuku = 0;
        int stock = 0;

        try (Connection conn = DatabaseUtil.configDB()) {
            List<String> book = new ArrayList<>();

            // get data transaksi
            String sqlGetTransaction = "SELECT * FROM transaction WHERE transactionId=?";
            PreparedStatement stmGetTransaction = conn.prepareStatement(sqlGetTransaction);
            stmGetTransaction.setString(1, id);
            ResultSet res = stmGetTransaction.executeQuery();

            while (res.next()) {
                ++jumlahBuku;
                idBuku = res.getString("bookId");
                tglEstimasi = res.getString("tglEstimasi");
                tglKembali = res.getString("tglKembali");

                book.add(idBuku);
            }

            // hitung hari terlambat beserta total denda
            long time = HelperUtil.dayBetween(tglEstimasi, dtf.format(now));
            int denda = (int) (time * 1500);
            int totalDenda = denda * jumlahBuku;

            // data yang dimasukan yaitu denda per buku
            transaction.setTotalDenda(String.valueOf(denda));

            // get data buku
            for (String bookId : book) {
                String sqlGetBook = "SELECT * FROM book WHERE id=?";
                PreparedStatement stmGetBook = conn.prepareStatement(sqlGetBook);
                stmGetBook.setString(1, bookId);
                ResultSet resGetBook = stmGetBook.executeQuery();

                if (resGetBook.next()) {
                    stock = resGetBook.getInt("jumlah");
                }

                // update jumlah buku
                int totalStock = stock + 1;
                String sqlUpdateBook = "UPDATE book SET jumlah=? WHERE id=?";
                PreparedStatement stmUpdateBook = conn.prepareStatement(sqlUpdateBook);
                stmUpdateBook.setInt(1, totalStock);
                stmUpdateBook.setString(2, bookId);

                stmUpdateBook.executeUpdate();
            }

            // update data transaksi untuk pengembalian
            String sqlUpdateTransaction = "UPDATE transaction SET tglKembali=?, totalDenda=? WHERE transactionId=?";
            PreparedStatement stmUpdateTransaction = conn.prepareStatement(sqlUpdateTransaction);
            stmUpdateTransaction.setString(1, transaction.getTglKembali());
            stmUpdateTransaction.setString(2, transaction.getTotalDenda());
            stmUpdateTransaction.setString(3, id);

            if (tglKembali == null) {
                if (denda == 0) {
                    JOptionUtil.showMessage("Total biaya denda : Rp 0 \nPengembalian berhasil");
                } else {
                    while (true) {
                        String confirm = JOptionPane.showInputDialog(
                                "Detail pengembalian\n==============================\n" +
                                        "Kode transaksi : " + id +
                                        "\nTgl estimasi : " + tglEstimasi +
                                        "\nKeterlambatan : " + time + " hari" +
                                        "\nJumlah buku : " + jumlahBuku +
                                        "\n\nTotal biaya denda : Rp " + totalDenda +
                                        "\n\nKonfirmasi pembayaran \n1. Pembayaran diterima");
                        try {
                            Integer.parseInt(confirm);
                            if (confirm.equals("1")) {
                                JOptionUtil.showMessage("Pengembalian berhasil");
                                break;
                            } else {
                                JOptionUtil.showMessage("Menu tidak ada");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionUtil.showMessage("Konfirmasi pembayaran denda");
                        }
                    }
                }
                stmUpdateTransaction.executeUpdate();
            } else {
                JOptionUtil.showMessage("Transaki " + id + " sudah dikembalikan");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean checkBookId(String id) {
        boolean data = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sqlGetBook = "SELECT * FROM book WHERE id=?";
            PreparedStatement stmGetBook = conn.prepareStatement(sqlGetBook);
            stmGetBook.setString(1, id);
            ResultSet res = stmGetBook.executeQuery();

            if (res.next()) {
                data = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    @Override
    public boolean checkMemberId(String id) {
        boolean data = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sqlGetBook = "SELECT * FROM member WHERE id=?";
            PreparedStatement stmGetBook = conn.prepareStatement(sqlGetBook);
            stmGetBook.setString(1, id);
            ResultSet res = stmGetBook.executeQuery();

            if (res.next()) {
                data = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }
}
