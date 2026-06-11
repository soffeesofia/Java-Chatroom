package object;

import java.io.Serializable;
import java.util.ArrayList;

public class Bookmark implements Serializable {
    private String username;
    private ArrayList<String> usersToBookMark;

    public Bookmark(String username, ArrayList<String> usersToBookMark) {
        this.username = username;
        this.usersToBookMark = usersToBookMark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getUsersToBookMark() {
        return usersToBookMark;
    }
    public void setUserToBookMark(ArrayList<String> usersToBookMark) {
        this.usersToBookMark = usersToBookMark;
    }
}