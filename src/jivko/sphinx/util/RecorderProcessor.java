package jivko.sphinx.util;

import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.frontend.endpoint.SpeechEndSignal;
import edu.cmu.sphinx.frontend.endpoint.SpeechStartSignal;
import edu.cmu.sphinx.frontend.util.DataUtil;
import edu.cmu.sphinx.util.props.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import jivko.dictionary.Dictionary;
import jivko.recognizer.Recognizer;
import jivko.recognizer.impl.RecognizerGoogleImpl;
import jivko.synthesizer.Synthesizer;
import jivko.synthesizer.impl.SyntesizerCloudGardenImpl;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecorderProcessor extends BaseDataProcessor {

  /**
   * The pathname which must obey the pattern: pattern + i + .wav. After each
   * DataEndSignal the smallest unused 'i' is determined. Pattern is padded to
   * create result file with fixed name lenght.
   */
  @S4String(defaultValue = "seg000000")
  public static final String PROP_OUT_FILE_NAME_PATTERN = "outFilePattern";
  
  @S4Boolean(defaultValue = false)
  public static final String PROP_IS_COMPLETE_PATH = "isCompletePath";
  /**
   * The property for the number of bits per value.
   */
  @S4Integer(defaultValue = 16)
  public static final String PROP_BITS_PER_SAMPLE = "bitsPerSample";
  /**
   * The property specifying whether the input data is signed.
   */
  @S4Boolean(defaultValue = true)
  public static final String PROP_SIGNED_DATA = "signedData";
  /**
   * The property specifying whether the input data is signed.
   */
  @S4Boolean(defaultValue = false)
  public static final String PROP_CAPTURE_UTTERANCES = "captureUtterances";
  
  private String fileName = "D:/tmp/tmp888.wav";
  private ByteArrayOutputStream baos;
  private DataOutputStream dos;
  
  private int sampleRate;
  private boolean isInSpeech;
  private boolean isSigned = true;
  private int bitsPerSample = 16;
  private String outFileNamePattern;
  protected boolean captureUtts = false;
  private boolean isCompletePath = false;
  
  
  Recognizer recognizer = new RecognizerGoogleImpl();
  Synthesizer synthesizer = new SyntesizerCloudGardenImpl();
  Dictionary dictionary = new Dictionary();

  public RecorderProcessor() {
  }

  @Override
  public Data getData() throws DataProcessingException {
    Data data = getPredecessor().getData();

    if (data instanceof DataStartSignal) {
      sampleRate = ((DataStartSignal) data).getSampleRate();
    }

    if (data instanceof DataStartSignal || (data instanceof SpeechStartSignal && captureUtts)) {
      baos = new ByteArrayOutputStream();
      dos = new DataOutputStream(baos);
    }



    if ((data instanceof DataEndSignal && !captureUtts) || (data instanceof SpeechEndSignal && captureUtts)) {

      String wavName = fileName;

      processUtterance(wavName);

      isInSpeech = false;
    }

    if (data instanceof SpeechStartSignal) {
      isInSpeech = true;
    }

    if ((data instanceof DoubleData || data instanceof FloatData) && (isInSpeech || !captureUtts)) {
      DoubleData dd = data instanceof DoubleData ? (DoubleData) data : DataUtil.FloatData2DoubleData((FloatData) data);
      double[] values = dd.getValues();

      for (double value : values) {
        try {
          dos.writeShort(new Short((short) value));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return data;
  }

  @Override
  public void initialize() {
    super.initialize();

    assert outFileNamePattern != null;
    baos = new ByteArrayOutputStream();
  }

  public static byte[] valuesToBytes(double[] values, int bytesPerValue, boolean signedData)
          throws ArrayIndexOutOfBoundsException {

    byte[] byteArray = new byte[bytesPerValue * values.length];

    int byteArInd = 0;

    for (double value : values) {
      int val = (int) value;


      for (int j = bytesPerValue - 1; j >= 0; j++) {
        byteArray[byteArInd + j] = (byte) (val & 0xff);
        val = val >> 8;
      }

      byteArInd += bytesPerValue;
    }

    return byteArray;
  }

  private static AudioFileFormat.Type getTargetType(String extension) {
    AudioFileFormat.Type[] typesSupported = AudioSystem.getAudioFileTypes();

    for (AudioFileFormat.Type aTypesSupported : typesSupported) {
      if (aTypesSupported.getExtension().equals(extension)) {
        return aTypesSupported;
      }
    }

    return null;
  }

  protected void processUtterance(String wavName) {
    try {
      writeFile(wavName);
      String response = recognizer.recognize(fileName);
      System.err.println(response);
      
      String answer = dictionary.findAnswer(response);
      synthesizer.talk(answer);
    } catch (Exception ex) {
      System.out.println(ex.toString());
      ex.printStackTrace();
    }
  }

  protected void writeFile(String wavName) {
    writeWavFile(wavName);
  }

  protected void writeWavFile(String wavName) {
    AudioFormat wavFormat = new AudioFormat(sampleRate, bitsPerSample, 1, isSigned, true);
    AudioFileFormat.Type outputType = getTargetType("wav");

    byte[] abAudioData = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(abAudioData);
    AudioInputStream ais = new AudioInputStream(bais, wavFormat, abAudioData.length / wavFormat.getFrameSize());

    File outWavFile = new File(wavName);

    if (AudioSystem.isFileTypeSupported(outputType, ais)) {
      try {
        AudioSystem.write(ais, outputType, outWavFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
