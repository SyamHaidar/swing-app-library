package view;

import model.Auth;
import service.AuthService;
import util.JOptionUtil;

public class AuthView {

    private final AuthService authService;

    public AuthView(AuthService authService) {
        this.authService = authService;
    }

    public boolean menu() {
        Auth auth = new Auth();

        auth.setUsername(JOptionUtil.plainMessage("Perpustakaan - Login", "Username :").toLowerCase());
        auth.setPassword(JOptionUtil.plainMessage("Perpustakaan - Login", "Password :"));

        return authService.getUser(auth);
    }

}
