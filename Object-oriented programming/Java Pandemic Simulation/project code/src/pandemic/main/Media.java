package pandemic.main;

import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

/**
 * Loads and plays music
 */
public class Media {
    /**
     * @return mediaPlayer responsible for played music
     */
    public static MediaPlayer playMusic() {
        javafx.scene.media.Media media = null;
        try {
            media = new javafx.scene.media.Media(Printer.class.getResource("music.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert media != null;
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.05);
        mediaPlayer.setAutoPlay(true);
        return mediaPlayer;
    }
}
