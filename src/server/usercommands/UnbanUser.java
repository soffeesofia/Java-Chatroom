package server.usercommands;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import server.ServerModel;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 * Unbans a user and updates their information in the UserList.xml file
 */
public class UnbanUser extends JFrame implements ActionListener {
    private final ServerModel serverModel;
    private JPanel panel;
    private ArrayList<JButton> buttons;
    private Document document;

    public UnbanUser(ServerModel serverModel){
        super("User List");
        this.serverModel = serverModel;
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new FlowLayout());
        buttons = new ArrayList<JButton>();
        add(panel, BorderLayout.CENTER);

        try {
            File file = new File("res/UserList.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            NodeList userList = document.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element user = (Element) userList.item(i);
                String isBanned = user.getElementsByTagName("isBanned").item(0).getTextContent();
                if (isBanned.equals("true")) {
                    // print out buttons
                    String username = user.getElementsByTagName("username").item(0).getTextContent();
                    JButton button = new JButton(username);
                    button.setActionCommand(username);
                    button.addActionListener(this);
                    panel.add(button);
                    buttons.add(button);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = e.getActionCommand();
        NodeList userList = document.getElementsByTagName("user");
        for (int i = 0; i < userList.getLength(); i++) {
            Element user = (Element) userList.item(i);
            String u = user.getElementsByTagName("username").item(0).getTextContent();
            if (u.equals(username)) {
                Element isBannedElement = (Element) user.getElementsByTagName("isBanned").item(0);
                isBannedElement.setTextContent("false");
                serverModel.addServerLog("Unbanned " +  username + " from the Chat" );
                break;
            }
        }
        try {
            // write the modified document back to the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("res/UserList.xml"));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
        // refresh the GUI with the modified XML document
        panel.removeAll();
        buttons.clear();
        NodeList updatedUserList = document.getElementsByTagName("user");
        for (int i = 0; i < updatedUserList.getLength(); i++) {
            Element user = (Element) updatedUserList.item(i);
            String isBanned = user.getElementsByTagName("isBanned").item(0).getTextContent();
            if (isBanned.equals("true")) {
                username = user.getElementsByTagName("username").item(0).getTextContent();
                JButton button = new JButton(username);
                button.setActionCommand(username);
                button.addActionListener(this);
                panel.add(button);
                buttons.add(button);
            }
        }
        panel.revalidate();
    }
}
