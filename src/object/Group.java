package object;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    String username;
    String groupname;
    ArrayList<String> members;
    public Group(String username, String groupname, ArrayList<String> members) {
        this.username = username;
        this.groupname = groupname;
        this.members = members;
    }
    public String getUsername() {
        return username;
    }
    public String getGroupName() {
        return groupname;
    }
    public ArrayList<String> getMembers(){
        return members;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }
    public void setMembers(ArrayList<String> members){
        this.members = members;
    }
}
