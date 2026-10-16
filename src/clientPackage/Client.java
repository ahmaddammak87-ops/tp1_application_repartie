package clientPackage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Je suis un client pas encore connecté…");
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Je suis un client connecté");
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(x);
        System.out.println("Nombre envoyé au serveur : " + x);

        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        int resultat = dis.readInt();
        System.out.println("Résultat reçu du serveur : " + resultat);

        dis.close();
        dos.close();
        socket.close();




    }
}
