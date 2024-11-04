import java.awt.Color;

public class Node{
    int id;
    int x,y;
    float quality;
    int visitCountRequest = 12;
    int visitCount = 12;
    public boolean isBestNode = false;

    Color color;
    public Node(int x, int y, float quality, int id){
        this.id = id;
        this.x = x;
        this.y = y;
        this.quality = quality;
        this.color = new Color(quality, 0.23f , 0.99f-quality);

    }
    public Node(int x, int y, float quality, boolean isSpawn) {
        this.x = x;
        this.y = y;
        this.quality = quality;
        if (isSpawn) {
            this.color = new Color(0, 0, 0, 0); 
        }
    }
    public Color getColor(){
        return this.color;
    }
}