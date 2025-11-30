/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

import java.io.InputStream;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.HashMap;

/**
 *
 * @author mayza
 */
public class Audio_manager {
    private Clip backgroundMusic;
    private Map<String, Clip> soundEffects;
    
    public Audio_manager() {
        soundEffects = new HashMap<>();
    }
    
    // Method untuk memulai background music
    public void playBackgroundMusic() {
        try {
            // Cek jika music sudah berjalan
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                System.out.println("Background music already playing");
                return;
            }
            
            System.out.println("Loading background music...");
            
            // Path sesuai struktur resources Anda
            InputStream musicStream = getClass().getClassLoader()
                .getResourceAsStream("asset_audio/Background_music_Game.wav");
            
            if (musicStream == null) {
                System.err.println("❌ Background music not found!");
                System.err.println("Trying to load: asset.audio/Background_music_Game.wav");
                
                // Debug: List available resources
                debugAvailableResources();
                return;
            }
            
            System.out.println("✅ Background music file found!");
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicStream);
            backgroundMusic = AudioSystem.getClip();
            
            System.out.println("Opening audio clip...");
            backgroundMusic.open(audioInputStream);
            
            // Set volume (optional)
            setVolume(0.7f);
            
            System.out.println("Starting background music loop...");
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
            
            System.out.println("🎵 Background music started successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error playing background music");
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            
            // Additional debug info
            debugAudioSystem();
        }
    }
    
    // Method untuk set volume (0.0 - 1.0)
    private void setVolume(float volume) {
        try {
            if (backgroundMusic != null && backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                System.out.println("Volume set to: " + volume);
            }
        } catch (Exception e) {
            System.err.println("Could not set volume: " + e.getMessage());
        }
    }
    
    // Method untuk stop background music
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            System.out.println("Background music stopped");
        }
    }
    
    // Debug method untuk mengecek resources yang tersedia
    private void debugAvailableResources() {
        try {
            System.out.println("🔍 Debug: Available resources in asset.audio:");
            java.net.URL resourceUrl = getClass().getClassLoader().getResource("asset.audio");
            if (resourceUrl != null) {
                System.out.println("Resource URL: " + resourceUrl);
                
                // Try to list files in the directory
                java.io.File audioDir = new java.io.File(resourceUrl.toURI());
                if (audioDir.exists() && audioDir.isDirectory()) {
                    String[] files = audioDir.list();
                    if (files != null) {
                        for (String file : files) {
                            System.out.println("   - " + file);
                        }
                    }
                }
            } else {
                System.out.println("No resource found for: asset.audio");
            }
        } catch (Exception e) {
            System.err.println("Error during debug: " + e.getMessage());
        }
    }
    
    // Debug method untuk mengecek audio system
    private void debugAudioSystem() {
        try {
            System.out.println("🔍 Audio System Info:");
            javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            System.out.println("Available mixers: " + mixers.length);
            for (javax.sound.sampled.Mixer.Info mixer : mixers) {
                System.out.println("   - " + mixer.getName() + " (" + mixer.getDescription() + ")");
            }
        } catch (Exception e) {
            System.err.println("Error getting audio system info: " + e.getMessage());
        }
    }
    
    // Method untuk mengecek status audio
    public void printAudioStatus() {
        if (backgroundMusic == null) {
            System.out.println("Audio status: Background music not initialized");
        } else if (backgroundMusic.isRunning()) {
            System.out.println("Audio status: Background music is PLAYING");
        } else {
            System.out.println("Audio status: Background music is STOPPED");
        }
    }
    
    //Mrthod untuk sound effect tebasan pedang
    public void loadSoundEffect(String name, String path) {
        try {
            InputStream soundStream = getClass().getClassLoader().getResourceAsStream(path);
            if (soundStream == null) {
                System.err.println("Sound effect tidak ditemukan: " + path);
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundEffects.put(name, clip);
            
            System.out.println("Sound effect " + name + "berhasil dimuat");
            
        } catch (Exception e) {
            System.err.println("Error load sound effect: " + name);
            e.printStackTrace();
        }
    }
    
    //Method untuk play sound effect
    public void playSoundEffect(String name) {
        try {
            Clip clip = soundEffects.get(name);
            if (clip != null) {
                //Mereset ke posisi awal dan play
                clip.setFramePosition(0);
                clip.start();
                System.out.println("Memainkan sound effect " + name);
            } else {
                System.err.println("Sound effect " + name + " gaga; dimuat");
            }
        } catch (Exception e) {
            System.err.println("Gagal memainkan sound effect" + name);
            e.printStackTrace();
        }
    }
}