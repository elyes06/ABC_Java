import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

// Classe représentant une abeille dans la simulation
public class Bee extends JPanel{
    // Attributs de la classe
    float x, y; // Position de l'abeille sur l'écran
    int dx, dy, speed; // Vecteur de déplacement et vitesse de l'abeille
    public Node selectedNode; // Noeud cible que l'abeille doit atteindre
    public boolean isAssigned = false; // Indique si l'abeille a été assignée à un noeud
    public boolean inHive = true; // Indique si l'abeille est dans la ruche
    public BufferedImage up1, down1, up2, down2; // Images pour l'animation de l'abeille
    public String direction; // Direction actuelle de l'animation de l'abeille

    // Constructeur de la classe
    public Bee(int x, int y, int speed){
        // Initialisation des attributs
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = "down1"; // Direction initiale de l'animation

        this.getBeeImage(); // Chargement des images de l'abeille
    }

    // Méthode pour déplacer l'abeille vers un noeud cible avec un deltaTime spécifié
    public float move(float deltaTime, Node target){
        // Calcul des distances en x et y entre l'abeille et le noeud cible
        float distanceX = target.x - this.x;
        float distanceY = target.y - this.y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        // Calcul de la proportion de déplacement en fonction de la vitesse et du deltaTime
        float proportion = Math.min(1.0f, this.speed * deltaTime / distance);
        float dx = distanceX * proportion;
        float dy = distanceY * proportion;

        // Mise à jour de la position de l'abeille
        this.x += dx;
        this.y += dy;
        return distance; // Retourne la distance restante à parcourir
    }

    // Méthode pour déplacer l'abeille vers la ruche
    public float move(float deltaTime){
        // Calcul des distances en x et y entre l'abeille et le point cible (65, 80) qui est la ruche
        float distanceX = 65 - this.x;
        float distanceY = 80 - this.y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        // Calcul de la proportion de déplacement en fonction de la vitesse et du deltaTime
        float proportion = Math.min(1.0f, this.speed * deltaTime / distance);
        float dx = distanceX * proportion;
        float dy = distanceY * proportion;

        // Mise à jour de la position de l'abeille
        this.x += dx;
        this.y += dy;

        return distance; // Retourne la distance restante à parcourir
    }

    // Méthode pour déplacer l'abeille vers un noeud cible avec un deltaTime spécifié
    public boolean moveToTarget(Node target, float deltaTime){
        // Si l'abeille atteint le noeud cible, met à jour sa position et retourne vrai
        if(move(deltaTime, target) <= 1.0f) { 
            this.x = target.x;
            this.y = target.y;
            return true;
        }
        return false;
    }

    // Méthode pour déplacer l'abeille vers la ruche
    public boolean moveToTarget(float deltaTime){
        // Si l'abeille atteint le point cible, met à jour sa position et retourne vrai
        if(move(deltaTime) <= 1.0) {
            this.x = 65;
            this.y = 80;
            this.inHive = true; // L'abeille est de retour dans la ruche
            return true;
        }
        return false;
    }
    
    // Méthode pour définir le noeud cible pour l'abeille
    public void setTarget(Node target){
        float deltaX = target.x - this.x;
        float deltaY = target.y - this.y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        // Normalisation du vecteur de déplacement
        dx = (int) Math.round((deltaX / distance));
        dy = (int) Math.round((deltaY / distance));
    }

    // Méthode pour définir le point cible = la ruche pour l'abeille
    public void setTarget(){
        float deltaX = 65 - this.x;
        float deltaY = 80 - this.y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        // Normalisation du vecteur de déplacement
        dx = (int) Math.round((deltaX / distance));
        dy = (int) Math.round((deltaY / distance));
    }

    // Méthode pour déplacer l'abeille vers la ruche avec un deltaTime spécifié
    public boolean moveToHive(float deltaTime){
        this.setTarget(); // Définit le point cible comme la ruche
        return (this.moveToTarget(deltaTime)); // Déplace l'abeille vers la ruche
    }

    // Méthode pour dessiner l'abeille sur l'écran
    public void drawBee(Graphics g){
        Color beeColor = CustomUtils.getBeeColor(this); // Obtient la couleur de l'abeille en fonction de son type

        // Détermine la direction de l'animation de l'abeille en fonction de son vecteur de déplacement
        int dir = this.dx < 0 ? 1 : -1;
        if(this.direction == "down1"){
            g.drawImage(CustomUtils.adjustBeeColor(this.down1, beeColor), (int)(this.x), (int)(this.y), this.down1.getWidth() * dir, this.down1.getHeight(), this);
            this.direction = "down2";
        }else if(this.direction == "down2"){
            g.drawImage(CustomUtils.adjustBeeColor(this.down2, beeColor), (int)(this.x), (int)(this.y), this.down2.getWidth() * dir, this.down2.getHeight(), this);
            this.direction = "up1";
        }else if(this.direction == "up1"){
            g.drawImage(CustomUtils.adjustBeeColor(this.up1, beeColor), (int)(this.x), (int)(this.y), this.up1.getWidth() * dir, this.up1.getHeight(), this);
            this.direction = "up2";
        }else{
            g.drawImage(CustomUtils.adjustBeeColor(this.up2, beeColor), (int)(this.x), (int)(this.y), this.up2.getWidth() * dir, this.up2.getHeight(), this);
            this.direction = "down1";
        }
    }
    
    // Méthode pour charger les images de l'abeille à partir des fichiers
    public void getBeeImage(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("./Assets/Sprites/MyBee/up1.gif"));
            down1 = ImageIO.read(getClass().getResourceAsStream("./Assets/Sprites/MyBee/down1.gif"));
            up2 = ImageIO.read(getClass().getResourceAsStream("./Assets/Sprites/MyBee/up2.gif"));
            down2 = ImageIO.read(getClass().getResourceAsStream("./Assets/Sprites/MyBee/down2.gif"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
