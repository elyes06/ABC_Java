import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

// Classe représentant l'écran de titre de la simulation
public class TitleScreen extends JPanel {
    // Attributs de la classe
    public MainGame mainGame;    // Référence à l'instance principale de la simulation
    private Font pixelFont;    // Police de caractères personnalisée

    // Constructeur de la classe
    public TitleScreen(MainGame mainGame) {
        this.mainGame = mainGame;

        // Chargement de la police de caractères personnalisée depuis un fichier
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("./Assets/Fonts/font.ttf")).deriveFont(36f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 36); // Utilisation d'une police par défaut en cas d'erreur
        }

        // Configuration de l'apparence de l'écran de titre
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Création et configuration des composants de l'écran de titre
        JLabel labelTitle = new JLabel("ARTIFICIAL BEE COLONY SIMULATION", SwingConstants.CENTER);
        labelTitle.setFont(pixelFont);
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

        JLabel instructionLabel = new JLabel("<center>Bienvenue à notre simulation de colonie d'abeilles,<br><br>" +
                                                "Le fonctionnement de la simulation est assez simple:<br><br><br>" +
                                                "<div class='bordered-line'>• Les abeilles à tête rouge représentent les éclaireuses.<br>" +
                                                "• Les abeilles à tête verte représentent les employées.<br>" +
                                                "• Les abeilles à tête bleue représentent les observatrices.<br><br>" +
                                                "• Plus une fleur est rouge, plus sa qualité est élevée.<br><br>" +
                                                "• La simulation se termine lorsqu'on a suffisamment exploité,<br>" +
                                                "  la meilleure fleur (source) du plateau.</div><br><br><br><br>" +
                                                "Appuyez sur ESPACE pour lancer la simulation...</center>");
        instructionLabel.setFont(pixelFont.deriveFont(18f));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String style = "<style>.bordered-line { border: 1px solid white; padding: 5px; }</style>";
        instructionLabel.setText("<html>" + style + instructionLabel.getText() + "</html>");

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(labelTitle);

        JLabel signature = new JLabel("dev by Elyes & Pixel", SwingConstants.LEFT);
        signature.setFont(pixelFont.deriveFont(12f));
        signature.setForeground(Color.WHITE);
        signature.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajout des composants à l'écran de titre
        add(centerPanel, BorderLayout.NORTH);
        add(instructionLabel, BorderLayout.CENTER);
        add(signature, BorderLayout.SOUTH);

        // Association de la touche espace au démarrage de la simulation
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("pressed SPACE"), "lancer");
        actionMap.put("lancer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainGame.startSimulation(); // Démarrage de la simulation lorsque la touche espace est pressée
            }
        });
    }
}
