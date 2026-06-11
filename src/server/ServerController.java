package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Manages client input and executes methods called for in the Server View
 */
public class ServerController extends JFrame {

    private ServerModel serverModel;
    private ServerView serverView;



    public ServerController(){
        this.serverModel = new ServerModel();
        this.serverView = new ServerView(serverModel);
        this.serverModel.addObserver(serverView);

        // ACTION LISTENERS FOR SERVER VIEW BUTTONS

        serverView.getStartServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverView.disableStartServerButton();
                startServer();
            }
        });

        serverView.getStopServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverView.disableStopServerButton();
                try {
                    stopServer();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        serverView.getAddUserButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        serverView.getDeleteUserButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delUser();
            }
        });

        serverView.getBanUserButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                banUser();
            }
        });

        serverView.getUnbanUserButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unbanUser();
            }
        });

        //JFRAME METHODS

        add(serverView.getServerPanel());

        pack();
        setTitle("Chat Server");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void stopServer() throws IOException {
        serverModel.stopServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverModel.startServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    private void addUser() {
        serverModel.addUser();
    }
    public void delUser() {
        serverModel.delUser();
    }
    public void banUser() {
        serverModel.banUser();
    }
    public void unbanUser() {
        serverModel.unbanUser();
    }
}
