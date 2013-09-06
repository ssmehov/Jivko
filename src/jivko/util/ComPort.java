package jivko.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class ComPort {
  
  private InputStream in;
  private OutputStream out;

  public InputStream getIn() {
    return in;
  }

  public OutputStream getOut() {
    return out;
  }
  
  private String portName;
  private int speed;  

  public ComPort(String portName, int speed) throws Exception {
    this.portName = portName;
    this.speed = speed;
    
    connect(portName, speed);
  }
  
  void connect(String portName, int speed) throws Exception {
    CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
    if (portIdentifier.isCurrentlyOwned()) {
      System.out.println("Error: Port is currently in use");
    } else {
      CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

      if (commPort instanceof SerialPort) {
        SerialPort serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        in = serialPort.getInputStream();
        out = serialPort.getOutputStream();
        
      } else {
        System.out.println("Error: Only serial ports are handled");
      }
    }
  }    
}
