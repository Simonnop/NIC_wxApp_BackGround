package group.service;

import group.pojo.User;

public interface LoginService {

    User tryLogin(String username);
}
