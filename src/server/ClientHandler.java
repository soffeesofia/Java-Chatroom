package server;

import object.*;

import java.awt.print.Book;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Manages the input and outputs from and to the client. Initiates Server Model methods to update the Chat Program's data
 */
public class ClientHandler implements Runnable {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket clientSocket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerModel serverModel;
    private String uName;
    private String getUName(){
        return uName;
    }
    public ClientHandler(Socket clientConnect, ServerModel serverModel) throws IOException, ClassNotFoundException {
        this.clientSocket = clientConnect;
        this.serverModel = serverModel;
        clientHandlers.add(this);
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    private void sendToRecipient(PrivateMessage message) throws IOException {
        serverModel.addPrivateMessagetoXML(message);
        System.out.println("Added to XML");
        out.writeObject(serverModel.getDMChatLogs());
        out.reset();
    }

    private void sendToGroup(GroupMessage message) throws IOException {
        serverModel.addGroupMessagetoXML(message);
        System.out.println("Added to XML");
        out.writeObject(serverModel.getGroupChatLogs());
        out.reset();
    }

    public void readClientInput() throws IOException, ClassNotFoundException {
        Timer timer = new Timer();
        while(true){
            Object obj = in.readObject();

            if(obj instanceof User){
                String username = ((User) obj).getUsername();
                String password = ((User) obj).getPassword();
                int result = serverModel.verifyUser(username, password);
                if (result == 3) {
                    out.writeObject(3);
                    out.reset();
                } else if(result == 1){
                    out.writeObject(1);
                    this.uName = username;
                    serverModel.addOnlineUser(uName);
                    serverModel.addServerLog(uName+" has connected.");
                    if(serverModel.checkIfBkmrk(uName)){
                        out.writeObject(serverModel.retrieveListOfUsers());
                    } else {
                        out.writeObject(serverModel.retrieveNewUserList(uName));
                    }
                    out.writeObject(serverModel.retrieveListOfGroup(username));
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                out.writeObject(serverModel.getDMChatLogs());
                                out.reset();
                                out.writeObject(serverModel.getGroupChatLogs());
                                out.reset();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, 0, 1000);

                    //THIS SENDS OUT THE ARRAYLIST SERVER MODEL 205

                } else if (result == 2);{
                    out.writeObject(2);
                    out.reset();
                }
            } else if (obj instanceof PrivateMessage){
                System.out.println(((PrivateMessage) obj).getMessage());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendToRecipient((PrivateMessage) obj);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                out.reset();
            } else if (obj instanceof GroupMessage) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendToGroup((GroupMessage) obj);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            } else if (obj instanceof Broadcast){
                Broadcast broadcast = (Broadcast) obj;
                out.writeObject(broadcast);
            } else if (obj instanceof Group){
                Group newGC = (Group) obj;
                String username = newGC.getUsername();
                serverModel.addNewGC(newGC);
                out.writeObject(serverModel.retrieveListOfGroup(username));
            } else if (obj instanceof Bookmark) {
                Bookmark newBK = (Bookmark) obj;
                String username = newBK.getUsername();
                ArrayList<String> usersToBkmrk = newBK.getUsersToBookMark();
                serverModel.addNewBkmrk(username, usersToBkmrk);
                out.writeObject(serverModel.retrieveNewUserList(username));
            }

        }
    }
    private void closeConnection() {
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
            clientHandlers.remove(this);
            serverModel.removeOnlineUser(uName);
            serverModel.addServerLog(uName+" has disconnected.");
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
            readClientInput();
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            closeConnection();
            e.printStackTrace();
        }

    }


}
