package serveurPackage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Je suis un serveur en attente la connexion d'un client ");

        Socket socket = serverSocket.accept();
        System.out.println("un client est connecté ");

        InputStream is =socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);


        socket.close();
        serverSocket.close();
    }
}