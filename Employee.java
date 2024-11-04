import java.util.ArrayList;
import java.util.HashMap;

// Définition de la classe Employee, qui hérite de la classe Bee
public class Employee extends Bee {
    // Attributs spécifiques à la classe Employee
    public Scout assignedScout; // Scout assigné à l'employé
    public char isWaiting = 0; // Indique si l'employé est en attente ( 0 : N'attend pas; 1 : Attente basique; 2 : A trouvé le meilleur voisin)
    public boolean nodeReached = false; // Indique si l'employé a atteint son noeud cible
    public boolean bestNeighbourFound = false; // Indique si l'employé a trouvé un voisin de meilleure qualité
    public boolean isObserver = false; // Indique si l'employé est un observateur

    // Constructeur de la classe Employee
    public Employee(int x, int y, int speed){
        // Appel du constructeur de la classe parent (Bee) avec les paramètres spécifiés
        super(x, y, speed);
    }

    // Méthode pour que l'employé travaille vers son noeud cible avec un deltaTime spécifié
    public void work(float deltaTime){        
        // Définit le noeud cible comme le nœud sélectionné de l'employé
        this.setTarget(this.selectedNode);
        // Déplace l'employé vers son noeud cible avec le deltaTime spécifié
        if(this.moveToTarget(this.selectedNode, deltaTime)){
            this.nodeReached = true; // Marque le noeud comme atteint lorsque l'employé arrive
        }
    }

    // Méthode pour trouver un meilleur voisin pour l'employé
    public void bestNeighbour(HashMap<Integer, ArrayList<Integer>> links, HashMap<Integer, Node> nodes){
        // Récupère la liste des voisins du noeud sélectionné de l'employé à partir de la hashMap de liens
        ArrayList<Integer> neighbours = links.get(this.selectedNode.id);

        int firstNode = this.selectedNode.id; // Stocke l'ID du noeud actuel pour vérifier plus tard

        // Parcourt tous les voisins pour trouver un voisin de meilleure qualité
        for(Integer key : neighbours){
            Node currentNoder = nodes.get(key); // Récupère le noeud voisin actuel
            // Si le voisin a une qualité supérieure et peut être visité, le sélectionne comme noeud cible
            if(currentNoder.quality > this.selectedNode.quality && currentNoder.visitCountRequest-1 >= 0){
                this.selectedNode = currentNoder;
            }
        }
        // Si le noeud cible a changé et peut être visité, réduit son compteur de demande de visite
        if(firstNode != this.selectedNode.id && this.selectedNode.visitCountRequest-1 >= 0){
            this.selectedNode.visitCountRequest--;
            this.nodeReached = false; // Indique que le nœud n'a pas encore été atteint
        }
        this.bestNeighbourFound = true; // Indique qu'un meilleur voisin a été trouvé
    }
}
