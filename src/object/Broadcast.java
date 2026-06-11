package object;

import java.io.Serializable;

public class Broadcast implements Serializable {
    private String broadcast;
    private String fromUser;

    public Broadcast(String broadcast, String fromUser) {
        this.broadcast = broadcast;
        this.fromUser = fromUser;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
