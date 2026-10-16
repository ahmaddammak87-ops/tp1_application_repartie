package clientPackage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client2 {



    public static void main(String[] args) throws IOException {
        System.out.println("Je suis un client pas encore connecté…");

        Socket socket = new Socket("localhost", 1234);
        System.out.println("Je suis un client connecté");

        Scanner scanner = new Scanner(System.in);
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        int x;
        do {
               System.out.print("Entrez un entier (0 pour quitter) : ");
               x = scanner.nextInt();

               dos.writeInt(x);                      // envoi de x au serveur
            dos.flush();
                System.out.println("Nombre envoyé au serveur : " + x);

                if (x != 0) {
                    int xx = dis.readInt();           // réception du résultat
                    System.out.println("Résultat reçu du serveur : " + x + " * 5 = " + xx);
                }
            } while (x != 0);

            System.out.println("Fin de la communication, fermeture du client.");
            dis.close();
            dos.close();
            socket.close();
        }
}
