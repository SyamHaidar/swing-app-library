package repository;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayRepositoryImpl implements PayRepository {

    @Override
    public String getAll() {
        StringBuilder denda = new StringBuilder("Total denda setiap member\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT member.nama, transaction.memberId, SUM(transaction.totalDenda) AS sumDenda " +
                    "FROM transaction " +
                    "LEFT JOIN member ON transaction.memberId=member.id " +
                    "WHERE (totalDenda IS NOT NULL AND totalDenda!=0) " +
                    "GROUP BY transaction.memberId " +
                    "ORDER BY sumDenda DESC";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            while (res.next()) {
                String memberId = res.getString("memberId");
                String nama = res.getString("nama");
                double totalDenda = res.getDouble("sumDenda");

                denda.append(String.format("· %s - %s : Rp %,.2f\n", memberId, nama, totalDenda));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return denda.toString();
    }

    @Override
    public String total() {
        StringBuilder denda = new StringBuilder("Total seluruh denda\n==============================\n");

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT SUM(totalDenda) AS sumDenda FROM transaction";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                double totalDenda = res.getDouble("sumDenda");
                denda.append(String.format("· Rp %,.2f\n", totalDenda));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return denda.toString();
    }

    @Override
    public double latePercentage() {
        double allRow = 0;
        double allRowLate = 0;

        try (Connection conn = DatabaseUtil.configDB()) {
            // count semua row
            String sql1 = "SELECT COUNT(transactionId) AS totalRow FROM transaction";
            PreparedStatement stm1 = conn.prepareStatement(sql1);
            ResultSet res1 = stm1.executeQuery();

            if (res1.next()) {
                allRow = res1.getDouble("totalRow");
            }

            // count pengembalian terlambat
            String sql2 = "SELECT COUNT(transactionId) AS totalRow FROM transaction " +
                    "WHERE (totalDenda IS NOT NULL AND totalDenda!=0)";
            PreparedStatement stm2 = conn.prepareStatement(sql2);
            ResultSet res2 = stm2.executeQuery();

            if (res2.next()) {
                allRowLate = res2.getDouble("totalRow");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        double terlambat = allRowLate / allRow;
        terlambat *= 100;

        return terlambat;
    }
}
