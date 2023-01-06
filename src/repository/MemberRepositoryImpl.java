package repository;

import model.Member;
import util.DatabaseUtil;
import util.HelperUtil;
import util.JOptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberRepositoryImpl implements MemberRepository {

    @Override
    public boolean dataExist(String id) {
        boolean data = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM member WHERE id=?";
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

        data.append(getTopMember());
        data.append("\n");
        data.append(getTopLateMember());
        data.append("\nList data member\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM member";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            while (res.next()) {
                int id = res.getInt("id");
                String nama = res.getString("nama");

                data.append(String.format("· %d - %s\n", id, nama));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public void getData(String id) {
        StringBuilder data = new StringBuilder("Detail member\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM member WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                String idMember = res.getString("id");
                String nama = res.getString("nama");
                String tglLahir = res.getString("tglLahir");
                String alamat = res.getString("alamat");
                String kelamin = res.getString("kelamin");
                String telepon = res.getString("telepon");

                String output = "Kode member : %s \nNama : %s \nTgl Lahir  : %s \nAlamat : %s \nKelamin : %s \nTelepon : %s";
                data.append(String.format(output, idMember, nama, tglLahir, alamat, kelamin, telepon));

                JOptionUtil.showMessage(data.toString());
            } else {
                data = new StringBuilder("Kode member " + id + " tidak ditemukan");
                JOptionUtil.showMessage(data.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getTopMember() {
        StringBuilder data = new StringBuilder("Member yang sering meminjam\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT member.nama, transaction.memberId, COUNT(distinct transaction.transactionId) as totalTransaction " +
                    "FROM transaction " +
                    "LEFT JOIN member ON transaction.memberId=member.id " +
                    "GROUP BY transaction.memberId " +
                    "ORDER BY totalTransaction DESC, STR_TO_DATE(transaction.tglPinjam, '%d/%m/%Y') DESC " +
                    "LIMIT 3";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            int count = 0;
            while (res.next()) {
                String id = res.getString("memberId");
                String nama = res.getString("nama");
                int totalTransaction = res.getInt("totalTransaction");

                String output = "%d. %s - %s (%dx)\n";
                data.append(String.format(output, ++count, id, nama, totalTransaction));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public String getTopLateMember() {
        StringBuilder data = new StringBuilder("Member yang sering terlambat\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT member.nama, transaction.memberId, COUNT(distinct transaction.transactionId) as totalTransaction " +
                    "FROM transaction " +
                    "LEFT JOIN member ON transaction.memberId=member.id " +
                    "WHERE (transaction.totalDenda IS NOT NULL AND transaction.totalDenda!=0) " +
                    "GROUP BY transaction.memberId " +
                    "ORDER BY totalTransaction DESC, STR_TO_DATE(transaction.tglPinjam, '%d/%m/%Y') DESC " +
                    "LIMIT 3";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            int count = 0;
            while (res.next()) {
                String id = res.getString("memberId");
                String nama = res.getString("nama");
                int totalTransaction = res.getInt("totalTransaction");

                String output = "%d. %s - %s (%dx)\n";
                data.append(String.format(output, ++count, id, nama, totalTransaction));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data.toString();
    }

    @Override
    public void add(Member member) {
        String id = HelperUtil.randomNumber();

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "INSERT INTO member (id,nama,tglLahir,alamat,kelamin,telepon) VALUE (?,?,?,?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            stm.setString(2, member.getNama());
            stm.setString(3, member.getTglLahir());
            stm.setString(4, member.getAlamat());
            stm.setString(5, member.getKelamin());
            stm.setString(6, member.getTelepon());

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                JOptionUtil.showMessage(
                        "Data berhasil disimpan\n==============================" +
                                "\nKode member : " + id +
                                "\nNama : " + member.getNama() +
                                "\nTgl Lahir : " + member.getTglLahir() +
                                "\nAlamat : " + member.getAlamat() +
                                "\nKelamin : " + member.getKelamin() +
                                "\nTelepon : " + member.getTelepon()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit(String id, Member member) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM member WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                member.setNama(res.getString("nama"));
                member.setTglLahir(res.getString("tglLahir"));
                member.setAlamat(res.getString("alamat"));
                member.setKelamin(res.getString("kelamin"));
                member.setTelepon(res.getString("telepon"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(String id, Member member) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "UPDATE member SET nama=?, tglLahir=?, alamat=?, kelamin=?, telepon=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, member.getNama());
            stm.setString(2, member.getTglLahir());
            stm.setString(3, member.getAlamat());
            stm.setString(4, member.getKelamin());
            stm.setString(5, member.getTelepon());
            stm.setString(6, id);

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                JOptionUtil.showMessage(
                        "Data berhasil diupdate\n==============================" +
                                "\n\nKode member : " + id +
                                "\nNama : " + member.getNama() +
                                "\nTgl Lahir : " + member.getTglLahir() +
                                "\nAlamat : " + member.getAlamat() +
                                "\nKelamin : " + member.getKelamin() +
                                "\nTelepon : " + member.getTelepon()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void remove(String id) {
        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "DELETE FROM member WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);

            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionUtil.showMessage("Data member " + id + " berhasil dihapus");
            } else {
                JOptionUtil.showMessage("Kode member " + id + " tidak ditemukan");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
