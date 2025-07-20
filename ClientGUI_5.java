import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class ClientGUI {
    // references for GUI components & networking streams
    private JFrame frame; // Main window for GUI
    private JTextArea chatArea; // TextArea to display chat msgs
    private JTextField inputField; // Text field for user input
    private PrintStream ps; // Stream to send msgs to the client
    private BufferedReader br1; // Bufferedreader to read incoming msgs from Client

    public string str= "sangvi";

    // Constructor to initialize the GUI components & start the server
    public ClientGUI() {

        // Intitialize JFrame for the main window
        frame = new JFrame("Server - Wrio Code Chat Messenger");
        frame.setSize(600, 600); // set size of window

        // Exit the Program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Innitialize chat area (non-editable text area for displaying msgs)
        chatArea = new JTextArea();

        // The chat area should not be editable by user
        chatArea.setEditable(false);

        /// wrap the chat area with a scrollpane for better UI
        JScrollPane scrollpane = new JScrollPane(chatArea);

        // Add the ScrollPane to the centre of the frame
        frame.add(scrollpane, BorderLayout.CENTER);

        // Initialize the input field for sending msgs
        inputField = new JTextField();

        // Event Handler for field
        inputField.addActionListener(new ActionListener() {
            // Action listener to send msg when the user press Enter
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add the input field to bottom of the frame
        frame.add(inputField, BorderLayout.SOUTH);

        // make the window visible
        frame.setVisible(true);

        // Connect to the Server (calls the method below)
        connectToServer();
    }

    // Method to connect to the server & handle incoming connections & msgs
    private void connectToServer() {
        try {

            // Establish a socket connection to Server on localhost & port 2100
            Socket sobj = new Socket("localhost", 2100);
            chatArea.append("Client is connected to the server\n");

            // Initialize input & output streams for communucation with the client
            br1 = new BufferedReader(new InputStreamReader(sobj.getInputStream())); // to read msgs from the Server

            ps = new PrintStream(sobj.getOutputStream()); // to send msgs to client

            // start reading msgs from the Server continuously
            while (true) {
                String str2 = br1.readLine(); // read line from the Server

                if (str2 != null) {
                    chatArea.append("Client : " + str2 + "\n"); // Display the msg from the Server
                }

                // If the Server sends "stop", break out of the loop & stop communication
                if (str2 != null && str2.equalsIgnoreCase("stop")) {
                    chatArea.append("Server has Stopped the communication \n");
                    break;
                }
            }

            // close all resources aftre communication ends
            br1.close();
            ps.close();
            sobj.close();

        } catch (Exception e) {
            e.printStackTrace(); // print any errors that occur
        }
    }

    // method to send a msg from client to server
    private void sendMessage() {
        String message = inputField.getText(); // get the msg from the input field

        // display the msg in the chat area
        chatArea.append("Server : " + message + "\n");

        // send the msg to the Server
        ps.println(message);

        // clear the input field
        inputField.setText(" ");

        // if the client sends the "stop" mesage, close the connection & exit the
        // program
        if (message.equalsIgnoreCase("stop")) {

            chatArea.append("Client has Stopped the connection \n");

            System.exit(0); // Exit the application
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {

        ClientGUI cobj = new ClientGUI();

        // create a new ServerGUI instanec , which initializes the server & the GUI.
    }
}
