package jivko.sphinx.util.wavtest;

import jivko.sphinx.util.wavtest.Segmenter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.FrontEnd;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.ConfigurationManagerUtils;


//TODO: 
//1. Split into different parts like jokes, sketches, utterances. 
//2. Implement commands and transitions
//3. Implemet sphinx recognizer part
//4. Simplify libraries and interfaces
//5. Jokes creator
//6. Speach vizualizator - face could be already drawn, we need to show only speech aplitude
//7. Speach syntezator for linux 
public class Segmenter {

    public static void main(String[] argv) throws MalformedURLException,
            IOException {

        String inputFile = "D:/tmp/1/test.wav";
        String outputFile = "D:/tmp/1/test2";
        
        String configFile = null;
        
        String inputCtl = null;
        
        boolean noSplit = false;

        for (int i = 0; i < argv.length; i++) {
            if (argv[i].equals("-c")) {
                configFile = argv[++i];
            }
            if (argv[i].equals("-i")) {
                inputFile = argv[++i];
            }
            if (argv[i].equals("-ctl")) {
                inputCtl = argv[++i];
            }
            if (argv[i].equals("-o")) {
                outputFile = argv[++i];
            }
            if (argv[i].equals("-no-split")) {
                noSplit = Boolean.parseBoolean(argv[i]);
            }
        }

        if ((inputFile == null && inputCtl == null) || outputFile == null) {
            System.out
                    .println("Usage: java  -cp lib/batch.jar:lib/sphinx4.jar edu.cmu.sphinx.tools.endpoint.Segmenter "
                            + "[ -config configFile ] -name frontendName "
                            + "< -i input File -o outputFile | -ctl inputCtl -i inputFolder -o outputFolder >");
            System.exit(1);
        }

        URL configURL;
        if (configFile == null)
            configURL = Segmenter.class.getResource("frontend.config2.xml");
        else
            configURL = new File(configFile).toURI().toURL();

        ConfigurationManager cm = new ConfigurationManager(configURL);

        if (noSplit) {
            ConfigurationManagerUtils.setProperty(cm, "wavWriter",
                    "captureUtterances", "false");
        }
        if (inputCtl != null) {
            ConfigurationManagerUtils.setProperty(cm, "wavWriter",
                    "isCompletePath", "true");
        }

        if (inputCtl == null)
            processFile(inputFile, outputFile, cm);
        else
            processCtl(inputCtl, inputFile, outputFile, cm);
    }

    static private void processFile(String inputFile, String outputFile,
            ConfigurationManager cm) throws MalformedURLException, IOException {

        FrontEnd frontend = (FrontEnd) cm.lookup("endpointer");        
        
        Microphone microphone = (Microphone) cm.lookup("microphone");
        
        WavWriter wavWriter = (WavWriter) cm.lookup("wavWriter");
        wavWriter.setOutFilePattern(outputFile);                
        
        frontend.initialize();
        
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");        
            System.exit(1);
        }
        
        for (int i = 0; i < 10; i++) {
          System.err.println(i);
        }
        
        Data data = null;
        do {
            data = frontend.getData();
        } while (data != null);
               
        /*AudioFileDataSource dataSource = (AudioFileDataSource) cm
                .lookup("audioFileDataSource");
        System.out.println(inputFile);
        dataSource.setAudioFile(new File(inputFile), null);
        WavWriter wavWriter = (WavWriter) cm.lookup("wavWriter");
        wavWriter.setOutFilePattern(outputFile);

        frontend.initialize();

        Data data = null;
        do {
            data = frontend.getData();
        } while (data != null);*/
    }

    static private void processCtl(String inputCtl, String inputFolder,
            String outputFolder, ConfigurationManager cm)
            throws MalformedURLException, IOException {

        Scanner scanner = new Scanner(new File(inputCtl));
        while (scanner.hasNext()) {
            String fileName = scanner.next();
            String inputFile = inputFolder + "/" + fileName + ".wav";
            String outputFile = outputFolder + "/" + fileName + ".wav";
            processFile(inputFile, outputFile, cm);
        }
    }
}
