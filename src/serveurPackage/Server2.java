package serveurPackage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {
        public static void main(String[] args) throws IOException {
            System.out.println("Je suis un serveur en attente de connexion…");

            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();
            System.out.println("Un client s'est connecté");

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            int x;
            do {
                x = dis.readInt();                    // réception de x
                System.out.println("Nombre reçu du client : " + x);

                if (x != 0) {
                    int resultat = x * 5;             // calcul x * 5
                    dos.writeInt(resultat);           // envoi du résultat
                    dos.flush();
                    System.out.println("Résultat envoyé au client : " + resultat);
                }
            } while (x != 0);

            System.out.println("Le client a envoyé 0, fermeture du serveur.");
            dis.close();
            dos.close();
            socket.close();
            serverSocket.close();
        }
    }




