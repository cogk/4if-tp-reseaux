# TP Programmation Réseaux

## TP 1 Application de Chat

### TCP

#### 1. Lancer le serveur

```sh
java chat.Main 4444
```

#### 2. Lancer un client texte

```sh
java chat.tcp.ChatClientTCP 127.0.0.1 4444
```

#### 3. Lancer un client graphique

```sh
java chat.tcp.ChatClientGUI 127.0.0.1 4444
```

### UDP

Pas de serveur dans la version UDP, donc pas d'historique.

```sh
java chat.udp.ChatClientUDP 228.5.6.7 5555
```

### Fonctionnalités communes

-   Pseudonymes : vous pouvez choisir votre pseudonyme

-   Quitter/Se déconnecter : au lieu de quitter en tuant le processus,
    vous pouvez taper `/quit`, `/dc` ou `/bye` pour vous déconnecter.

-   Salles de discussion : les clients dans les mêmes salles de discussion peuvent discuter
    ensemble sans que les autres utilisateurs ne puissent voir leurs discussions.
    Pour rejoindre une salle de discussion, tapez `/room <nom-de-la-salle>`.
    Pour retourner dans la salle principale, tapez `/room`.

---

## TP 2 Serveur Web

## Lancer le serveur

```sh
java http.server.WebServer <port> <chemin/complet/vers/le/dossier/racine>
```

-   Le dossier racine correspond au dossier qui contient tous les fichiers à servir.

-   Le dossier `test-serveur/` inclut quelques fichiers de démonstration. Le dossier `doc/` est aussi consultable, par exemple, via le serveur.

## Accéder aux ressources

### PUT

-   Créer un fichier avec le contenu spécifié

-   ou remplacer le contenu

```sh
curl -i -X PUT localhost/lorem.txt -d "Lorem ipsum. "
```

### GET

-   récupérer un fichier

```sh
curl localhost/lorem.txt
```

-   voir les headers

```sh
curl -i localhost/lorem.txt
```

### POST

-- créer un fichier avec le contenu spécifié
-- ou ajouter à la fin du fichier

```sh
curl -i -X POST localhost/lorem.txt -d "Bla bla. "
```

### HEAD

-   récupérer les headers, et aussi la longueur du fichier

```sh
curl -I localhost/lorem.txt
```

Note : `curl` indique un avertissement lorsqu'on
souhaite exécuter la méthode HEAD. En effet, cette
manière de faire remarque que le body de la réponse
n'est pas de la longueur indiquée dans le header de
la réponse Content-Length, car le Content-Length
dans la réponse se rapport à la taille du fichier
qu'on aurait pu obtenir par un GET, et non pas la
taille du body de la réponse.

```sh
curl -X HEAD localhost/lorem.txt # ne pas faire ça
```

### GET sur fichier illisible (permissions)

```sh
curl localhost/inaccessible
```

### DELETE

-   Supprimer un fichier

```sh
curl -i -X DELETE localhost/lorem.txt
```

### Redirections 301

-   lorsqu'on GET un dossier, le serveur
    redirige vers index.html, index.htm ou index.php
    si le fichier existe, sinon erreur 403
    car impossible de GET un dossier.

```sh
curl -i localhost # pas de redirection
```

```sh
curl -L localhost # en suivant la redirection
```

### Autres méthodes HTTP, non implémentées

-   erreur HTTP Not Implemented

```sh
curl -i -X PATCH localhost
```

### Exécution PHP (côté serveur)

-   Au préalable, il faut que l'exécutable `php` soit disponible sur votre système
    et que le PATH soit correctement défini.

-   Un GET vient exécuter le fichier PHP du côté
    du serveur, et renvoit le résultat vers le client

```sh
curl localhost/bonjour.php
```

-   On peut ajouter un header 'TP-Ignore-Executable' pour
    que le serveur n'exécute pas le fichier PHP,
    donc pour que le serveur le considère comme
    n'importe quel autre fichier.

```sh
curl -H 'TP-Ignore-Executable: ignore' localhost/bonjour.php
```

-   Par exemple on peut faire un PUT pour créer le fichier PHP

```php
curl -H 'TP-Ignore-Executable: ignore' -X PUT localhost/dossier/date.php -d '<?php
    setlocale(LC_ALL, "fr_FR");
    $str = strftime("%A %e %B %Y à %H:%M");
    $encoding = mb_detect_encoding($str, "auto", true);
    $converted = iconv($encoding, "UTF-8", $str);
    $converted = str_replace(" ", " ", $converted);
    echo $converted . PHP_EOL;
?>'
```

### Exécution Python côté serveur

-   Au préalable, il faut que l'exécutable `python3` soit disponible sur votre système
    et que le PATH soit correctement défini.

```sh
curl localhost/rand.py
```
