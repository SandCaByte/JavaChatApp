import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Client {

    // Private Class Fields
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Consumer<String> onMessageReceived;

    // Constructor
    public Client(String address, int port, Consumer<String> onMessageReceived) throws IOException {

        // Set the fields
        this.socket = new Socket(address, port); // Connect to server
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Instantiate input
        this.out = new PrintWriter(socket.getOutputStream(), true); // Instantiate output
        this.onMessageReceived = onMessageReceived;

    }

    // Method for sending messages
    public void sendMessage(String msg) {

        out.println(msg); // Print the message

    }

    // Method for starting client
    public void startClient() {

        // Create a new thread
        new Thread(() -> {

            // Expect exceptions
            try {

                // Variable for accepting input
                String line;
                // Loop executes while the input is not null
                while ((line = in.readLine()) != null) {

                    // Accept the input
                    onMessageReceived.accept(line);

                }

            } catch (IOException e) { // Handle errors

                e.printStackTrace(); // Print errors

            }

        }).start(); // Start the client
    }

}
