// Définition de la classe Observer, qui hérite de la classe Employee
public class Observer extends Employee {
    // Constructeur de la classe Observer
    public Observer(int x, int y, int speed){
        // Appel du constructeur de la classe parent (Employee) avec les paramètres spécifiés
        super(x, y, speed);
        this.isObserver = true; // Indique que cet employé est un observateur
    }
}
