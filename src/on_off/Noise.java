package on_off;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Noise extends Thread{
    
    boolean start_check = false;
        ByteArrayOutputStream byteArrayOutputStream;
        TargetDataLine targetDataLine;
        int cnt;
        boolean stopCapture = false;
        byte tempBuffer[] = new byte[8000];
        public static int countzero, countdownTimer;    
        short convert[] = new short[tempBuffer.length];
        int timer = 0;
        Scanner in;
        FileReader input;
        String my_host;
        int my_port;
        public boolean allowed = true;
        int send = 3;
        My_Client client;
        boolean changed = false;
        
        Noise(){
            client = new My_Client();
            
            try {
                setInfo();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            readin();
        }
        
        void setInfo() throws FileNotFoundException{
            input = new FileReader("input.dat");
            in = new Scanner(input);
        }
        
        public void allow(){
            allowed = true;
        }
        
        public void not_allow(){
            allowed = false;
        }
        
        void readin(){
        while(in.hasNext()){
            my_host = in.next();
            my_port = in.nextInt();
            System.out.println("the host is "+my_host);
            System.out.println("the port is "+my_port);
        }
    }
        
        @Override
        public void run(){
            byteArrayOutputStream = new ByteArrayOutputStream();
            stopCapture = false;
            countdownTimer = 0;

            while (!stopCapture) {
                AudioFormat audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                try {
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    targetDataLine.open(audioFormat);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
                }
                targetDataLine.start();
                cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                byteArrayOutputStream.write(tempBuffer, 0, cnt); 
                try {
                    countzero = 0;
                    for (int i = 0; i < tempBuffer.length; i++) {                                     
                        convert[i] = tempBuffer[i];
                        if (convert[i] == 0) {
                            countzero++;
                        }
                    }
                    
                    if(countzero > 700) start_check = true;
                    if(countzero < 500 && start_check == true&&send == 0){
                         send = 1;
                         changed = true;
                         System.out.println("sent on");
                    }
                    else if(countzero < 500 && start_check == true&& send == 1 ){
                        send = 0;
                        changed = true;
                        System.out.println("sent off");
                    }
                    else{
                        changed = false;
                        send = 0;
                    }
                    
                    countdownTimer++;
                    System.out.println(countzero + " " + countdownTimer+" "+send);
 
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
                if(changed == true){
                    try {
                        client.sendinfo(send,my_host, my_port);
                        System.out.println("message sent");
                    } catch (IOException ex) {
                        Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                targetDataLine.close();
            }
        }
    
}
