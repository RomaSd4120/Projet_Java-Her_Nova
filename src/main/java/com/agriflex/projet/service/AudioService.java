package com.agriflex.projet.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioService {

    private TargetDataLine line;
    private boolean enCours = false;

    // Format audio standard compatible avec tous les PC Windows
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    /**
     * Démarre l'enregistrement depuis le microphone dans un thread séparé
     */
    public void demarrerEnregistrement(String cheminFichier) throws LineUnavailableException {
        if (enCours) return;

        File fichierAudio = new File(cheminFichier);
        if (fichierAudio.getParentFile() != null) {
            fichierAudio.getParentFile().mkdirs(); // Crée le dossier audio_profils automatiquement
        }

        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone non détecté ou non supporté sur ce PC.");
        }

        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        enCours = true;

        Thread threadEnregistrement = new Thread(() -> {
            try (AudioInputStream ais = new AudioInputStream(line)) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fichierAudio);
            } catch (IOException e) {
                System.err.println("Erreur écriture fichier audio : " + e.getMessage());
            }
        });
        threadEnregistrement.setDaemon(true);
        threadEnregistrement.start();
    }

    /**
     * Arrête l'enregistrement et ferme le fichier .wav
     */
    public void arreterEnregistrement() {
        if (line != null && enCours) {
            enCours = false;
            line.stop();
            line.close();
        }
    }

    /**
     * Lit le fichier audio .wav enregistré
     */
    public void lireAudio(String cheminFichier) throws Exception {
        File fichierAudio = new File(cheminFichier);
        if (!fichierAudio.exists()) {
            throw new IOException("Aucun fichier audio trouvé : " + cheminFichier);
        }

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(fichierAudio);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public boolean isEnCours() {
        return enCours;
    }
}