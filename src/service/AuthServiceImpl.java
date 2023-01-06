package service;

import model.Auth;
import repository.AuthRepository;


public class AuthServiceImpl implements AuthService{

    private final AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public boolean getUser(Auth auth) {
        return authRepository.getUser(auth);
    }
}
