package object;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private boolean isBanned;
    private boolean isOnline;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isOnline = isOnline();
        this.isBanned = isBanned();
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

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
