import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

// Classe utilitaire contenant des méthodes pour la manipulation d'images et de couleurs personnalisées
public class CustomUtils {

    // Méthode pour obtenir la couleur d'une abeille en fonction de son type
    public static Color getBeeColor(Bee bee) {
        if (bee instanceof Observer) {
            return Color.BLUE; // Observateur (bleu)
        } else if (bee instanceof Employee) {
            return Color.GREEN; // Employé (vert)
        } else {
            return Color.RED; // Autre (rouge)
        }
    }

    // Méthode pour ajuster la couleur d'une image en fonction de la couleur de l'abeille
    public static BufferedImage adjustBeeColor(BufferedImage sourceImage, Color beeColor) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Parcours de chaque pixel de l'image source
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelRGB = sourceImage.getRGB(x, y);

                // Vérifie si le pixel est transparent
                if ((pixelRGB >> 24) == 0x00) {
                    image.setRGB(x, y, pixelRGB); // Garde le pixel transparent
                } else {
                    Color pixelColor = new Color(pixelRGB);
                    // Vérifie si le pixel est dans une plage de couleurs spécifique
                    if (pixelColor.getAlpha() == 255 && (pixelColor.getRed() >= 64 && pixelColor.getRed() <= 77)) {
                        // Calcule la nouvelle couleur moyenne en mélangeant le pixel d'origine avec la couleur de l'abeille
                        int red = (pixelColor.getRed() + beeColor.getRed()) / 2;
                        int green = (pixelColor.getGreen() + beeColor.getGreen()) / 2;
                        int blue = (pixelColor.getBlue() + beeColor.getBlue()) / 2;

                        Color newColor = new Color(red, green, blue);
                        image.setRGB(x, y, newColor.getRGB()); // Applique la nouvelle couleur au pixel
                    } else {
                        image.setRGB(x, y, pixelRGB); // Garde la couleur du pixel d'origine
                    }
                }
            }
        }

        return image;
    }

    // Méthode pour ajuster la couleur d'une image de nœud en fonction de la couleur du nœud
    public static Image adjustNodeColor(BufferedImage sourceImage, Node node) {
        BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Parcours de chaque pixel de l'image source
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelRGB = sourceImage.getRGB(x, y);

                // Vérifie si le pixel est transparent
                if ((pixelRGB >> 24) == 0x00) {
                    image.setRGB(x, y, pixelRGB); // Garde le pixel transparent
                } else {
                    Color pixelColor = new Color(pixelRGB);
                    // Vérifie si le pixel est dans une plage de couleurs spécifique
                    if (pixelColor.getAlpha() == 255 && pixelColor.getRed() >= 170) {
                        // Calcule la nouvelle couleur moyenne en mélangeant le pixel d'origine avec la couleur du nœud
                        int red = (pixelColor.getRed() + node.getColor().getRed()) / 2;
                        int green = (pixelColor.getGreen() + node.getColor().getGreen()) / 2;
                        int blue = (pixelColor.getBlue() + node.getColor().getBlue()) / 2;

                        Color newColor = new Color(red, green, blue);
                        image.setRGB(x, y, newColor.getRGB()); // Applique la nouvelle couleur au pixel
                    } else {
                        image.setRGB(x, y, pixelRGB); // Garde la couleur du pixel d'origine
                    }
                }
            }
        }
        return image;
    }
}
