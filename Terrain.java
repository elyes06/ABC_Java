// Graphiques
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


// Utils
import java.util.Random;

// Structures
import java.util.ArrayList;
import java.util.HashMap;

public class Terrain extends JPanel{
     // Déclaration des variables membres

    // Largeur et hauteur du terrain
    int width, height;
    // Surface d'une cellule sur le terrain
    int cellSurface = 25;
    // Nombre total de cellules sur le terrain
    int cellCount;
    // Taux de spawn des nœuds
    float spawnRate;
    // Images utilisées pour afficher le terrain
    BufferedImage s_beehive;
    BufferedImage s_source;
    BufferedImage s_background;

    // Listes et tableaux pour stocker les données du terrain
    ArrayList<Integer> nodeKeys;
    ArrayList<Node> scoutSelectedNodes;
    ArrayList<Node> employeeNodes;
    ArrayList<Node> suitableNodes;
    HashMap<Integer, Node> nodes;
    HashMap<Integer, ArrayList<Integer>> links;
    ArrayList<Scout> scouts;
    ArrayList<Employee> employees;
    ArrayList<Observer> observers;
    ArrayList<Scout> scoutQueue;

    // Variables de contrôle du déroulement de la simulation
    boolean assigned = false;
    boolean finished = false;
    boolean allWaiting = true;
    boolean endOfSimulation = false;

    // Portée de recherche des employés
    float range;
    // Nombre d'employés sur le terrain
    int employeeCount;
    // Index du premier nœud d'arrière-plan
    int firstBack = 0;

    // Constructeur de la classe Terrain
public Terrain(int height, int width, int scoutCount, int employeeCount, int observerCount, float range, float spawnRate){
    // Définition des préférences de ce JPanel
    this.setVisible(true);  // Rend ce JPanel visible
    this.isDoubleBuffered();  // Active le double buffering pour éviter le scintillement

    // Chargement des images utilisées pour afficher le terrain
    try {
        s_beehive = ImageIO.read(new File("./Assets/Sprites/beehive.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        s_source = ImageIO.read(new File("./Assets/Sprites/source2.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        s_background = ImageIO.read(new File("./Assets/Sprites/background2.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Initialisation des variables membres avec les valeurs passées en paramètres
    this.spawnRate = spawnRate;  // Taux de spawn des nœuds
    this.height = height;  // Hauteur du terrain
    this.width = width;  // Largeur du terrain
    this.range = range;  // Portée de recherche des employés

    // Calcul du nombre total de cellules sur le terrain
    this.cellCount = (this.height / this.cellSurface) * (this.width / this.cellSurface);

    // Initialisation des structures de données pour stocker les noeuds et les employés
    this.nodes = new HashMap<Integer, Node>();  // hashMap pour stocker les noeuds avec leur ID comme clé
    this.nodeKeys = new ArrayList<Integer>();  // Liste pour stocker les clés des noeuds
    this.employeeNodes = new ArrayList<Node>();  // Liste pour stocker les noeuds des employés

    this.employeeCount = employeeCount;  // Nombre d'employés sur le terrain

    // Réglage des préférences de ce JPanel
    this.setPreferredSize(new Dimension(this.width, this.height));  // Définit la taille préférée de ce JPanel
    this.setBackground(new Color(117, 182, 78));  // Définit la couleur de fond du JPanel

    // Génération des scouts, des employés et des observateurs
    this.generateScouts(scoutCount);  // Génère le nombre spécifié de scouts
    this.scoutQueue = new ArrayList<Scout>(this.scouts);  // Initialise la file d'attente des scouts
    this.generateEmployees(employeeCount);  // Génère le nombre spécifié d'employées
    this.generateObservers(observerCount);  // Génère le nombre spécifié d'observatrices

    // Génération des noeuds sur le terrain
    this.generate();  // Génère les noeuds sur le terrain
    this.suitableNodes = new ArrayList<Node>(this.nodes.values());  // Initialise la liste des noeuds appropriés
    this.setLinks();  // Définit les liens entre les noeuds
}


    


    // Cette méthode génère les nœuds sur le terrain en fonction du taux de spawn et de la taille du terrain
    public void generate() {
        Random delta = new Random();  // Crée un générateur de nombres aléatoires
        Node bestNode = null;  // Initialise le meilleur nœud à null

        // Parcours de chaque cellule du terrain
        for (int i = 0; i < this.cellCount; ++i) {
            int basex = (i % (this.width / this.cellSurface)) * this.cellSurface;  // Calcule la coordonnée x de la cellule
            int basey = (i / (this.width / this.cellSurface)) * this.cellSurface;  // Calcule la coordonnée y de la cellule

            // Vérifie si un nœud doit être généré à cette position
            if (delta.nextInt((int) (32 - (this.spawnRate * 32) + 2)) == 1 && (basex > this.s_beehive.getWidth() || basey > this.s_beehive.getHeight())) {
                float quality = delta.nextInt(99) / 99.0f;  // Génère une qualité aléatoire pour le nœud
                Node node = new Node(basex, basey, quality, this.nodes.size());  // Crée un nouveau nœud

                // Met à jour le meilleur nœud si nécessaire
                if (bestNode == null || node.quality > bestNode.quality) {
                    bestNode = node;
                }

                this.nodeKeys.add(this.nodes.size());  // Ajoute la clé du nœud à la liste des clés
                this.nodes.put(this.nodes.size(), node);  // Ajoute le nœud à la map des nœuds
            }
        }
        bestNode.isBestNode = true;  // Marque le meilleur nœud comme tel
    }


    // Cette méthode vérifie si deux points sont à portée l'un de l'autre en fonction de la portée spécifiée
    public boolean reachable(int x1, int y1, int x2, int y2, float range) {
        // Calcule la distance entre les deux points
        int distance = ((int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
        // Calcule la distance maximale autorisée en fonction de la portée spécifiée
        int delta = (int) (((this.height + this.width) / 2) * range);
        // Vérifie si la distance entre les deux points est inférieure ou égale à la distance maximale autorisée
        return distance <= delta;
    }

    // Cette méthode établit les liens entre les nœuds en fonction de leur proximité et de la portée spécifiée
    public void setLinks() {
        this.links = new HashMap<Integer, ArrayList<Integer>>();  // Initialise la map des liens

        // Parcours de chaque nœud pour établir les liens
        for (Integer key : this.nodeKeys) {
            ArrayList<Integer> nodesLinks = new ArrayList<Integer>();  // Liste pour stocker les liens du nœud actuel
            Node alpha = this.nodes.get(key);  // Récupère le nœud actuel

            // Parcours de chaque nœud pour vérifier s'il est à portée du nœud actuel
            for (Integer innerLink : this.nodeKeys) {
                Node omega = this.nodes.get(innerLink);  // Récupère le nœud voisin
                // Vérifie si le nœud voisin est différent du nœud actuel et s'il est à portée
                if (innerLink != key && reachable(alpha.x, alpha.y, omega.x, omega.y, this.range)) {
                    nodesLinks.add(innerLink);  // Ajoute le nœud voisin à la liste des liens
                }
            }

            this.links.put(key, nodesLinks);  // Ajoute les liens du nœud actuel à la map des liens
        }
    }


    // Cette méthode supprime les liens d'un nœud spécifié
    public void removeLink(int target) {
        // Parcours de chaque liste de liens dans la map des liens
        for (ArrayList<Integer> linksList : this.links.values()) {
            // Supprime tous les liens pointant vers le nœud spécifié
            linksList.removeIf(link -> link == target);
        }
        
        this.nodes.remove(target);  // Supprime le nœud spécifié de la map des nœuds
        this.nodeKeys.removeIf(e -> e == target);  // Supprime la clé du nœud spécifié de la liste des clés
    }

    
    // Cette méthode génère le nombre spécifié de scouts avec des positions et des vitesses aléatoires
    public void generateScouts(int count) {
        this.scouts = new ArrayList<Scout>();  // Initialise la liste des scouts

        // Crée et ajoute le nombre spécifié de scouts à la liste
        Random randomizer = new Random();
        for (int i = 0; i < count; ++i) {
            Scout neoScout = new Scout(randomizer.nextInt(cellSurface), randomizer.nextInt(cellSurface), 4 + randomizer.nextInt(1));
            this.scouts.add(neoScout);
        }
    }


    // Cette méthode génère le nombre spécifié d'employés avec des positions et des vitesses aléatoires
    public void generateEmployees(int count) {
        this.employees = new ArrayList<Employee>();  // Initialise la liste des employés

        // Crée et ajoute le nombre spécifié d'employés à la liste
        Random randomizer = new Random();
        for (int i = 0; i < count; ++i) {
            Employee neoEmployee = new Employee(randomizer.nextInt(cellSurface), randomizer.nextInt(cellSurface), 2 + randomizer.nextInt(5));
            this.employees.add(neoEmployee);
        }
    }



    // Méthode pour générer un nombre donné d'observateurs
    public void generateObservers(int count){
        this.observers = new ArrayList<Observer>(); // Initialisation de la liste d'observateurs
        Random randomizer = new Random(); // Création d'une instance de Random pour générer des valeurs aléatoires

        // Boucle pour créer et ajouter des observateurs à la liste
        for(int i = 0; i < count; ++i){
            // Création d'un nouvel observateur avec des coordonnées aléatoires et une vitesse aléatoire
            Observer neoObserver = new Observer(randomizer.nextInt(cellSurface), randomizer.nextInt(cellSurface), 2 + randomizer.nextInt(5));
            this.observers.add(neoObserver); // Ajout de l'observateur à la liste
        }
    }
        
    
    // Méthode pour définir le comportement des observateurs pendant le travail
    private void observersWork(){
        // Liste pour stocker la somme cumulée des qualités des nœuds sélectionnés
        ArrayList<Float> qualityCumul = new ArrayList<Float>();
        float qualitySum = 0; // Initialisation de la somme des qualités

        // Boucle pour calculer la somme cumulée des qualités des nœuds sélectionnés par les employés
        for(Employee employee : employees){
            if(!employee.isObserver){ // Vérification si l'employé n'est pas un observateur
                qualityCumul.add(qualitySum); // Ajout de la somme cumulée actuelle à la liste
                qualitySum += employee.selectedNode.quality; // Ajout de la qualité du nœud sélectionné par l'employé à la somme
                employee.isWaiting = 0; // Réinitialisation du flag d'attente de l'employé
                employee.isAssigned = true; // Définition de l'employé comme assigné
            }
        }

        // Boucle pour déterminer quel nœud chaque observateur doit observer
        for(Observer observer : observers){
            // Génération d'une qualité aléatoire à observer
            float randomQuality = qualityCumul.get(qualityCumul.size()-1) * (float) Math.random();

            // Boucle pour trouver l'employé associé à la qualité aléatoire générée
            for(int i = 0; i < qualityCumul.size(); ++i){
                if(qualityCumul.get(i) >= randomQuality ){
                    Employee employee = employees.get(i);
                    observer.selectedNode = employee.selectedNode; // Définition du nœud à observer pour l'observateur
                    break;
                }
            }

            // Ajout de l'observateur à la liste des employés s'il n'y est pas déjà
            if(!employees.contains(observer)){
                employees.add((Employee) observer);
            }
            observer.isAssigned = true; // Définition de l'observateur comme assigné
        }
    }

    // Méthode pour définir le comportement des employés pendant le travail
    private void employeesWork(float deltaTime, Employee employee) {
        // Vérification si l'employé n'est pas en attente
        if(employee.isWaiting != 1){
            // Vérification si l'employé a atteint son nœud cible
            if(employee.nodeReached){
                // Déplacement de l'employé vers la ruche si nécessaire
                if(employee.moveToHive(deltaTime)){
                    if(employee.isWaiting == 2){
                        employee.isWaiting = 1;
                    }
                    employee.nodeReached = false;
                    // Vérification si l'employé n'a pas de nœud sélectionné
                    if(employee.selectedNode == null){
                        employee.isAssigned = false;
                        employee.bestNeighbourFound = false;
                    }
                }
            } else {
                // Vérification si l'employé est dans la ruche
                if(employee.inHive){
                    // Réduction du nombre de visites restantes au nœud sélectionné
                    if(employee.selectedNode.visitCountRequest-1 >= 0){
                        employee.selectedNode.visitCountRequest--;
                        employee.inHive = false;
                        // Suppression du nœud de la liste s'il a été visité le nombre de fois nécessaire
                        if(employee.selectedNode.visitCountRequest == 0){
                            this.suitableNodes.remove(employee.selectedNode); 
                        }
                    }else{
                        // Suppression du nœud sélectionné s'il a été visité trop de fois
                        employee.selectedNode = null;
                        employee.isAssigned = false;
                        employee.bestNeighbourFound = false;
                        return;
                    }
                }

                // Définition du nœud cible et déplacement de l'employé vers ce nœud
                employee.setTarget(employee.selectedNode);
                if(employee.moveToTarget(employee.selectedNode, deltaTime)){
                    employee.nodeReached = true;
                    employee.selectedNode.visitCount--;

                    // Vérification si le nœud a été suffisamment visité
                    if(employee.selectedNode.visitCount <= 0){
                        // Vérification si le nœud est le meilleur nœud
                        if(employee.selectedNode.isBestNode){
                            this.endOfSimulation = true;
                        }
                        removeLink(employee.selectedNode.id); // Suppression du lien associé au nœud
                        employee.selectedNode = null; // Réinitialisation du nœud sélectionné de l'employé
                    }

                    // Vérification si le meilleur voisin n'a pas encore été trouvé
                    if(!employee.bestNeighbourFound){
                        // Vérification si l'employé n'est pas un observateur
                        if(!employee.isObserver){
                            employee.isWaiting = 2; // Définition de l'employé en attente pour trouver le meilleur voisin
                        }
                        employee.bestNeighbour(links, nodes); // Recherche du meilleur voisin
                    }
                }
            }
            // Vérification si l'employé n'est pas en attente
            if(employee.isWaiting != 1){
                this.allWaiting = false; // Définition de la condition de tous en attente
            }
        }
    }


    // Méthode pour dessiner les éléments graphiques
    public void paint(Graphics g){
        super.paint(g); // Appel de la méthode paint de la classe parente
        if(this.height == 600 && this.width == 800){
            g.drawImage(this.s_background, 0, 0, this); // Dessine l'arrière-plan
        }        
        g.drawImage(this.s_beehive, 0, 0, this); // Dessine la ruche
        
        // Boucle pour dessiner chaque nœud
        for(Integer key : nodeKeys){
            Node alpha = this.nodes.get(key);
            g.drawImage(CustomUtils.adjustNodeColor(s_source, alpha), alpha.x, alpha.y, this);
        }

        // Dessine les liens entre les nœuds (commenté pour plus d'esthétique)
        // drawLinks(g);
        
        // Dessine les abeilles (observateurs, éclaireurs, employés)
        drawBees(this.observers, g);
        drawBees(this.scouts, g);
        drawBees(this.employees, g);
    }

    // Méthode pour dessiner les abeilles
    void drawBees(ArrayList<? extends Bee> bees, Graphics g){
        for(Bee bee : bees){
            // Vérifie le type d'abeille et dessine en conséquence
            if(bee instanceof Scout || bee instanceof Employee || bee instanceof Observer){
                bee.drawBee(g);
            }
        }
    }

    // Méthode pour dessiner les liens entre les nœuds
    public void drawLinks(Graphics g){
        Graphics2D g2d = (Graphics2D) g; // Conversion du contexte graphique en Graphics2D
                
        g2d.setStroke(new BasicStroke(2)); // Définition de l'épaisseur de la ligne
        g.setColor(Color.darkGray); // Définition de la couleur des liens

        // Boucle pour dessiner les liens entre les nœuds
        for(int i = 0; i < this.nodeKeys.size(); ++i){
            if(!this.links.containsKey(i)){
                continue;
            }
            Node alpha = this.nodes.get(nodeKeys.get(i));
            for(Integer link : this.links.get(nodeKeys.get(i))){
                Node omega = this.nodes.get(link);
                // Dessine une ligne entre chaque paire de nœuds
                g.drawLine(alpha.x + (cellSurface/2), alpha.y + (cellSurface/2), omega.x + (cellSurface/2), omega.y + (cellSurface/2));
            }
        }
    }

    // Méthode pour exécuter la simulation
    public void run() {
        float interval = 1000.0f/60.0f; // Intervalle de rafraîchissement (en millisecondes)
        float deltaTime = 0; // Temps écoulé depuis la dernière mise à jour
        long lastTime = System.currentTimeMillis(); // Temps écoulé depuis le début de la simulation
        long currentTime;
        
        // Boucle de simulation
        while(!endOfSimulation && !suitableNodes.isEmpty()){
            currentTime = System.currentTimeMillis();
            
            // Calcul du temps écoulé depuis la dernière mise à jour
            deltaTime+= (currentTime - lastTime) / interval;
            lastTime = currentTime;
            
            // Vérification si le temps écoulé atteint ou dépasse l'intervalle de rafraîchissement
            if(deltaTime >= 1){
                update(deltaTime); // Met à jour la simulation
                repaint(); // Redessine l'interface graphique
                
                deltaTime--; // Réinitialise le temps écoulé
            }
        }
    }

    // Méthode pour mettre à jour la simulation
    private void update(float deltaTime) {
        this.allWaiting = true; // Initialisation de la condition "tous en attente"
        
        // Boucle pour mettre à jour chaque employé
        for(Employee employee : this.employees){
            // Vérifie si l'employé est assigné à une tâche
            if(employee.isAssigned){
                this.employeesWork(deltaTime, employee); // Met à jour le comportement de l'employé
            } else if(!employee.isObserver) {
                this.allWaiting = false; // S'il n'est pas assigné, met la condition "tous en attente" à faux
                
                // Vérifie si l'employé n'a pas d'éclaireur assigné
                if(employee.assignedScout == null ){
                    employee.assignedScout = scoutQueue.get(0); // Affecte le premier éclaireur de la file
                    scoutQueue.remove(0); // Retire l'éclaireur de la file
                } else {
                    // Déplacement de l'éclaireur vers le prochain nœud dans la liste des nœuds appropriés
                    if(employee.assignedScout.currentNode < suitableNodes.size()){
                        Node currentNode = this.suitableNodes.get(employee.assignedScout.currentNode);
                        employee.assignedScout.setTarget(currentNode);
                        if(employee.assignedScout.moveToTarget(currentNode, deltaTime)){
                            employee.assignedScout.currentNode = this.suitableNodes.indexOf(currentNode) + 1;
                        }
                    } else if(employee.assignedScout.moveToHive(deltaTime)){
                        // Si l'éclaireur a atteint tous les nœuds, choisit un nœud aléatoire et l'affecte à l'employé
                        Random randomizer = new Random();
                        employee.selectedNode = this.suitableNodes.get(randomizer.nextInt(this.suitableNodes.size()));
                        employee.isAssigned = true;
                        employee.assignedScout.currentNode = 0;
                        this.scoutQueue.add(employee.assignedScout); // Ajoute l'éclaireur à la file
                        employee.assignedScout = null; // Réinitialise l'éclaireur assigné
                    }
                }
            }      
        }

        // Si tous les employés sont en attente, exécute le travail des observateurs
        if(this.allWaiting){
            this.observersWork();
        }
    }

}
