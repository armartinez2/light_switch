package on_off;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class FXMLDocumentController implements Initializable {
    My_Client myclient = new My_Client();
    String my_host;
    int my_port;
    FileReader input;
    Scanner in;
    boolean status = false;
    boolean listening = false;
    Noise noise;
    boolean started = false;
    
    void setInfo() throws FileNotFoundException{
        input = new FileReader("input.dat");
        in = new Scanner(input);
    }
    
    void make_some_noise(){
        try {
            noise = new Noise();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void readin(){
        while(in.hasNext()){
            my_host = in.next();
            my_port = in.nextInt();
        }
    }
    
    @FXML
    private Label label;
    @FXML
    private Button button_on;
    @FXML
    private Button button_off;
    @FXML
    private TextField morse_text;
    @FXML
    private Button button_morse;
    
    public TextField get_text(){
        return morse_text;
    }
    
    @FXML
    private void handleButtonAction1(ActionEvent event) {
        morse_code converter = new morse_code();
        int is_on;
        My_Client client = new My_Client();
        try {
            setInfo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        readin();
        try {
            client.sendinfo(1, my_host, my_port);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("device turned on");
        label.setText("device currently on");
        status = true;
    }
    
    @FXML
    private void handleButtonAction2(ActionEvent event){
        int is_off;
        My_Client client = new My_Client();
        try {
            setInfo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        readin();
        try {
            client.sendinfo(0,my_host, my_port);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("device turned off");
        label.setText("device currently off");
        status = false;
    }
    
    @FXML
    private void handleButtonAction3(ActionEvent event){
        if(noise == null){
            make_some_noise();
            System.out.println("made some noise");
        }
        label.setText("listening");
        if(started == false){
            noise.start();
            started = true;
        }
        else{
            noise.stopCapture = false;
            noise.run();
        }
        System.out.println("noise started");
  
    }
    
    @FXML
    private void handleButtonAction4(ActionEvent event){
        noise.stopCapture = true;
        noise.interrupt();
        noise = new Noise();
        started = false;
        label.setText("not currently listening");             
    }
    
    @FXML
    public void handleButtonAction5(ActionEvent event) {
        
        String my_code;
        morse_code morse = new morse_code();
        My_Client client = new My_Client();
        try {
            setInfo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        readin();
        try {
            client.sendinfo(0,my_host, my_port);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        my_code = morse_text.getText();
        morse.ConvertText(my_code);
        System.out.println(my_code);
        System.out.println(morse.ConvertText(my_code));
        morse.set_morse(my_code);
        morse.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
