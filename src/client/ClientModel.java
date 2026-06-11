package client;


import object.*;


import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Stores data of the chat application and manages communication with the sever
 */
public class ClientModel {
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<GroupMessage> gcList = new ArrayList<>();
    private ArrayList<PrivateMessage> pmList = new ArrayList<>();
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket clientSocket;
    private String username, password;
    private ClientView clientView;
    private ClientController clientController;

    // Getter methods for CLinet Model variables
    public String getUsername() {
        return username;
    }
    public ArrayList<GroupMessage> getGcList() {
        return gcList;
    }
    public ArrayList<PrivateMessage> getPmList() {
        return pmList;
    }
    /**
     * Connects to the Server and sends out a User object to verify a client's login
     */
    public int connect(String uName, String pswd, String serverAddress) throws IOException, ClassNotFoundException {
        clientSocket = new Socket(serverAddress, 9000);
        this.username = uName;
        this.password = pswd;
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        User user = new User(username, password);
        out.writeObject(user);
        return (int) in.readObject();
    }
    /**
     * Sends a private message to the server
     */
    public void sendPrivateMessage(String text, String recipient) throws IOException {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String var = Integer.toString(hour);
        String time = var + ":" + min;
        PrivateMessage newMsg = new PrivateMessage(username,recipient, text, time);
        out.writeObject(newMsg);
    }

    /**
     * Sends a broadcast message to the server
     */
    public void sendBroadcastMessage(String text, String sender) throws IOException {
        Broadcast newMsg = new Broadcast(sender, text);
        out.writeObject(newMsg);
    }

    /**
     * Sends a group message to the server
     */
    public void sendGroupMessage(String text, String group) throws IOException {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String var = Integer.toString(hour);
        String time = var + ":" + min;
        GroupMessage newMsg = new GroupMessage(group,username, text, time);
        out.writeObject(newMsg);
    }

    /**
     * Creates a new group chat
     */
    public void createNewGC(String groupName, ArrayList<String> groupMembers) throws IOException {
        Group newGC = new Group(getUsername(), groupName, groupMembers);
        out.writeObject(newGC);
    }

    /**
     * Allows users to bookmark other users
     */
    public void addBookmark(ArrayList<String> usersToBookMark) throws IOException {
        Bookmark newBK = new Bookmark(getUsername(),usersToBookMark);
        out.writeObject(newBK);
    }

    /**
     * Reads server input and structures its String for display
w    */
    public void readServerInput() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();

        if (obj instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) obj;
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
                if (firstElement instanceof String) {
                    ArrayList<String> usernameList = (ArrayList<String>) obj;
                    clientView.updateDMDropdown(usernameList);
                    clientView.updateUserListDropdown(usernameList);
                    clientView.updateBookmarkDropdown(usernameList);
                } else if (firstElement instanceof PrivateMessage) {
                    pmList = (ArrayList<PrivateMessage>) obj;
                    clientView.initialUpdateTextArea(pmList, clientView.getDropdownDMValue());
                } else if (firstElement instanceof GroupMessage) {
                    gcList = (ArrayList<GroupMessage>) obj;
                    clientView.groupUpdateTextArea(gcList, clientView.getDropdownGCValue());
                } else if (firstElement == getUsername()){
                    ArrayList<String> newUserList = (ArrayList<String>) obj;
                    clientView.updateDMDropdownWithBK(newUserList);
                }
            }
        } else if (obj instanceof Broadcast) {
            String broadcastMessage = ((Broadcast) obj).getBroadcast();
            String fromUser = ((Broadcast) obj).getFromUser();

            JOptionPane.showMessageDialog(null, "From: " + broadcastMessage + "\n" +fromUser);

        } else if (obj instanceof Vector<?>) {
            Vector<String> vector = (Vector<String>) obj;
            ArrayList<String> arrayList = new ArrayList<>(vector);
            clientView.updateGCDropdown(arrayList);
        }
    }

    /**
     * Creates a thread to listen to incoming input from the server
     */
    public void listenForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        readServerInput();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Removes connection from the server and closes all object streams
     */
    public void disconnect() {
        try {
            if (clientSocket.isConnected()) {
                clientSocket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Notifies the Client View if a new Message has been read
     */
    public void addObserver(ClientView clientView){
        this.clientView = clientView;
    }


}
