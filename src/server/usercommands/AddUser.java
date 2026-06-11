package server.usercommands;

import object.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import server.ServerModel;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Adds a user and writes their credentials in the UserList.xml file
 */
public class AddUser implements ActionListener {
    private JFrame frame;
    private JButton addButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    ServerModel serverModel;


    public AddUser(ServerModel serverModel){
        this.serverModel = serverModel;
        frame = new JFrame("Add User");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        panel.add(new JLabel(""));
        addButton = new JButton("Add User");
        addButton.addActionListener(this);
        panel.add(addButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void addUser(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = new User(username, password);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse("res/UserList.xml");
            Element root = document.getDocumentElement();

            NodeList existingUsers = root.getElementsByTagName("user");
            for (int i = 0; i < existingUsers.getLength(); i++) {
                Element existingUser = (Element) existingUsers.item(i);
                String existingUsername = existingUser.getElementsByTagName("username").item(0).getTextContent();
                if (existingUsername.equals(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists");
                    return;
                }
            }

            Element userElement = document.createElement("user");
            Element usernameElement = document.createElement("username");
            usernameElement.setTextContent(user.getUsername());
            userElement.appendChild(usernameElement);

            Element passwordElement = document.createElement("pswd");
            passwordElement.setTextContent(user.getPassword());
            userElement.appendChild(passwordElement);

            Element isBannedElement = document.createElement("isBanned");
            isBannedElement.setTextContent("false");
            userElement.appendChild(isBannedElement);

            root.appendChild(userElement);

            // Write XML to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            File file = new File("res/UserList.xml");
            StreamResult result = new StreamResult(file);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            // Display success message
            JOptionPane.showMessageDialog(null, "User added successfully");
            serverModel.addServerLog("Added " +  user.getUsername() + " to the Chat");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding user");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addButton)){
            addUser();
            frame.dispose();
        }
    }
}