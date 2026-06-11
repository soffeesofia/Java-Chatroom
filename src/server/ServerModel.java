package server;

import object.Group;
import object.GroupMessage;
import object.PrivateMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.usercommands.AddUser;
import server.usercommands.BanUser;
import server.usercommands.DeleteUser;
import server.usercommands.UnbanUser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Manages the writing of messages and user information into XML files
 */
public class ServerModel {
    private ArrayList<String> onlineUsers = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket clientConnect;
    private ServerView serverView;
    final int PORT = 9000;

    /**
     * Starts the server and accepts client connections
     * Once a client is connected, thread of the ClientHandler will start
     */
    public void startServer() throws IOException, ClassNotFoundException {
        serverSocket = new ServerSocket(PORT);
        serverView.updateServerOps("Server has started on port: " +PORT);
        while(true) {
            clientConnect = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientConnect, this);
            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
        }
    }

    public void stopServer() throws IOException {
        serverSocket.close();
    }

    public void addOnlineUser(String username){
        onlineUsers.add(username+"\n");
        serverView.updateUsers();
    }

    public void removeOnlineUser(String username){
        onlineUsers.remove(username+"\n");
        serverView.updateUsers();
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * Displays a String of a server action in to the text area of the Server View
     */
    public void addServerLog(String s) {
        serverView.updateServerOps(s);
    }

    /**
     * Notifies the Server View that a new Message has been received
     */
    public void addObserver(ServerView serverView){
        this.serverView = serverView;
    }

    /**
     * References the UserList.xml file to verify a user's login. Returns an int value to the client
     */
    public int verifyUser(String username, String password) {
        try {
            File xmlFile = new File("res/UserList.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                Element user = (Element) nList.item(i);
                String xmlUsername = user.getElementsByTagName("username").item(0).getTextContent();
                String xmlPassword = user.getElementsByTagName("pswd").item(0).getTextContent();
                String xmlIsBanned = user.getElementsByTagName("isBanned").item(0).getTextContent();
                if (xmlIsBanned.equals("true")){
                    return 3;
                }
                if (username.equals(xmlUsername) && password.equals(xmlPassword)) {
                    if (!onlineUsers.contains(username) || !onlineUsers.contains(password)){
                        //addOnlineUser(username);
                        return 1;
                    } else {
                        return 2;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4;
    }

    /**
     * Checks if the user currently has any bookmarked users
     */
    public boolean checkIfBkmrk(String uName) {
        try {
            // Parse the XML file using DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("res/UserBookmarks.xml");

            // Get the list of "bkmrk" elements
            NodeList bkmrkList = doc.getElementsByTagName("bkmrk");

            // Iterate over each "bkmrk" element and check if the "username" value is "Marshall"
            for (int i = 0; i < bkmrkList.getLength(); i++) {
                Element bkmrk = (Element) bkmrkList.item(i);
                String username = bkmrk.getElementsByTagName("username").item(0).getTextContent();
                if (username.equals(uName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a String ArrayList of current users of the program
     */
    public ArrayList<String> retrieveListOfUsers(){
        ArrayList<String> usernames = new ArrayList<>();
        try {
            File file = new File("res/UserList.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++){
                Element user = (Element) nodeList.item(i);
                String username = user.getElementsByTagName("username").item(0).getTextContent();
                usernames.add(username);
                System.out.println(username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usernames;
    }

    /**
     * Creates a Vector list of current group chats in the program
     */
    public Vector<String> retrieveListOfGroup(String username) {
        Vector<String> groups = new Vector<String>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File file = new File("res/GroupList.xml");
            Document doc = dBuilder.parse(file);
            Element root = doc.getDocumentElement();
            NodeList groupNodes = root.getElementsByTagName("group");
            for (int i = 0; i < groupNodes.getLength(); i++) {
                Element groupElement = (Element) groupNodes.item(i);
                NodeList usernameNodes = groupElement.getElementsByTagName("username");
                for (int j = 0; j < usernameNodes.getLength(); j++) {
                    String currentUsername = usernameNodes.item(j).getTextContent();
                    if (currentUsername.equals(username)) {
                        String groupName = groupElement.getElementsByTagName("name").item(0).getTextContent();
                        groups.add(groupName);
                        break;
                    }
                }
            }
            return groups;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a String ArrayList that displays the bookmarked contacts of a user first
     */
    public ArrayList<String> retrieveNewUserList(String username){
        ArrayList<String> userTemp = new ArrayList<>();
        userTemp.add(username);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("bookmarks.xml"));

            Element root = document.getDocumentElement();

            NodeList bookmarkNodes = root.getElementsByTagName("bkmrk");
            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < bookmarkNodes.getLength(); i++) {
                Element bookmark = (Element) bookmarkNodes.item(i);
                NodeList usernameNodes = bookmark.getElementsByTagName("username");
                String uName = usernameNodes.item(0).getTextContent();
                if (uName.equals(username)) {
                    NodeList nameNodes = bookmark.getElementsByTagName("name");
                    for (int j = 0; j < nameNodes.getLength(); j++) {
                        String name = nameNodes.item(j).getTextContent();
                        userTemp.add(name);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> temp = retrieveListOfUsers();
        for (String name : userTemp) {
            temp.removeIf(e -> e.equals(name));
        }
        userTemp.addAll(temp);
        return userTemp;
    }

    /**
     * Writes the content of a Private Message object into an XML document
     */
    public synchronized void addPrivateMessagetoXML(Object obj) {
        PrivateMessage pm = (PrivateMessage) obj;
        String sender = pm.getSender();
        String content = pm.getMessage();
        String time = pm.getTime();
        String recipient = pm.getRecipient();

        try {
            //APPEND IT
            File xmlFile = new File("res/PrivateChatLog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            Element messageElement = doc.createElement("message");
            Element textElement = doc.createElement("text");
            Element timeElement = doc.createElement("time");
            Element senderElement = doc.createElement("sender");
            Element recipientElement = doc.createElement("recipient");
            textElement.appendChild(doc.createTextNode(content));
            timeElement.appendChild(doc.createTextNode(time));
            senderElement.appendChild(doc.createTextNode(sender));
            recipientElement.appendChild(doc.createTextNode(recipient));

            messageElement.appendChild(textElement);
            messageElement.appendChild(timeElement);
            messageElement.appendChild(senderElement);
            messageElement.appendChild(recipientElement);

            root.appendChild(messageElement);

            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            doc.getDocumentElement().normalize();


        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Writes the content of a Group Message object into an XML document
     */
    public synchronized void addGroupMessagetoXML(Object obj) {
        GroupMessage pm = (GroupMessage) obj;
        String groupname = pm.getGroupName();
        String sender = pm.getSender();
        String content = pm.getMessage();
        String time = pm.getTime();

        try {
            //APPEND IT
            File xmlFile = new File("res/GroupChatLog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            Element groupNameElement = doc.createElement("groupname");
            Element messageElement = doc.createElement("message");
            Element textElement = doc.createElement("text");
            Element timeElement = doc.createElement("time");
            Element senderElement = doc.createElement("sender");
            groupNameElement.appendChild(doc.createTextNode(groupname));
            textElement.appendChild(doc.createTextNode(content));
            timeElement.appendChild(doc.createTextNode(time));
            senderElement.appendChild(doc.createTextNode(sender));

            messageElement.appendChild(groupNameElement);
            messageElement.appendChild(textElement);
            messageElement.appendChild(timeElement);
            messageElement.appendChild(senderElement);

            root.appendChild(messageElement);

            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            doc.getDocumentElement().normalize();
            } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Writes the information of a group chat into an XML file
     */
    public synchronized void addNewGC(Group newGC) {
        String groupName = newGC.getGroupName();
        ArrayList<String> groupMembers = newGC.getMembers();
        try{
            File xmlFile = new File("res/GroupList.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            Element newGroup = doc.createElement("group");
            Element gcName = doc.createElement("name");
            gcName.appendChild(doc.createTextNode(groupName));
            newGroup.appendChild(gcName);

            Element members = doc.createElement("members");
            for (String name : groupMembers) {
                Element username = doc.createElement("username");
                username.appendChild(doc.createTextNode(name));
                members.appendChild(username);
            }
            newGroup.appendChild(members);
            root.appendChild(newGroup);

            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            doc.getDocumentElement().normalize();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Writes the bookmarked contacts of a user in an XML file
     */
    public synchronized void addNewBkmrk(String username, ArrayList<String> usersToBkmrk){
        try{
            File xmlFile = new File("res/UserBookmarks.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            Element newBkmrk = doc.createElement("bkmrk");
            Element uName = doc.createElement("username");
            uName.appendChild(doc.createTextNode(username));
            newBkmrk.appendChild(uName);

            Element bookmarked = doc.createElement("contacts");
            for (String name : usersToBkmrk) {
                Element userToBkmrk = doc.createElement("name");
                userToBkmrk.appendChild(doc.createTextNode(name));
                bookmarked.appendChild(userToBkmrk);
            }
            newBkmrk.appendChild(bookmarked);
            root.appendChild(newBkmrk);

            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            doc.getDocumentElement().normalize();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the current private messages stored in PrivateChatLog.xml
     * and adds them into an ArrayList of Private Messages
     */
    public ArrayList<PrivateMessage> getDMChatLogs(){
        ArrayList<PrivateMessage> pms = new ArrayList<>();
        try {
            File xmlFile = new File("res/PrivateChatLog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("message");
            for (int i = 0; i<nodeList.getLength(); i++){
                Element message = (Element) nodeList.item(i);
                String xmlMessage = message.getElementsByTagName("text").item(0).getTextContent();
                String xmlTime = message.getElementsByTagName("time").item(0).getTextContent();
                String xmlSender = message.getElementsByTagName("sender").item(0).getTextContent();
                String xmlRecipient = message.getElementsByTagName("recipient").item(0).getTextContent();

                pms.add(new PrivateMessage(xmlSender, xmlRecipient, xmlMessage,xmlTime));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return pms;
    }

    /**
     * Retrieves the current group messages stored in GroupChatLog.xml
     * and adds them into an ArrayList of Group Messages
     */
    public ArrayList<GroupMessage> getGroupChatLogs(){
        ArrayList<GroupMessage> pms = new ArrayList<>();
        try {
            File xmlFile = new File("res/GroupChatLog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("message");
            for (int i = 0; i<nodeList.getLength(); i++){
                Element message = (Element) nodeList.item(i);
                String xmlGroupname = message.getElementsByTagName("groupname").item(0).getTextContent();
                String xmlMessage = message.getElementsByTagName("text").item(0).getTextContent();
                String xmlTime = message.getElementsByTagName("time").item(0).getTextContent();
                String xmlSender = message.getElementsByTagName("sender").item(0).getTextContent();

                pms.add(new GroupMessage(xmlGroupname, xmlSender, xmlMessage,xmlTime));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return pms;
    }

    /**
     * Calls the classes that maniulates user information
     */
    public void addUser() {
        new AddUser(this);
    }
    public void delUser() {
        new DeleteUser(this);
    }
    public void banUser() {
        new BanUser(this);
    }
    public void unbanUser() {
        new UnbanUser(this);
    }



}

