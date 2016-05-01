package on_off;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class morse_code extends Thread{
    
    String my_host;
    int my_port;
    FileReader input;
    Scanner in;
    String _morse;

    private String ConvertCharacter(char ch){
        String code = " ";
        if(ch == 'A' || ch == 'a') code = ".-";
        if(ch == 'B'|| ch == 'b') code = "-...";
        if(ch == 'C'|| ch == 'c') code = "-.-.";
        if(ch == 'D'|| ch == 'd') code = "-..";
        if(ch == 'E'|| ch == 'e') code = ".";
        if(ch == 'F'|| ch == 'f') code = "..-.";
        if(ch == 'G'|| ch == 'g') code = "--.";
        if(ch == 'H'|| ch == 'h') code = "....";
        if(ch == 'I'|| ch == 'i') code = "..";
        if(ch == 'J'|| ch == 'j') code = ".---";
        if(ch == 'K'|| ch == 'k') code = "-.-";
        if(ch == 'L'|| ch == 'l') code = ".-..";
        if(ch == 'M'|| ch == 'm') code = "--";
        if(ch == 'N'|| ch == 'n') code = "-.";
        if(ch == 'O'|| ch == 'o') code = "---";
        if(ch == 'P'|| ch == 'p') code = ".--.";
        if(ch == 'Q'|| ch == 'q') code = "--.-";
        if(ch == 'R'|| ch == 'r') code = ".-.";
        if(ch == 'S'|| ch == 's') code = "...";
        if(ch == 'T'|| ch == 't') code = "-";
        if(ch == 'U'|| ch == 'u') code = "..-";
        if(ch == 'V'|| ch == 'v') code = "...-";
        if(ch == 'W'|| ch == 'w') code = ".--";
        if(ch == 'X'|| ch == 'x') code = "-..-";
        if(ch == 'Y'|| ch == 'y') code = "-.--";
        if(ch == 'Z'|| ch == 'z') code = "--..";

        return code;
    }
    
    void setInfo() throws FileNotFoundException{
        input = new FileReader("input.dat");
        in = new Scanner(input);
    }
    
    void readin(){
        while(in.hasNext()){
            my_host = in.next();
            my_port = in.nextInt();
        }
    }
    
    void set_morse(String my_word){
        _morse = ConvertWord(my_word);
    }

    private String ConvertWord(String word){
        StringBuilder sb = new StringBuilder();
        char[] char_array = word.toCharArray();
        for(int i = 0; i < char_array.length; i++){
            sb.append(ConvertCharacter(char_array[i]));
        }
        return sb.toString();
    }

    public String ConvertText(String sentence)
    {
        String[] words = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String word : words)
        {
            if (sb.length() != 0) sb.append("|");
            sb.append(ConvertWord(word));
        }
        return sb.toString();
    }

    @Override
    public void run()
    {
        System.out.println("thread started in morse");
        My_Client client = new My_Client();
        
        try {
            setInfo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        readin();
            
            for(char ch: _morse.toCharArray())
            {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
            }

                    if(ch == '.')
                    {
                            try {
                                client.sendinfo(1, my_host, my_port);
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            try {
                                client.sendinfo(0, my_host, my_port);
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }
                    else if(ch== '-')
                    {
                        try {
                            client.sendinfo(1, my_host, my_port);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            client.sendinfo(0, my_host, my_port);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }/*
                else if(ch== ' '){
                        try {
                            TimeUnit.SECONDS.sleep(6);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }*/
                else {
                    try {
                            client.sendinfo(0, my_host, my_port);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    try {
                            TimeUnit.MILLISECONDS.sleep(3500);
                    } catch (InterruptedException ex) {
                            Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(morse_code.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                client.sendinfo(0, my_host, my_port);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.out.println("thread ended");
    }
}

