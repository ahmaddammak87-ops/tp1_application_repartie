# TP1-DAR2026 : Programmation par Sockets en Java

Ce projet présente trois activités de communication **Client/Serveur** en Java avec les sockets TCP.

## Table des matières


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


![Activité 1](captures/activite1.png)
<img width="917" height="857" alt="image" src="https://github.com/user-attachments/assets/2f571877-b23c-4a14-8c64-687b12b4c758" />

---

## Activité 2 : Service de multiplication

### Étape 1 : Préparation de l'espace de travail
Création du projet IntelliJ avec les packages `clientPackage` et `serveurPackage`.

![Espace de travail](captures/etape1.png)

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

![Tests](captures/activite2-tests.png)
![Fermeture](captures/activite2-fermeture.png)

---

## Activité 3 : Deux machines et calculatrice

### Étape 1 : Connexion entre deux machines distinctes

Les deux machines doivent être sur le **même réseau local**.

- **Côté serveur** : le `ServerSocket` est lié à l'IP réelle de la machine et à un port choisi.
  ```java
  InetAddress ip = InetAddress.getByName("192.168.1.10");   // IP du serveur
  InetSocketAddress adresse = new InetSocketAddress(ip, 1234);
  ServerSocket serverSocket = new ServerSocket();
  serverSocket.bind(adresse);
  ```
- **Côté client** : `localhost` est remplacé par l'IP de la machine serveur.
  ```java
  InetAddress ipServeur = InetAddress.getByName("192.168.1.10");
  Socket socket = new Socket();
  socket.connect(new InetSocketAddress(ipServeur, 1234), 5000);
  ```

**Trouver l'adresse IP :** `ipconfig` (Windows) ou `ip a` (Linux).

![IP du serveur](captures/ip-serveur.png)
![IP du client](captures/ip-client.png)
![Connexion](captures/connexion.png)

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

![Addition](captures/addition.png)
![Soustraction](captures/soustraction.png)
![Multiplication](captures/multiplication.png)
![Division](captures/division.png)
![Division par zéro](captures/division-zero.png)

---

## Compilation et exécution

### Avec IntelliJ
1. Lancer d'abord la classe **Serveur** (clic droit puis *Run*).
2. Lancer ensuite la classe **Client**.
3. Pour l'Activité 3, exécuter le serveur sur la machine A et le client sur la machine B.

### En ligne de commande
```bash
javac -d out src/serveurPackage/*.java src/clientPackage/*.java
java -cp out serveurPackage.Server2     # terminal 1
java -cp out clientPackage.Client2      # terminal 2
```

---

## Problèmes fréquents

| Problème | Cause probable | Solution |
|----------|----------------|----------|
| `Connection refused` | Le serveur n'est pas lancé, ou le port est incorrect | Lancer le serveur d'abord, vérifier le port |
| `Address already in use` | Une ancienne instance occupe le port | Arrêter l'ancien processus (bouton stop) |
| Timeout de connexion (Activité 3) | Mauvaise IP ou pare-feu | Vérifier l'IP avec `ipconfig`, autoriser le port 1234 |
| `BindException: Cannot assign requested address` | L'IP écrite dans le serveur n'est pas celle de la machine | Mettre l'IP réelle du serveur |
| Programme bloqué | Ordre de lecture/écriture incohérent entre client et serveur | Vérifier que chaque `write` correspond à un `read` |
| `InputMismatchException` | Saisie d'autre chose qu'un entier | Saisir un entier valide |
| `package ... does not match` | Le nom du package ne correspond pas au dossier | Aligner la ligne `package` avec le dossier |

---

## Auteur
Nom : ...
Filière / Groupe : ...
Année : 2026
