import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Classe pour la lecture et la manipulation des fichiers audio
public class Sound {
 
    Clip clip; // Clip pour gérer la lecture audio
    URL soundUrl[] = new URL[2]; // Tableau pour stocker les URL des fichiers audio
    
    // Constructeur pour initialiser les URL des fichiers audio
    public Sound(){
        // Chargement des URL des fichiers audio
        soundUrl[0] = getClass().getResource("./Assets/Music/ABC.wav"); // URL du premier fichier audio
        soundUrl[1] = getClass().getResource("./Assets/Music/ABC_INSIMULATION.wav"); // URL du deuxième fichier audio
    }

    // Méthode pour définir le fichier audio à lire en fonction de l'indice fourni
    public void setFile(int i){
        try{
            // Création d'un flux d'entrée audio à partir de l'URL spécifiée
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            // Ouverture du clip audio
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch(Exception e){
            // Gestion des exceptions
        }
    }

    // Méthode pour démarrer la lecture du clip audio
    public void play(){
        clip.start();
    }

    // Méthode pour mettre en boucle la lecture du clip audio
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Méthode pour arrêter la lecture du clip audio
    public void stop(){
        clip.stop();
    }
}
