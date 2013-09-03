package jivko.synthesizer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jivko.synthesizer.Synthesizer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import jivko.util.MakeSound;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RHVoiceOnLinuxSynthesizer implements Synthesizer {

    @Override
    public void talk(String text) {

        if (text == null || text == "") {
            return;
        }

        try {
            String TEXT_TO_SAY_FILENAME = "textToSay.txt";
            String RESULT_WAV_FILENAME = "out.wav";

            PrintWriter writer = new PrintWriter(TEXT_TO_SAY_FILENAME, "UTF-8");
            writer.println(text);
            writer.close();

            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "RHVoice -i " + TEXT_TO_SAY_FILENAME + " -o " + RESULT_WAV_FILENAME});
            p.waitFor();
            
            int exitCode = p.exitValue();

            if (exitCode == 0) {
              playWav(RESULT_WAV_FILENAME);
            } else {
                throw new Exception("cann't run RHVoice");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void playWav(String fileName) throws Exception {
        MakeSound.playSound(fileName);
    }

    public static void playWav2(String fileName) throws Exception {
        InputStream in = new FileInputStream(fileName);

        AudioStream audioStream = new AudioStream(in);

        AudioPlayer.player.start(audioStream);
    }
}
