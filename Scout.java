
// Définition de la classe Scout, qui hérite de la classe Bee
public class Scout extends Bee {
    // Attributs spécifiques à la classe Scout
    public int currentNode = 0; // Indique le noeud actuel du parcours du scout

    // Constructeur de la classe Scout
    public Scout(int x, int y, int speed){
        // Appel du constructeur de la classe parent (Bee) avec les paramètres spécifiés
        super(x, y, speed);
    }

}
