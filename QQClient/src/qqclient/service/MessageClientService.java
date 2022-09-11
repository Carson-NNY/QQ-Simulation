package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

public class MessageClientService {

    public void privateChat(String content, String senderId, String getterId){

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId+" says to "+ getterId+" : " +content);
        // Because the corresponding sockey port is in the thread, you can find the port if you find the thread.
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void groupChat(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_GroupCHAT);
        message.setSender(senderId);
        message.setContent(content);
        System.out.println(senderId+" says to everyone: " +content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
