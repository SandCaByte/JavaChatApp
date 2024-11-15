import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
public class ChatClientGUI extends JFrame {

    // private fields
    private JTextArea messageArea;
    private JTextField textField;
    private Client client;

    // Constructor
    public ChatClientGUI() {

        // Create a frame
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a text area for messages to be displayed
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Create a text field for inputting messages
        textField = new JTextField();

        // Prompt for a username
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name); // Set window title to include username

        // Listen for the enter key to be pressed
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Executes when action is performed

                // Format message to include date and sender
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": " + textField.getText();
                // Send the message
                client.sendMessage(message);
                // clear the text field
                textField.setText("");

            }
        });
        add(textField, BorderLayout.SOUTH);

        // Initialize exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {

            // Send a departure message to the server
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);

            // Delay to ensure the message is sent before exiting
            try {
                Thread.sleep(1000); // Wait for 1 second to ensure message is sent
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            // Exit the application
            System.exit(0);

        }); // Exit the application
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize and start the Client
        try {

            this.client = new Client("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();

        } catch (IOException e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);

        }
    }

    // Method for handling received messages
    private void onMessageReceived(String message) {

        // Display message in message area
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));

    }

    // Main method
    public static void main(String[] args) {

        //Display the frame
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });

    }
}