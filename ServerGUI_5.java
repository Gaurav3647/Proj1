import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ServerGUI {
    // references for GUI components & networking streams
    private JFrame frame; // Main window for GUI
    private JTextArea chatArea; // TextArea to display chat msgs
    private JTextField inputField; // Text field for user input
    private PrintStream ps; // Stream to send msgs to the client
    private BufferedReader br1; // Bufferedreader to read incoming msgs from Client

    // Constructor to initialize the GUI components & start the server
    public ServerGUI() {

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

        // start the server to listen for client connection
        startServer();
    }

    // Method to start the server & handle incoming connections & msgs
    private void startServer() {
        try {
            // create a server socket listening on port 2100
            ServerSocket ssobj = new ServerSocket(2100);
            chatArea.append("Server is in Listening Mode at port 2100 \n");

            // Accept the incoming client connection
            Socket sobj = ssobj.accept();
            chatArea.append("Client & server connectipn is Successful \n");

            // Initialize input & output streams for communucation with the client
            br1 = new BufferedReader(new InputStreamReader(sobj.getInputStream())); // to read msgss from client

            ps = new PrintStream(sobj.getOutputStream()); // to send msgs to client

            // start reading msgs from the client continuously
            while (true) {
                String str1 = br1.readLine(); // read line from the client

                if (str1 != null) {
                    chatArea.append("Client : " + str1 + "\n"); // Display the msg from the client
                }

                // If the client sends "stop", break out of the loop & stop communication
                if (str1 != null && str1.equalsIgnoreCase("stop")) {
                    chatArea.append("Client has Stopped the communication \n");
                    break;
                }
            }

            // close all resources aftre communication ends
            br1.close();
            ps.close();
            sobj.close();
            ssobj.close();
        } catch (Exception e) {
            e.printStackTrace(); // print any errors that occur
        }
    }

    // method to send a msg from server to client
    private void sendMessage() {
        String message = inputField.getText(); // get the msg from the input field

        // display the msg in the chat area
        chatArea.append("Server : " + message + "\n");

        // send the msg to the client
        ps.println(message);

        // clear the input field
        inputField.setText(" ");

        // if the server sends the "stop" mesage, close the connection & exit the
        // program
        if (message.equalsIgnoreCase("stop")) {

            chatArea.append("Server has Stopped the connection \n");

            System.exit(0); // Exit the application
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {

        ServerGUI sobj = new ServerGUI();

        // create a new ServerGUI instanec , which initializes the server & the GUI.
    }
}