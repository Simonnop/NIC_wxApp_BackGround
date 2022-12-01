package group.pojo;

public class User {
    String username;
    String password;
    String classStr;

    public User(String username, String password, String classStr) {
        this.username = username;
        this.password = password;
        this.classStr = classStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", classStr='" + classStr + '\'' +
               '}';
    }
}
