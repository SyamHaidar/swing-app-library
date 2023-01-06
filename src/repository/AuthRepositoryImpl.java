package repository;

import model.Auth;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public boolean getUser(Auth auth) {
        boolean login = false;

        try (Connection conn = DatabaseUtil.configDB()) {
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, auth.getUsername());
            stm.setString(2, auth.getPassword());
            ResultSet res = stm.executeQuery();

            if (res.next()) {
                login = auth.getUsername().equals(res.getString("username")) && auth.getPassword().equals(res.getString("password"));
            }

            if (login) {
                System.out.println("Login berhasil");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return login;
    }
}
