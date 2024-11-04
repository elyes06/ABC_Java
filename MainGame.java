import javax.swing.*;

// Classe principale représentant la simulation 
public class MainGame extends JFrame {
    // Attributs de la classe
    private Terrain terrain;        // Le terrain de simulation 
    private TitleScreen titleScreen;    // L'écran de titre
    private EndScreen endScreen;        // L'écran de fin de simulation 
    private Sound sound= new Sound();
    // Constructeur de la classe
    public MainGame() {
        // Initialisation des attributs
        this.terrain = new Terrain(600, 800, 3, 3, 2, 0.15f, 0.1f);
        this.titleScreen = new TitleScreen(this);
        this.endScreen = new EndScreen(this); 

        this.playMusic(0);
        // Configuration de la fenêtre principale
        this.add(titleScreen); 
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true); 

    }

    // Méthode pour démarrer la simulation 
    public void startSimulation() {
        // Remplacement de l'écran de titre par le terrain de simulation
        this.stopMusic();
        this.playMusic(1);
        this.remove(titleScreen); 
        this.add(terrain); 

        // Exécution de la simulation dans un thread séparé
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                terrain.run();    // Exécution de la simulation
                switchToEndScreen();    // Passage à l'écran de fin de simulation
                return null;
            }
        };
        worker.execute();    // Démarrage du thread de travail
        this.validate();     // Validation de la modification de la fenêtre
        this.repaint();      // Rafraîchissement de l'affichage
    }

    // Méthode pour passer à l'écran de fin de simulation
    public void switchToEndScreen() {
        this.stopMusic();
        this.playMusic(0);
        this.remove(terrain); 
        this.add(endScreen); 
        this.validate(); 
        this.repaint(); 
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic(){
        sound.stop();
    }
    // Méthode principale, point d'entrée de l'application
    public static void main(String args[]) {
        new MainGame();    // Création d'une instance de MainGame pour démarrer la simulation
    }
}
