package Tool;

import javax.sound.sampled.*;
import java.io.File;

public class Music {
    private final String audioFilePath;
    private Clip clip;
    boolean isPlaying = false;

    public Music(String audioFilePath) {
        this.audioFilePath = audioFilePath;
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        isPlaying = false;
                        clip.setFramePosition(0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void play() {
        if (clip != null && !isPlaying) {
            clip.setFramePosition(0);
            clip.start();
            isPlaying = true;
        }
    }

    public void stop() {
        if (clip!= null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}