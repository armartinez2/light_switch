package on_off;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


class My_Client {
    
    public void sendinfo(int on_off, String host_info, int socket_num) throws IOException{
        String temp, temp2,temp3;
        if (on_off == 1){
            temp =  "on";
        }
        else if(on_off == 0) {
            temp =  "off";
        }
        else temp = "?";
        Socket socket = new Socket(host_info, socket_num);
        ByteBuffer encoded = Charset.forName("US-ASCII").encode(temp);
        socket.getOutputStream().write(encoded.array());
        socket.close();

    }
}
