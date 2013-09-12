package jivko.synthesizer.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import jivko.config.ConfigurationManager;
import jivko.synthesizer.Synthesizer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import jivko.util.MakeSound;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RHVoiceOnLinuxSynthesizer extends BaseSynthesizer {

    @Override
    public void talk(String text) {

        if (text == null || "".equals(text)) {
            return;
        }

        try {
            String textToSayFileName = ConfigurationManager.getInstance().getRhVoiceTextToSayFileName();
            String resultWavFileName = ConfigurationManager.getInstance().getRhVoiceResultWavFileName();

            PrintWriter writer = new PrintWriter(textToSayFileName, "UTF-8");
            writer.println(text);
            writer.close();

            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "RHVoice -i " + textToSayFileName + " --voice=Aleksandr -o " + resultWavFileName});
            p.waitFor();
            
            int exitCode = p.exitValue();

            if (exitCode == 0) {
              playWav(resultWavFileName);
            } else {
                throw new Exception("cann't run RHVoice");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playWav(String fileName) throws Exception {
        MakeSound.playWav(fileName);
    }

    public static void playWav2(String fileName) throws Exception {
        InputStream in = new FileInputStream(fileName);

        AudioStream audioStream = new AudioStream(in);

        AudioPlayer.player.start(audioStream);
    }
}
