package Tool;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 一个使用 Java Sound API 播放音频文件的音乐播放器。
 * 支持播放、暂停、恢复、停止和资源清理功能。
 */
public class Music {
    private final String audioFilePath; // 音频文件路径
    private Clip clip; // 音频剪辑对象
    private boolean isPlaying = false; // 是否正在播放
    private long pausePosition; // 暂停时的播放位置

    /**
     * 使用指定的音频文件路径构造音乐播放器。
     *
     * @param audioFilePath 音频文件路径（例如 WAV 格式）
     * @throws IllegalArgumentException 如果文件路径无效或不受支持
     * @throws RuntimeException 如果初始化音频时发生错误
     */
    public Music(String audioFilePath) {
        if (audioFilePath == null || audioFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("音频文件路径不能为空或空字符串");
        }
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists() || !audioFile.isFile()) {
            throw new IllegalArgumentException("音频文件不存在或不是文件：" + audioFilePath);
        }
        this.audioFilePath = audioFilePath;
        initializeClip();
    }

    /**
     * 初始化音频剪辑以供播放。
     */
    private void initializeClip() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(audioFilePath))) {
            AudioFormat audioFormat = audioInputStream.getFormat();
            if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInputStream)) {
                throw new IllegalArgumentException("不支持的音频格式：" + audioFilePath);
            }
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    synchronized (Music.this) {
                        isPlaying = false;
                    }
                }
            });
        } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("不支持的音频文件格式：" + audioFilePath, e);
        } catch (IOException e) {
            throw new RuntimeException("读取音频文件时发生错误：" + audioFilePath, e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException("无法获取音频线路：" + audioFilePath, e);
        }
    }

    /**
     * 检查音频是否正在播放。
     *
     * @return 如果音频正在播放，返回 true，否则返回 false
     */
    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 从头开始播放音频。
     */
    public synchronized void play() {
        if (clip == null) {
            throw new IllegalStateException("音频剪辑未初始化");
        }
        if (!isPlaying) {
            clip.setFramePosition(0);
            clip.start();
            isPlaying = true;
            pausePosition = 0;
        }
    }

    /**
     * 暂停音频播放，保留当前播放位置。
     */
    public synchronized void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
        }
    }

    /**
     * 从暂停位置恢复音频播放。
     */
    public synchronized void resume() {
        if (clip == null) {
            throw new IllegalStateException("音频剪辑未初始化");
        }
        if (!isPlaying) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
            isPlaying = true;
        }
    }

    /**
     * 停止音频播放并重置到开头。
     */
    public synchronized void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
            isPlaying = false;
            pausePosition = 0;
        }
    }

    /**
     * 关闭音频剪辑并释放资源。
     */
    public synchronized void close() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            isPlaying = false;
            pausePosition = 0;
        }
    }

    /**
     * 获取音频文件路径。
     *
     * @return 音频文件路径
     */
    public String getAudioFilePath() {
        return audioFilePath;
    }
}