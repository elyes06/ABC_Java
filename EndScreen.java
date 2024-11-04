import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

// Classe représentant l'écran de fin de simulation
public class EndScreen extends JPanel {
    // Attributs de la classe
    public MainGame mainGame;    // Référence à l'instance principale de la simulation
    private Font pixelFont;    // Police de caractères personnalisée

    // Constructeur de la classe
    public EndScreen(MainGame mainGame) {
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

        // Configuration de l'apparence de l'écran de fin de simulation
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Création et configuration des composants de l'écran de fin de simulation
        JLabel labelTitle = new JLabel("FIN DE LA SIMULATION", SwingConstants.CENTER);
        labelTitle.setFont(pixelFont.deriveFont(58f)); // Modification de la taille de la police
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0)); // Définition d'une marge supérieure

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(labelTitle);

        JLabel signature = new JLabel("dev by Elyes & Thomas", SwingConstants.LEFT);
        signature.setFont(pixelFont.deriveFont(12f)); 
        signature.setForeground(Color.WHITE);
        signature.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instructionLabel = new JLabel("<html><center>Appuyez sur la touche q pour quitter...</center></html>");
        instructionLabel.setFont(pixelFont.deriveFont(18f));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Ajout des composants à l'écran de fin de simulation
        add(centerPanel, BorderLayout.NORTH);
        add(instructionLabel, BorderLayout.CENTER);
        add(signature, BorderLayout.SOUTH);

        // ActionMap pour quitter la simulation lorsque la touche 'q' est pressée
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("pressed Q"), "quitter");
        actionMap.put("quitter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(EndScreen.this);
                frame.dispose(); // Fermeture de la fenêtre principale de l'application
            }
        });
    }
}
