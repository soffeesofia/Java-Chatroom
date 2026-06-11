package client;

import object.GroupMessage;
import object.PrivateMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClientView {
    private JPanel clientPanel;
    private JPanel loginPanel;
    private JLabel hostname;
    private JLabel username;
    private JLabel password;
    private JTextField hostnameField;
    private JTextField uNameField;
    private JPasswordField pswdField;
    private JLabel errorMsg;
    private JButton connectButton;
    private JPanel privateChatPanel;
    private JLabel dmRoom;
    private JTextArea dmTextArea;
    private JComboBox dropdownDM;
    private JTextField fieldDM;
    private JButton sendDMBtn;
    private JButton toBroadcastBtn1;
    private JButton toGCBtn1;
    private JButton logoutBtn1;
    private JPanel groupPanel;

    private JComboBox dropdownGC;
    private JTextField fieldGC;
    private JButton sendGCBtn;
    private JButton logoutBtn2;
    private JButton toBroadcastBtn2;
    private JTextArea gcTextArea;
    private JLabel gcRoom;
    private JButton toDMBtn1;
    private JPanel addNewGroupPanel;
    private JButton createGCBtn;
    private JComboBox userListGCDropdown;
    private JTextArea memberListDisplay;
    private JTextField gcNameField;
    private JButton makeNewGCBtn;
    private JPanel broadcastPanel;
    private JTextField broadcastField;
    private JButton sendBroadcastBtn;
    private JButton toDMBtn2;
    private JButton toGCBtn2;
    private JButton logoutBtn3;
    private JButton bookmarkBtn;
    private JPanel bookmarkPanel;
    private JComboBox bookmarkDropdown;
    private JButton addBookmarBtn;
    private JLabel bookmarkMsg;
    private JTextArea toBookmarkList;
    private ClientModel clientModel;

    //References Client Model to display chat messages
    public  ClientView(ClientModel clientModel){
        this.clientModel = clientModel;
    }

    // Getter Methods for GUI Components
    public JPanel getClientPanel() {
        return clientPanel;
    }
    public JComboBox getDropdownDM() {
        return dropdownDM;
    }
    public String getDropdownDMValue() {
            return dropdownDM.getSelectedItem().toString();
    }
    public JComboBox getDropdownGC() {
        return dropdownGC;
    }
    public String getDropdownGCValue() {
        return getDropdownGC().getSelectedItem().toString();
    }
    public String getHostname() {
        return hostnameField.getText();
    }
    public String getUName() {
        return uNameField.getText();
    }
    public String getPswd() {
        return pswdField.getText();
    }
    public JButton getConnectButton() {
        return connectButton;
    }
    public JButton getLogoutButton1() {
        return logoutBtn1;
    }
    public JButton getLogoutButton2() {
        return logoutBtn2;
    }
    public JButton getLogoutButton3() {
        return logoutBtn3;
    }
    public JButton getToBroadcastBtn1() {
        return toBroadcastBtn1;
    }
    public JButton getToBroadcastBtn2() {
        return toBroadcastBtn2;
    }
    public JButton getToDMBtn1() {
        return toDMBtn1;
    }
    public JButton getSendBroadcastBtn(){
        return sendBroadcastBtn;
    }
    public JButton getToDMBtn2() {
        return toDMBtn2;
    }
    public JButton getToGCBtn1(){
        return toGCBtn1;
    }
    public JButton getToGCBtn2(){
        return toGCBtn2;
    }
    public JButton getSendDMBtn() {
        return sendDMBtn;
    }
    public String getDMMessage(){
        return fieldDM.getText();
    }
    public String getBroadcastMessage(){
        return broadcastField.getText();
    }
    public JTextField getDMField() {
        return fieldDM;
    }
    public JTextField getBroadcastField() {
        return broadcastField;
    }
    public String getGCMessage(){
        return fieldGC.getText();
    }
    public JTextField getGCField() {
        return fieldGC;
    }
    public JButton getSendGCBtn() {
        return sendGCBtn;
    }
    public JTextField getGcNameField(){
        return gcNameField;
    }
    public JButton getCreateGCBtn(){
        return createGCBtn;
    }
    public String getGcName(){
        return getGcNameField().getText();
    }
    public JComboBox getUserListGCDropdown(){
        return userListGCDropdown;
    }
    public String getUserListGCDropdownValue(){
        return getUserListGCDropdown().getSelectedItem().toString();
    }
    public JTextArea getMemberListDisplay(){
        return memberListDisplay;
    }
    public String getMemberListDisplayValue(){
        return getMemberListDisplay().getText();
    }
    public JButton getMakeNewGCBtn(){
        return makeNewGCBtn;
    }
    public JButton getBookmarkBtn(){
        return bookmarkBtn;
    }
    public JComboBox getBookmarkDropdown(){
        return bookmarkDropdown;
    }
    public String getBookDropdownValue(){
        return bookmarkDropdown.getSelectedItem().toString();
    }
    public JButton getAddBookmarkBtn(){
        return addBookmarBtn;
    }

    public JTextArea getToBookmarkList(){
        return toBookmarkList;
    }
    public String getToBookmarkListValue(){
        return getToBookmarkList().getText();
    }
    public void showDM() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "private");
    }
    public void showLogin() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "login");
    }
    public void showGC() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "group");
    }
    public void showBroadcast() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "broadcast");
    }
    public void showCreateGC() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "createGC");
    }
    public void showBookmark() {
        CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
        cardLayout.show(clientPanel, "bookmark");
    }
    // Methods for Updating the Client View
    public void updateDMDropdown(ArrayList<String> list) {
        for (String item: list) {
            dropdownDM.addItem(item);
        }
    }
    public void updateGCDropdown(ArrayList<String> list) {
        for (String item: list) {
            dropdownGC.addItem(item);
        }

    }
    public void updateUserListDropdown(ArrayList<String> list) {
        for (String item: list) {
            userListGCDropdown.addItem(item);
        }
    }

    public void updateBookmarkDropdown(ArrayList<String> list) {
        for (String item: list) {
            bookmarkDropdown.addItem(item);
        }
    }
    public void updateDMDropdownWithBK(ArrayList<String> list) {
        dropdownDM.removeAllItems();
        for (String item: list) {
            dropdownDM.addItem(item);
        }
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMsg.setText(errorMessage);
    }
    public void initialUpdateTextArea(ArrayList<PrivateMessage> pms, String dropDownSelected) {
        for (int j = 0 ;j < pms.size(); j++) {
            String sender = pms.get(j).getSender();
            String time = pms.get(j).getTime();
            String text = pms.get(j).getMessage();
            String recipient = pms.get(j).getRecipient();
            String message = "<" +time+ ">" +sender+ ": " +text+"\n";

            // check if the message has already been appended
            if (!dmTextArea.getText().contains(message)) {
                if (sender.equals(dropDownSelected) && recipient.equals(clientModel.getUsername())) {
                    dmTextArea.append(message);
                } else if (sender.equals(clientModel.getUsername()) && recipient.equals(dropDownSelected)) {
                    dmTextArea.append(message);
                }
            }
        }
    }
    public void groupUpdateTextArea(ArrayList<GroupMessage> pms, String dropDownSelected) {
        for (int j = 0; j < pms.size(); j++) {
            String group = pms.get(j).getGroupName();
            String sender = pms.get(j).getSender();
            String time = pms.get(j).getTime();
            String text = pms.get(j).getMessage();
            String message = "<" + time + ">" + sender + ": " + text + "\n";

            if (!gcTextArea.getText().contains(message)) {
                if (group.equals(dropDownSelected)) {
                    gcTextArea.append(message);
                }

            }
        }
    }
    public void refreshDMTextArea(){
        dmTextArea.setText("");
    }
    public void refreshGCTextArea(){
        gcTextArea.setText("");
    }


}
