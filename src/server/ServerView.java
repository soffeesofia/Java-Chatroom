package server;

import javax.swing.*;

public class ServerView {
    private JPanel serverPanel;
    private JTextArea serverLogArea;
    private JTextArea onlineArea;
    private JButton startServerButton;
    private JButton stopServerButton;
    private JButton addUserButton;
    private JButton banUserButton;
    private JButton deleteUserButton;
    private JButton unbanUserButton;
    private ServerModel serverModel;

    public ServerView(ServerModel serverModel){
        this.serverModel = serverModel;
    }

    //GETTER METHODS
    public JButton getStartServerButton() {
        return startServerButton;
    }

    public JButton getStopServerButton() {
        return stopServerButton;
    }

    public JButton getAddUserButton() {
        return addUserButton;
    }

    public JButton getBanUserButton() {
        return banUserButton;
    }
    public JButton getUnbanUserButton() {
        return unbanUserButton;
    }

    public JButton getDeleteUserButton() {
        return deleteUserButton;
    }

    public JPanel getServerPanel() {
        return serverPanel;
    }

    /**
     * Updates the list of usernames under the Online Users text area
     */
    public void updateUsers() {
        for (String user: serverModel.getOnlineUsers()){
            onlineArea.removeAll();
            onlineArea.append(user);
        }
    }

    /**
     * Updates the Chat text area
     */
    public void updateServerOps(String message){
        serverLogArea.append(message+"\n");
    }
    //to disable other button when already pressed?
    public void disableStartServerButton(){
        getStopServerButton().setEnabled(true);
        getStartServerButton().setEnabled(false);
    }
    public void disableStopServerButton(){
        getStartServerButton().setEnabled(true);
        getStopServerButton().setEnabled(false);
    }

}
