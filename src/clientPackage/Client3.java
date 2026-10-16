package clientPackage;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client3 {

        public static void main(String[] args) throws IOException {
            System.out.println("Je suis un client pas encore connecté…");

            // IP de la machine SERVEUR (à remplacer, plus de "localhost")
            InetAddress ipServeur = InetAddress.getByName("127.0.0.1");
            int port = 1234;
            InetSocketAddress adresse = new InetSocketAddress(ipServeur, port);

            Socket socket = new Socket();
            socket.connect(adresse, 5000);            // délai max : 5 secondes
            System.out.println("Je suis un client connecté à " + ipServeur.getHostAddress() + ":" + port);

            Scanner scanner = new Scanner(System.in);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            int choix;
            do {
                System.out.println("\n===== CALCULATRICE =====");
                System.out.println("1. Addition");
                System.out.println("2. Soustraction");
                System.out.println("3. Multiplication");
                System.out.println("4. Division");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                if (choix >= 1 && choix <= 4) {
                    System.out.print("Entrez x : ");
                    int x = scanner.nextInt();
                    System.out.print("Entrez y : ");
                    int y = scanner.nextInt();

                    dos.writeInt(choix);
                    dos.writeInt(x);
                    dos.writeInt(y);
                    dos.flush();
                    System.out.println("Données envoyées au serveur");

                    boolean ok = dis.readBoolean();
                    if (ok) {
                        double resultat = dis.readDouble();
                        System.out.println("Résultat reçu du serveur : " + resultat);
                    } else {
                        System.out.println("Erreur : opération impossible (division par zéro ?)");
                    }
                } else if (choix == 0) {
                    dos.writeInt(0);
                    dos.flush();
                    System.out.println("Fin de la communication, fermeture du client.");
                } else {
                    System.out.println("Choix invalide, réessayez.");
                }
            } while (choix != 0);

            dis.close();
            dos.close();
            socket.close();
        }
    }

