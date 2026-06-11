package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Manages the communication between the Client View and Client Model
 * Calls on the methods to be executed when a component is clicked/triggered in the Client View
 */
public class ClientController extends JFrame {

    private ClientModel clientModel;
    private ClientView clientView;

    public ClientController(){
        this.clientModel = new ClientModel();
        this.clientView = new ClientView(clientModel);

        clientModel.addObserver(clientView);

        //ACTION LISTENERS FOR CLIENT VIEW BUTTONS

        /**
         * Connect to the server and displays the Chatroom View
         */
        clientView.getConnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = clientView.getUName();
                String password = clientView.getPswd();
                String hostname = clientView.getHostname();
                if( clientView.getHostname().isBlank() && clientView.getUName().isBlank() && clientView.getPswd().isBlank()){
                    clientView.setErrorMessage("Please Fill In All Fields");
                } else {
                    try{
                        int result = clientModel.connect(username, password, hostname);
                        if ( result == 1){
                            clientModel.listenForMessages();
                            System.out.println("Changed to Chat Panel.");
                            clientView.showDM();
                            setTitle("Chatroom: " +username);
                            //clientModel.retrieveChatLogs(username);
                        } else if (result == 2){
                            clientView.setErrorMessage("User not Found.");
                        } else if (result == 3){
                            clientView.setErrorMessage("User is banned.");
                        }
                    } catch (IOException ex) {
                        clientView.setErrorMessage("Failed to connect to Server. Please try again.");
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
        /**
        * Disconnects from the server and displays the Login View
        */
        clientView.getLogoutButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientModel.disconnect();
                System.out.println("Logged out and returning to Login Panel.");
                clientView.showLogin();
            }
        });
        clientView.getLogoutButton2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientModel.disconnect();
                System.out.println("Logged out and returning to Login Panel.");
                clientView.showLogin();
            }
        });
        clientView.getLogoutButton3().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientModel.disconnect();
                System.out.println("Logged out and returning to Login Panel.");
                clientView.showLogin();
            }
        });
        /**
         * Sends the Message to the server using the method called as specified in the Client Model
         */
        clientView.getSendDMBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientView.getDMMessage();
                try {
                    clientModel.sendPrivateMessage(message,clientView.getDropdownDMValue());
                    clientView.getDMField().setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    clientModel.disconnect();
                    clientView.showLogin();
                    throw new RuntimeException(ex);
                }
                System.out.println("Sent Message");
            }
        });

        clientView.getSendBroadcastBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String broadcast = clientView.getBroadcastMessage();
                try {
                    clientModel.sendBroadcastMessage(broadcast,clientModel.getUsername());
                    clientView.getBroadcastField().setText("");
                    clientView.showLogin();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    clientModel.disconnect();
                    clientView.showLogin();
                    throw new RuntimeException(ex);
                }

            }
        });

        clientView.getSendGCBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientView.getGCMessage();
                try {
                    clientModel.sendGroupMessage(message,clientView.getDropdownGCValue());
                    clientView.getGCField().setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    clientModel.disconnect();
                    clientView.showLogin();
                    throw new RuntimeException(ex);
                }
                System.out.println("Sent Message");
            }
        });

        clientView.getMakeNewGCBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = clientView.getGcName();
                String memListDisplay = clientView.getMemberListDisplayValue();
                ArrayList<String> groupMembers = new ArrayList<>(Arrays.asList(memListDisplay.split("\n")));
                try {
                    clientModel.createNewGC(groupName, groupMembers);
                    clientView.getGcNameField().setText("");
                    clientView.getMemberListDisplay().setText("");
                    JOptionPane.showMessageDialog(clientView.getClientPanel(), "Created New Group");
                    clientView.showDM();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        clientView.getBookmarkBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toBkmrk = clientView.getToBookmarkListValue();
                ArrayList<String> toBkmrkList = new ArrayList<>(Arrays.asList(toBkmrk.split("\n")));
                try {
                    clientModel.addBookmark(toBkmrkList);
                    JOptionPane.showMessageDialog(clientView.getClientPanel(), "Created New Group");
                    clientView.showDM();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /**
         * Displays the panel according to button pressed
         */
        clientView.getToDMBtn1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showDM();
            }
        });
        clientView.getToDMBtn2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showDM();
            }
        });

        clientView.getToGCBtn1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showGC();
            }
        });
        clientView.getToGCBtn2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showGC();
            }
        });

        clientView.getToBroadcastBtn1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showBroadcast();
            }
        });
        clientView.getToBroadcastBtn2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showBroadcast();
            }
        });

        clientView.getCreateGCBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showCreateGC();
            }
        });

        clientView.getBookmarkBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientView.showBookmark();
            }
        });

        /**
         *  Retrieves the selected item from the dropdown menus
         */
        clientView.getDropdownDM().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String selectedUser = clientView.getDropdownDMValue();
                    clientView.refreshDMTextArea();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        clientView.initialUpdateTextArea(clientModel.getPmList(), selectedUser);
                    }
                }, 0, 1000);
                System.out.println(selectedUser);
            }
        });

        clientView.getDropdownGC().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGroup = clientView.getDropdownGCValue();
                clientView.refreshGCTextArea();
                clientView.groupUpdateTextArea(clientModel.getGcList(), selectedGroup);
                System.out.println(selectedGroup);
            }
        });
        clientView.getUserListGCDropdown().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientView.getMemberListDisplayValue().contains(clientView.getUserListGCDropdownValue())){
                    String selectedUser = clientView.getUserListGCDropdownValue();
                    clientView.getMemberListDisplay().append(selectedUser+"\n");
                }

            }
        });

        clientView.getBookmarkDropdown().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(clientView.getToBookmarkListValue().contains(clientView.getBookDropdownValue())){
                    String selctedUser = clientView.getBookDropdownValue();
                    clientView.getToBookmarkList().append(selctedUser+"\n");
                }

            }
        });


        /**
         * Adds the panels into a JFrame to display it
         */
        add(clientView.getClientPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new ClientController();
    }

}
