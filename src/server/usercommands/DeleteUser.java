package server.usercommands;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import server.ServerModel;

/**
 * Deletes a user and their information in the UserList.xml file
 */
public class DeleteUser extends JFrame implements ActionListener {
    private final ServerModel serverModel;
    private JPanel panel;
    private JComboBox<String> userDropdown;
    private Document document;

    public DeleteUser(ServerModel serverModel){
        super("User List");
        this.serverModel = serverModel;
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        userDropdown = new JComboBox<String>();
        panel.add(userDropdown, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(this);
        panel.add(deleteButton, BorderLayout.SOUTH);

        try {
            File file = new File("res/UserList.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            NodeList userList = document.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element user = (Element) userList.item(i);
                String username = user.getElementsByTagName("username").item(0).getTextContent();
                userDropdown.addItem(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = (String) userDropdown.getSelectedItem();
        NodeList userList = document.getElementsByTagName("user");
        for (int i = 0; i < userList.getLength(); i++) {
            Element user = (Element) userList.item(i);
            String u = user.getElementsByTagName("username").item(0).getTextContent();
            if (u.equals(username)) {
                user.getParentNode().removeChild(user);
                try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(document);
                    StreamResult result = new StreamResult("res/UserList.xml");
                    transformer.transform(source, result);
                    serverModel.addServerLog("Deleted " +  username + " from the Chat" );

                } catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            }
        }

        //to update dropdown after deleting
        userDropdown.removeAllItems();
        NodeList newUserList = document.getElementsByTagName("user");
        for (int i = 0; i < newUserList.getLength(); i++) {
            Element user = (Element) newUserList.item(i);
            String u = user.getElementsByTagName("username").item(0).getTextContent();
            userDropdown.addItem(u);
        }
    }
}
