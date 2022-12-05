package group.service;

import group.pojo.User;

public interface UserService {

    User tryLogin(String username);

    void getMission(String username, String missionID, String kind);
}
