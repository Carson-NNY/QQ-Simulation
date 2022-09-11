package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this is for ClientService Thread
 */
public class ClientServiceThread extends Thread {
    // The thread needs to hold the Socket
    private Socket socket;

    // The constructor can accept an object of Socket
    public ClientServiceThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    // Because the thread sometimes needs to communicate with the server constantly in the background, use while loop
    public void run(){

        while(true){

            System.out.println("Client communication: waiting for messages sent from the server to be read");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //If the server does not send a Message object, the thread will get stuck here

                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] s= message.getContent().split(" ");
                    System.out.println("=========Current user list==========");
                    for (int i = 0; i < s.length; i++) {
                        System.out.println("user： "+ s[i]);
                    }

                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    System.out.println(message.getSender()+" says to "+ message.getGetter()
                            " :" +message.getContent());

                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_GroupCHAT)){
                    System.out.println(message.getSender()+" says to everyone："+message.getContent());

                } else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_FileSending)){
                    System.out.println(message.getSender() +" send a file to "+message.getSender());
                    byte[] data = message.getBytes();
                    String dest = message.getDestPath();

                    FileOutputStream fileOutputStream = new FileOutputStream(dest);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                    System.out.println("File saved successfully");
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_AdsMessages)){
                    System.out.println("The system says to everyone："+message.getContent());
                }

                else {
                    System.out.println("Do not deal with it for the time being");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
