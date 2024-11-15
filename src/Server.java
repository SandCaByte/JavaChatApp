import java.io.*;
import java.net.*;
import java.util.*;
public class Server {

    // List to keep track of all connected clients
    private static List<ClientHandler> clients = new ArrayList<>();

    // Main Method
    public static void main(String[] args) throws IOException {

        // Start the server on port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        // Print message confirming the server is started
        System.out.println("Server started. Waiting for clients...");

        while (true) {

            // Accept clients to connect
            Socket clientSocket = serverSocket.accept();
            // Print message confirming client connection
            System.out.println("Client connected.");

            // Spawn a new thread for each client
            ClientHandler clientThread = new ClientHandler(clientSocket, clients); // Instantiate a new client thread
            clients.add(clientThread); // Add the client thread to the list of clients
            new Thread(clientThread).start(); // Start the thread

        }

    }

}

// Client Handler class
class ClientHandler implements Runnable {

    // Private Class Fields
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor
    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {

        // Set the fields
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true); // Auto flush set to true
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    public void run() {

        try {

            // Input Variable
            String inputLine;
            // execute loop while accepting input
            while ((inputLine = in.readLine()) != null) {

                // Broadcast message to all clients
                for (ClientHandler aClient : clients) {
                    aClient.out.println(inputLine); // Print the message on other clients
                }

            }

        } catch (IOException e) { // Handle exception

            System.out.println("An error occurred: " + e.getMessage()); // Print the error message

        } finally { // Execute regardless of if an exception was thrown or not

            try {

                //Close the print writer, buffered reader, and socket
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) { // Handle exception

                e.printStackTrace();

            }

        }

    }

}
