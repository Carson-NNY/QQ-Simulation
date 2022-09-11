package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Carson
 *
 * This class is used to complete a series of operations such as login verification of users.
 */
public class UserClientService {

    // Because user,socket information is used elsewhere, make it an attribute
    private User u= new User();
    private Socket socket;

    // see whether the login is successful or not according to id and pwd
    public boolean checkUser(String userId ,String pwd){
        boolean flag = false;
        u.setUserId(userId);
        u.setPasswd(pwd);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Send a message from user
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // Read the information sent back by server
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message)ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){

                // Once you create a thread that keeps communicating with the server: create a class ClientServiceThread
                ClientServiceThread cst = new ClientServiceThread(socket);// Here, port 9999 is passed in
                cst.start();
                // For multithreaded management, put in collection management (a class that creates a thread collection)
                ManageThread.addThread(userId,cst);

                flag= true;

            }else{
                // Login failed, but socket needs to be closed
                socket.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType (MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());


        // Get the output stream corresponding to this user
        try {
            OutputStream outputStream =
                    ManageThread.getClientServiceThread(u.getUserId()).getSocket().getOutputStream();
            // GetUseId can be used directly here, and there is no need to pass parameters because checkUser has been 
            // dynamically bound and the properties of User have been changed.

            ObjectOutputStream obs = new ObjectOutputStream(outputStream);
            obs.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());// Specify to Server which thread to exit

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" exit");
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void privateChat(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Please enter the person you want to chat with： ");
//        String obj  = scanner.next();
//        System.out.print("Please enter what you want to say： ");
//        String sentence = scanner.next();
//
//        String fullContent =obj+":"+sentence;
//        Message message = new Message();
//        message.setMesType(MessageType.MESSAGE_CLIENT_PRIVATECHAT);
//
//        message.setContent(fullContent);
//        message.setSender(u.getUserId());
//
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(message);
//            System.out.println(u.getUserId()+ " says to "+obj+" ："+sentence);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
