# TP1-DAR2026 : Programmation par Sockets en Java

Ce projet présente trois activités de communication **Client/Serveur** en Java avec les sockets TCP.

---

## Structure du projet

```
TP1-DAR2026/
├── src/
│   ├── clientPackage/
│   │   ├── Client.java      # Activité 1
│   │   ├── Client2.java     # Activité 2
│   │   └── Client3.java     # Activité 3
│   ├── serveurPackage/
│   │   ├── Server.java      # Activité 1
│   │   ├── Server2.java     # Activité 2
│   │   └── Server3.java     # Activité 3
│   └── Main.java
├── captures/                # Captures d'écran utilisées dans ce README
└── README.md
```

| Activité | Client | Serveur | Objectif |
|----------|--------|---------|----------|
| 1 | `Client` | `Server` | Établir une première connexion |
| 2 | `Client2` | `Server2` | Calculer `x * 5` côté serveur, avec boucle jusqu'à 0 |
| 3 | `Client3` | `Server3` | Fonctionner sur deux machines, avec une calculatrice à 4 opérations |

---

## Notions utilisées

| Classe | Rôle |
|--------|------|
| `ServerSocket` | Côté serveur : écoute sur un port et attend les clients (`accept()`) |
| `Socket` | Canal de communication entre le client et le serveur |
| `DataOutputStream` | Envoie des types primitifs (`writeInt`, `writeDouble`, `writeBoolean`) |
| `DataInputStream` | Reçoit des types primitifs (`readInt`, `readDouble`, `readBoolean`) |
| `InetAddress` | Représente une adresse IP |
| `InetSocketAddress` | Représente le couple adresse IP + port |
| `Scanner` | Lit les saisies au clavier |

**Principe général :**
1. Le serveur démarre et attend (`accept()`).
2. Le client se connecte (`new Socket(ip, port)`).
3. Les deux échangent des données via les flux d'entrée et de sortie.
4. Les flux et les sockets sont fermés à la fin.

**Règle importante :** l'ordre des écritures du client doit correspondre exactement à l'ordre des lectures du serveur (et inversement). Sinon, les données sont mal interprétées ou le programme reste bloqué.

---

## Activité 1 : Premier échange Client/Serveur

Le serveur écoute sur le port **1234**. Le client s'y connecte via `localhost`.


![Activité 1]
<img width="400" height="200" alt="image" src="https://github.com/user-attachments/assets/2f571877-b23c-4a14-8c64-687b12b4c758" />
<img width="500" height="742" alt="image" src="https://github.com/user-attachments/assets/37c8b19c-fe89-4541-9d2c-c7851a3a3380" />
resultat :<img width="500" height="637" alt="image" src="https://github.com/user-attachments/assets/cfcb96c9-a028-4a8d-848c-8ca7e298c603" />



---

## Activité 2 : Service de multiplication

### Étape 1 : Préparation de l'espace de travail
Création du projet IntelliJ avec les packages `clientPackage` et `serveurPackage`.

![Espace de travail] <img width="500" height="907" alt="image" src="https://github.com/user-attachments/assets/0a8c7b7c-ae5f-47b2-9645-5bc51e99669e" />
<img width="500" height="940" alt="image" src="https://github.com/user-attachments/assets/ceec3bd5-c186-45b4-9e57-0a4446a31863" />



### Étape 2 : Fonctionnement

1. Le client saisit un entier `x` au clavier.
2. Il l'envoie au serveur avec `dos.writeInt(x)`.
3. Le serveur le reçoit avec `dis.readInt()`.
4. Le serveur calcule `x * 5`.
5. Le serveur renvoie le résultat avec `dos.writeInt(resultat)`.
6. Le client reçoit et affiche le résultat.

**Pourquoi `DataInputStream` et `DataOutputStream` ?**
`writeInt` envoie l'entier sur **4 octets** (complément à deux). Les nombres négatifs sont donc transmis sans perte. Avec `OutputStream.write(int)`, un seul octet est envoyé et la valeur est tronquée.

**Boucle de saisie :** le client répète l'opération tant que l'entier saisi est différent de 0. Quand l'utilisateur saisit **0** :
- le client envoie 0 puis ferme ses flux et son socket ;
- le serveur lit 0, sort de sa boucle et ferme ses flux, son socket et son `ServerSocket`.

### Tests

| x saisi | Résultat affiché | Interprétation |
|---------|------------------|----------------|
| 5 | 25 | Cas positif |
| -3 | -15 | Le signe est conservé |
| 0 | (fin) | Arrêt propre de la communication |

![Tests] <img width="500" height="440" alt="image" src="https://github.com/user-attachments/assets/6e628318-1131-4075-b2aa-fb06e23c1837" />

![Fermeture]<img width="500" height="437" alt="image" src="https://github.com/user-attachments/assets/53cc727d-7b35-42c0-a217-e2064a74003e" />


---

## Activité 3 : Deux machines et calculatrice

### Étape 1 : Connexion entre deux machines distinctes

Les deux machines doivent être sur le **même réseau local**.

- **Côté serveur** : le `ServerSocket` est lié à l'IP réelle de la machine et à un port choisi.
  ```java
  InetAddress ip = InetAddress.getByName("192.168.100.3");   // IP du serveur
  InetSocketAddress adresse = new InetSocketAddress(ip, 1234);
  ServerSocket serverSocket = new ServerSocket();
  serverSocket.bind(adresse);
  ```
- **Côté client** : `localhost` est remplacé par l'IP de la machine serveur.
  ```java
  InetAddress ipServeur = InetAddress.getByName("192.168.100.3");
  Socket socket = new Socket();
  socket.connect(new InetSocketAddress(ipServeur, 1234), 5000);
  ```

**Trouver l'adresse IP :** `ipconfig` (Windows) 

![serveur]<img width="500" height="922" alt="image" src="https://github.com/user-attachments/assets/11e55eb0-cd04-47b5-8473-8fb4db7e196d" />
<img width="913" height="780" alt="image" src="https://github.com/user-attachments/assets/4adbb558-664f-4bc9-acd5-f400b50c2ae6" />



![IP du client]<img width="800" height="926" alt="image" src="https://github.com/user-attachments/assets/d45f1e92-6365-47ba-b946-37b49516a90c" />
<img width="1222" height="806" alt="image" src="https://github.com/user-attachments/assets/4c08a6ba-14ec-4b26-9aa4-9692489babea" />


![Connexion]<img width="500" height="357" alt="image" src="https://github.com/user-attachments/assets/c4a7013d-60a3-4c01-a3cd-4c7f1ccb789a" />



### Étape 2 : Calculatrice

Menu proposé au client :

```
1. Addition
2. Soustraction
3. Multiplication
4. Division
0. Quitter
```

**Protocole d'échange :**

| Sens | Données envoyées |
|------|------------------|
| Client → Serveur | `int choix`, `int x`, `int y` |
| Serveur → Client | `boolean ok`, puis `double resultat` si `ok` est vrai |

Le serveur répond `ok = false` en cas de division par zéro ou de choix inconnu, et le client affiche alors un message d'erreur. Le résultat est un `double` pour gérer les divisions non entières (par exemple 7 / 2 = 3.5).

| Opération | x | y | Résultat |
|-----------|---|---|----------|
| Addition | 4 | 3 | 7.0 |
| Soustraction | 4 | 10 | -6.0 |
| Multiplication | -3 | 5 | -15.0 |
| Division | 7 | 2 | 3.5 |
| Division | 5 | 0 | Erreur |



---

## Compilation et exécution

1. Lancer d'abord la classe **Serveur** (clic droit puis *Run*).
2. Lancer ensuite la classe **Client**.
3. Pour l'Activité 3, exécuter le serveur sur la machine A et le client sur la machine B.






Nom : ahmed dammak
Filière  : lsi3

